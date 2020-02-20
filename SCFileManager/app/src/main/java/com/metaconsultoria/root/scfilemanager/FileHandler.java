package com.metaconsultoria.root.scfilemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v4.provider.DocumentFile;
import android.util.Log;

import java.io.*;

public class FileHandler{


    static public void copyFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                if(!dir.mkdirs()){}
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            out.flush();
            out.close();
            out = null;

        }  catch (FileNotFoundException fnfe1) {
            Log.e("tag123", fnfe1.getMessage());
            fnfe1.printStackTrace();
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    static public void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath + inputFile).delete();


        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
            fnfe1.printStackTrace();
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
            e.printStackTrace();
        }

    }

    public static void deleteFile(String inputPath, String inputFile) {
        try {
            // delete the original file
            new File(inputPath + inputFile).delete();
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    public static String saveImageToInternalStorage(Bitmap image, Context contx,String arqName ) {
        File pictureFile = getOutputMediaFile(contx,arqName);
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            return Uri.fromFile(pictureFile).toString();
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return null;
        }
    }

    static private  File getOutputMediaFile(Context contx, String arqName){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + contx.getApplicationContext().getPackageName()
                + "/Files/QR_images");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        String mImageName="QR_"+ arqName +".jpeg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public static void copyToProvider(String arqpath,String provider){
        String path=arqpath.substring(0,arqpath.lastIndexOf('/'));
        String arq=arqpath.substring(arqpath.lastIndexOf('/'));
        FileHandler.copyFile(path,arq,provider);
    }

    public static void openWith(String arqpath, String name, String tipo, Activity act){
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri ur = Uri.parse(arqpath);
            File mfile = new File(act.getFilesDir().getPath());
            mfile.mkdir();
            File provider = new File(mfile.getPath(),"/storage");
            FileHandler.copyToProvider(ur.getPath(),provider.getPath());
            File shareFile=new File(provider.getPath()+"/"+name);

            Log.wtf("open with",shareFile.getPath());
            Uri uri = FileProvider.getUriForFile(act, act.getPackageName()+".fileprovider", shareFile);

            intent = new Intent(Intent.ACTION_VIEW)
                    .setDataAndType(uri,tipo)
                    .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent =intent.createChooser(intent,act.getString(R.string.send_to));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            act.startActivity(intent);
        } else {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(arqpath), tipo);
            intent = Intent.createChooser(intent, act.getString(R.string.send_to));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            act.startActivity(intent);
        }
    }

    public static void copiDirectory(String inputPath, String inputDirectory, String outputPath){
        String inputPathDir=inputPath+inputDirectory;
        String outputPathDir=outputPath+inputDirectory;
        File inputPathDirectory = new File(inputPathDir);
        File outputPathDirectory = new File(outputPathDir);
        if(outputPathDirectory.exists()){
            deleteDirectory(outputPathDirectory);
        }
        File[] subDirs = inputPathDirectory.listFiles();
        for (File fileIterator: subDirs) {
            if(fileIterator.isDirectory()){
                copiDirectory(inputPathDir,"/"+fileIterator.getName(),outputPathDir);
            }else if(fileIterator.isFile()){
                copyFile(inputPathDir,"/"+fileIterator.getName(),outputPathDir);
            }
        }
    }

    public static void deleteDirectory(File fileToDelete){
        File[] subDirs = fileToDelete.listFiles();
        for (File fileIterator: subDirs) {
            if(fileIterator.isDirectory()){
                deleteDirectory(fileIterator);
            }else if(fileIterator.isFile()){
                fileIterator.delete();
            }
        }
        fileToDelete.delete();
    }

}
