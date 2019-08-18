package com.example.homework522;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FileHelper {
    private Context context;
    private String fileName;

    public FileHelper(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    //создаем системный файл для хранения данных
        try {
            File file = new File(context.getFilesDir(), fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //обновляем данные
    public void updateValue(String login, String pass) {
        FileOutputStream outputStream;
        String value=login+"\n"+pass;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(value.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //получаем данные из файла
    public String getValue() {
        StringBuilder output = new StringBuilder();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.openFileInput(fileName));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = "";
            // читаем содержимое по строкам (строки во время записи можно разделить при помощи "\n")
            while((line=reader.readLine())!=null){
                output.append(line).append(";");
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}
