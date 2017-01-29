package com.oscl.www.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by GritsenkoVA on 24.01.17.
 */
public class StrFileReader {//жосткий файлордер (умеет по умолчанию и по данному ему имени)
    private final static String FILE_NAME = "src/com/oscl/www/Data";
    private List<String> lines;

    public StrFileReader(){
        try {
            lines = Files.readAllLines(Paths.get(FILE_NAME));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public StrFileReader (String fileName){
        try {
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public List<String> getLines() {
        return lines;
    }
}
