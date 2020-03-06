package se1302.demo.contentproviderdemo.services;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.nio.channels.*;
import java.util.*;

interface IUltilService {
    public void copyFile(String src, String des);
    public List<String> copyFileFromDataDirToSDCard(String src, String des);
    public boolean checkDB(String databaseName);
    public void copyDB(InputStream input, OutputStream output);
    public void copyDBFromAssetToData(String des, String fileName);
}
public class UltilService implements IUltilService{
    private Context context;
    public UltilService(Context context) {
        this.context = context;
    }

    @Override
    public void copyFile(String src, String des) {
        File srcFile = new File(src);
        File desFile = new File(des);
        try {
            FileChannel srcFileChannel = new FileInputStream((srcFile)).getChannel();
            FileChannel desFileChannel = new FileInputStream((desFile)).getChannel();
            try {
                desFileChannel.transferFrom(srcFileChannel, 0, srcFileChannel.size());
            } catch (Exception e) {
                Log.d("DatabaseService","Error at CopyFile " + e.getMessage());
            } finally {
                try {
                    if (srcFileChannel != null) {
                        srcFileChannel.close();
                    }
                    if (desFileChannel != null) {
                        desFileChannel.close();
                    }
                } catch (Exception e) {
                    Log.d("DatabaseService","Error at CopyFile " + e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.d("DatabaseService","Error at CopyFile " + e.getMessage());
        }
    }

    @Override
    public List<String> copyFileFromDataDirToSDCard(String src, String des) {
        File sdCard = Environment.getExternalStorageDirectory();
        String realPath = sdCard.getAbsolutePath();
        Log.d("DatabaseService", "sdCard Path: " + realPath);

        File data = Environment.getDataDirectory();
        String desDir = realPath + "/" + des;

        File directory = new File(desDir);
        if (!directory.exists()) {
            directory.mkdir();
        }

        File dataDir = new File(data, src);

        File[] files = dataDir.listFiles();
        List<String> result = new ArrayList<String>();
        if(files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                result.add(file.getName());
                copyFile(file.getAbsolutePath(), desDir + "/" + file.getName());
            }
        }
        return result;
    }

    @Override
    public boolean checkDB(String databaseName) {
        boolean check = false;
        File dbFile = null;
        try {
            String dbPath = "/data/data/" + context.getPackageName() + "/databases/" + databaseName;
            Log.d("DatabaseService", "dbPath: " + dbPath);
            dbFile = new File(dbPath);
            check = dbFile.exists();
        } catch (Exception e) {
            Log.d("DatabaseService","Error at CheckDB " + e.getMessage());
        } finally {
            dbFile = null;
        }
        return check;
    }

    @Override
    public void copyDB(InputStream input, OutputStream output) {
        byte[] buffer = new byte[1024];
        try {
            while (input.read(buffer) > 0) {
                output.write(buffer, 0, input.read(buffer));
            }
        } catch (Exception e) {
            Log.d("DatabaseService","Error at CopyDB " + e.getMessage());
        } finally {
            try {
                output.flush();
                input.close();
                output.close();
            } catch (Exception e) {
                Log.d("DatabaseService","Error at CopyDB " + e.getMessage());
            }

        }
    }

    @Override
    public void copyDBFromAssetToData(String des, String fileName) {
        File directory = new File(des);
        if (!directory.exists()) {
            directory.mkdir();
        }

        File file = new File(directory.getAbsolutePath(), fileName);
        try {
            InputStream input = context.getAssets().open(fileName);
            OutputStream output = new FileOutputStream(des + "/" + fileName);
            copyDB(input, output);
        } catch (Exception e) {
            Log.d("DatabaseService","Error at CopyDBFromAssetToData " + e.getMessage());
        }

    }
}
