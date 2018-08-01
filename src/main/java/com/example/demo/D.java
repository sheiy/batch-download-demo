package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Slf4j
public abstract class D {

    private static final CloseableHttpClient CLIENT = HttpClients.createMinimal();

    private static final ThreadPoolExecutor EXECUTOR;

    static {
        int cpuNum = Runtime.getRuntime().availableProcessors();
        EXECUTOR = new ThreadPoolExecutor(cpuNum, cpuNum << 1, 5L, TimeUnit.MINUTES, new ArrayBlockingQueue<>(100), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "下载线程");
            }
        });
    }

    public static File download(String url, int threadNumber) throws Exception {
        Path file;
        HttpGet httpGet = new HttpGet(url);
        List<FutureTask<Boolean>> tasks = new ArrayList<>();
        try (CloseableHttpResponse response = CLIENT.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IllegalStateException("连接异常,StatusCode=" + response.getStatusLine().getStatusCode());
            }
            long length = entity.getContentLength();
            log.info("文件总长:" + length + "字节");
            file = Files.createTempFile(UUID.randomUUID().toString().replaceAll("-", ""), ".tmp");
            log.info("文件将存储到:{}", file.toFile().getAbsolutePath());
            RandomAccessFile accessFile = new RandomAccessFile(file.toFile(), "rw");
            accessFile.setLength(length);
            accessFile.close();
            long blockSize = length / threadNumber;
            for (int i = 0; i < threadNumber; i++) {
                long endPos;
                long startPos = i * blockSize;
                if (i == threadNumber - 1) {
                    endPos = length;
                } else {
                    endPos = (i + 1) * blockSize - 1;
                }
                log.info("线程" + i + "下载的部分为：" + startPos + "---" + endPos);
                FutureTask<Boolean> futureTask = new FutureTask<>(() -> {
                    HttpGet get = new HttpGet(url);
                    get.addHeader("Range", "bytes=" + startPos + "-" + endPos);
                    CloseableHttpResponse execute = CLIENT.execute(get);
                    //206表示文件分段请求,而不是整个文件请求
                    if (execute.getStatusLine().getStatusCode() != 206) {
                        throw new IllegalStateException("不支持断点下载");
                    }
                    InputStream is = execute.getEntity().getContent();
                    int len = 0;
                    byte[] buf = new byte[1024];
                    RandomAccessFile raf = new RandomAccessFile(file.toFile(), "rw");
                    raf.seek(startPos);
                    while ((len = is.read(buf)) > 0) {
                        raf.write(buf, 0, len);
                    }
                    is.close();
                    return Boolean.TRUE;
                });
                tasks.add(futureTask);
                EXECUTOR.execute(futureTask);
            }
        }
        while (tasks.stream().anyMatch(task -> !task.isDone())) {
        }
        return file.toFile();
    }
}