package com.example.homework522;


import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

public class FileHelper {
    private Context context;


    public FileHelper(Context context) {
        this.context = context;
    }


    //обновляем данные во внутреннем хранилище
    public void updateInternalValue(String fileName, String login, String pass) {
        FileOutputStream fos = null;
        String value = login + "\n" + pass;
        try {
            fos = context.openFileOutput(fileName, MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(value);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                }
            }
        }
    }

    //обновляем данные во внешнем хранилище
    public void updateExternalValue(String fileName, String login, String pass) {
        String actualFile = context.getApplicationContext().getExternalFilesDir(null) + "/" + fileName;
        String value = login + "\n" + pass;
        FileWriter textFileWriter = null;
        try {
            textFileWriter = new FileWriter(actualFile, true);
            textFileWriter.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (textFileWriter != null) {
                try {
                    textFileWriter.close();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                }
            }
        }
    }

    //получаем данные из файла из внутреннего хранилища
    public String getInternalValue(String fileName) {
        FileInputStream fis = null;
        StringBuilder output = new StringBuilder();
        String line = "";
        try {
            fis = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            // читаем содержимое по строкам
            while ((line = reader.readLine()) != null) {
                output.append(line).append(";");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return line;
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
        String line = "";
        try {
            fis = new FileInputStream(fileReg);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            // читаем содержимое по строкам
            while ((line = reader.readLine()) != null) {
                output.append(line).append(";");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return line;
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
            Toast.makeText(context, context.getResources().getString(R.string.msg_file_error), Toast.LENGTH_LONG).show();
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