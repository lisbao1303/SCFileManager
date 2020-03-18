package com.metaconsultoria.root.scfilemanager.utils;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;

import com.metaconsultoria.root.scfilemanager.ConstantesDoProjeto;

import java.io.File;

public class ExternalDirsUtils {
    public static String[] getSdDirs(Activity act){
        File[] teste = act.getExternalFilesDirs(null);
        String buffer="";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for(int i=0;i<teste.length;i++){
                if(Environment.isExternalStorageRemovable((teste[i]))){
                    buffer=teste[i].getPath();
                    break;
                }
                if(i==(teste.length-1)){
                    buffer=teste[0].getPath();
                }
            }
        }else{
            if(teste.length>1){
                buffer=teste[1].getPath();
            }else{
                buffer=teste[0].getPath();
            }
        }
        String[] strings=new String[2];
        strings[0]=buffer;
        strings[1]=teste[0].getPath();
        return strings;
    }

    public static boolean prepareSdDirs(Activity act){
        String[] buffer = ExternalDirsUtils.getSdDirs(act);

        String protectPath=buffer[0].substring(0,buffer[0].indexOf("/Android"));
        String storage= buffer[1];
        ConstantesDoProjeto.getInstance().setMainPath(protectPath);
        ConstantesDoProjeto.getInstance().setMainPathProtected(buffer[0]+"/ArquivosSouza");
        ConstantesDoProjeto.getInstance().setMainPathProtectedCopy(buffer[0]);
        ConstantesDoProjeto.getInstance().setBackUpPath(storage);

        File testinho =new File(ConstantesDoProjeto.getInstance().getMainPathProtected());
        if(!testinho.exists()){
            testinho.mkdirs();
        }
        return true;
    }
}
