package com.example.homework522;


import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileHelper {
    private Context context;


    public FileHelper(Context context) {
        this.context = context;
    }

    //создаем файл во внутреннем хранилище
    public void createFileRegInternal(String fileName) {
        String actualFile = context.getFilesDir() + "/" + fileName;
        createFile(actualFile);
    }

    //создаем файл во внешнем хранилище
    public void createFileRegExternal(String fileName) {
        if (isExternalStorageWritable()) {
            String actualFile = context.getApplicationContext().getExternalFilesDir(null) + "/" + fileName;
            createFile(actualFile);
        } else {
            Toast.makeText(context, "File Error", Toast.LENGTH_LONG).show();
        }
    }

    public void createFile(String fileName) {
        try {
            File fileSetting = new File(fileName);
            if (!fileSetting.exists()) {
                fileSetting.createNewFile();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //обновляем данные во внутреннем хранилище
    public void updateInternalValue(String fileName, String login, String pass) {
        FileOutputStream outputStream;
        String value = login + "\n" + pass;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(value.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //обновляем данные во внешнем хранилище
    public void updateExternalValue(String fileName, String login, String pass) {
        String actualFile = context.getApplicationContext().getExternalFilesDir(null) + "/" + fileName;
        String value = login + "\n" + pass;
        try {
            FileWriter textFileWriter = new FileWriter(actualFile, true);
            textFileWriter.write(value);
            textFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //получаем данные из файла из внутреннего хранилища
    public String getInternalValue(String fileName) {
        FileInputStream fis = null;
        StringBuilder output = new StringBuilder();
        try {
            fis = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = "";
            // читаем содержимое по строкам
            while ((line = reader.readLine()) != null) {
                output.append(line).append(";");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                }
            }
        }
        return output.toString();
    }

    //получаем данные из файла из внешнего хранилища
    public String getExternalValue(String fileName) {
        String actualFile = context.getApplicationContext().getExternalFilesDir(null) + "/" + fileName;
        File fileReg = new File(actualFile);
        FileInputStream fis = null;
        StringBuilder output = new StringBuilder();
        try {
            fis = new FileInputStream(fileReg);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = "";
            // читаем содержимое по строкам
            while ((line = reader.readLine()) != null) {
                output.append(line).append(";");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                }
            }
        }
        return output.toString();
    }

    //удаляем файл во внутреннем хранилище
    public void deleteInternalFile(String fileName) {
        try {
            String actualFile = context.getFilesDir() + "/" + fileName;
            new File(actualFile).delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //удаляем файл во внешнем хранилище
    public void deleteExternalFile(String fileName) {
        if (isExternalStorageWritable()) {
            String actualFile = context.getApplicationContext().getExternalFilesDir(null) + "/" + fileName;
            new File(actualFile).delete();
        } else {
            Toast.makeText(context, "File Error", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}