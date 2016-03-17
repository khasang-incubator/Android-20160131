package com.khasang.vkphoto.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.khasang.vkphoto.presentation.model.PhotoAlbum;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileManager {
    public static final String BASE_DIR_NAME = "VK Photo";
    public static final String BASE_DIRECTORY = "base_directory";
    public static final String JPEG_FORMAT = "/%d.jpg";

    public static String createAlbumDirectory(String albumName, Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        File baseDirectory = getBaseDirectory(defaultSharedPreferences);
        if (baseDirectory == null) {
            initBaseDirectory(context);
            baseDirectory = getBaseDirectory(defaultSharedPreferences);
        }
        assert baseDirectory != null;
        File albumDirectory = new File(baseDirectory.getAbsolutePath() + "/" + albumName);
        if (checkDirectoryExists(albumDirectory) || albumDirectory.mkdirs()) {
            return albumDirectory.getAbsolutePath();
        } else {
            return null;
        }
    }

    public static boolean deleteAlbumDirectory(String path) {
        File albumDirectory = new File(path);
        return checkDirectoryExists(albumDirectory) && deleteDir(albumDirectory);
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }

    public static boolean initBaseDirectory(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        File storageDirectory = StorageUtils.getStorageDirectories(context);
        if (storageDirectory == null) {
            Logger.d("storageDirectory = null");
            return false;
        }
        File baseDirectory = new File(storageDirectory.getAbsolutePath() + "/" + BASE_DIR_NAME);
        if (checkDirectoryExists(baseDirectory) || baseDirectory.mkdirs()) {
            if (!defaultSharedPreferences.getString(BASE_DIRECTORY, "").equals(baseDirectory.getAbsolutePath())) {
                SharedPreferences.Editor editor = defaultSharedPreferences.edit();
                editor.putString(BASE_DIRECTORY, baseDirectory.getAbsolutePath());
                editor.apply();
            }
            return true;
        }
        return false;
    }

    public static File getBaseDirectory(SharedPreferences sharedPreferences) {
        String basePath = sharedPreferences.getString(BASE_DIRECTORY, "");
        if (basePath.equals("")) {
            return null;
        }
        File baseDirectory = new File(basePath);
        return checkDirectoryExists(baseDirectory) ? baseDirectory : null;
    }

    private static boolean checkDirectoryExists(File file) {
        return file.exists() && file.isDirectory();
    }

    public static File saveImage(String urlPath, PhotoAlbum photoAlbum, int photoId) {
        int count;
        File file = null;
        try {
            URL url = new URL(urlPath);
            URLConnection urlConnection = url.openConnection();
            //  long total = 0;
            urlConnection.connect();
            String targetFileName = String.format(JPEG_FORMAT, photoId);
            //  int lenghtOfFile = urlConnection.getContentLength();
            String folderPath = photoAlbum.filePath + "/";
            String filePath = folderPath + targetFileName;
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();//If there is no folder it will be created.
            }
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(filePath, false);
            byte data[] = new byte[1024];
            while ((count = input.read(data)) != -1 && !Thread.currentThread().isInterrupted()) {
                //      total += count;
//                publishProgress((int) (total * 100 / lenghtOfFile));
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            file = new File(filePath);
        } catch (Exception e) {
            Logger.d(e.toString());
        }
        return file;
    }
}
