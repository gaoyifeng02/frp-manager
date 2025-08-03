package com.gaoyifeng.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * 进程管理工具类，用于管理frpc进程的启动、停止和日志收集
 */
public class ProcessManager {

    private static final Logger logger = LoggerFactory.getLogger(ProcessManager.class);

    private final String[] command;
    private final String logFile;
    private final Consumer<String> outputConsumer;
    private Process process;
    private ExecutorService executorService;
    private Instant startTime;
    private Long pid;

    /**
     * 构造函数
     *
     * @param command        启动命令
     * @param logFile        日志文件路径
     * @param outputConsumer 输出消费者，用于接收进程的输出
     */
    public ProcessManager(String[] command, String logFile, Consumer<String> outputConsumer) {
        this.command = command;
        this.logFile = logFile;
        this.outputConsumer = outputConsumer;
    }

    /**
     * 启动进程
     *
     * @return 是否成功启动
     */
    public synchronized boolean start() {
        if (isRunning()) {
            logger.warn("进程已经在运行中");
            return false;
        }

        try {
            // 创建进程构建器
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true); // 将错误流合并到标准输出流

            // 启动进程
            process = processBuilder.start();
            startTime = Instant.now();

            // 获取进程ID
            try {
                // Java 9+
                pid = process.pid();
            } catch (UnsupportedOperationException e) {
                // Java 8 或更早版本，无法获取PID
                pid = null;
            }

            // 创建线程池
            executorService = Executors.newFixedThreadPool(2);

            // 处理进程输出
            executorService.submit(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String logLine = line;

                        // 将输出发送给消费者
                        if (outputConsumer != null) {
                            outputConsumer.accept(logLine);
                        }

                        // 如果指定了日志文件，将输出写入日志文件
                        if (logFile != null && !logFile.isEmpty()) {
                            try (FileWriter fw = new FileWriter(logFile, true);
                                 BufferedWriter bw = new BufferedWriter(fw);
                                 PrintWriter out = new PrintWriter(bw)) {
                                out.println(logLine);
                            } catch (IOException e) {
                                logger.error("写入日志文件失败", e);
                            }
                        }
                    }
                } catch (IOException e) {
                    logger.error("读取进程输出失败", e);
                }
            });

            // 监控进程退出
            executorService.submit(() -> {
                try {
                    int exitCode = process.waitFor();
                    logger.info("进程已退出，退出码: {}", exitCode);

                    // 进程退出后清理资源
                    cleanup();

                    // 通知进程退出
                    if (outputConsumer != null) {
                        outputConsumer.accept("进程已退出，退出码: " + exitCode);
                    }
                } catch (InterruptedException e) {
                    logger.error("等待进程退出被中断", e);
                    Thread.currentThread().interrupt();
                }
            });

            logger.info("进程已启动: {}", String.join(" ", command));
            return true;
        } catch (IOException e) {
            logger.error("启动进程失败", e);
            cleanup();
            return false;
        }
    }

    /**
     * 停止进程
     *
     * @return 是否成功停止
     */
    public synchronized boolean stop() {
        if (!isRunning()) {
            logger.warn("进程未运行");
            return false;
        }

        try {
            // 先尝试正常终止进程
            process.destroy();

            // 等待进程终止，最多等待5秒
            boolean terminated = process.waitFor(5, java.util.concurrent.TimeUnit.SECONDS);

            // 如果进程未能在5秒内终止，强制终止
            if (!terminated) {
                process.destroyForcibly();
                terminated = process.waitFor(5, java.util.concurrent.TimeUnit.SECONDS);
            }

            if (terminated) {
                logger.info("进程已终止");
            } else {
                logger.warn("无法终止进程");
            }

            // 清理资源
            cleanup();

            return terminated;
        } catch (InterruptedException e) {
            logger.error("等待进程终止被中断", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 清理资源
     */
    private synchronized void cleanup() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
        process = null;
        pid = null;
        startTime = null;
    }

    /**
     * 检查进程是否在运行
     *
     * @return 是否在运行
     */
    public synchronized boolean isRunning() {
        return process != null && process.isAlive();
    }

    /**
     * 获取进程ID
     *
     * @return 进程ID
     */
    public synchronized Long getPid() {
        return pid;
    }

    /**
     * 获取进程启动时间
     *
     * @return 启动时间
     */
    public synchronized Instant getStartTime() {
        return startTime;
    }

    /**
     * 获取进程运行时间（秒）
     *
     * @return 运行时间
     */
    public synchronized long getUptime() {
        if (startTime == null) {
            return 0;
        }
        return Duration.between(startTime, Instant.now()).getSeconds();
    }
}
