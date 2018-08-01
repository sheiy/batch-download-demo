package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void download()throws Exception{
        D.download("https://download.jetbrains.8686c.com/idea/ideaIU-2018.2.exe",4);
    }

    @Test
    public void genCSV() throws Exception {
        Path path = Paths.get("d:", "test.csv");

        BufferedWriter writer = Files.newBufferedWriter(path);
        String[] title = {"name", "id", "type", "mobile", "nationId", "bankCard"};
        String titleLine = Arrays.asList(title).toString().replaceFirst("\\[", "")
                .replaceFirst("]", "");
        writer.write(titleLine);
        writer.newLine();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 200000; i++) {
            for (int j = 0; j < 6; j++) {
                stringBuilder.append(title[j]).append(Math.random());
                if (j != 5) {
                    stringBuilder.append(",");
                }
            }
            writer.write(stringBuilder.toString());
            stringBuilder.delete(0,stringBuilder.length());
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }
}
