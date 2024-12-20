package com.probro.util;

import com.amazonaws.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class FileUtil {

    public String format(String content) {
        if (content == null) {
            return "";
        }

        return StringUtils.trim(content);
    }

    public String loadFile(String resourceName) throws Exception {
        String fileContent = "";

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);
        StringBuilder out = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        String read;
        while ((read = reader.readLine()) != null) {
            out.append(read);
            out.append("\r\n");
        }

        fileContent = out.toString();

        return fileContent;
    }
}
