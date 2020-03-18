package com.metaconsultoria.root.scfilemanager.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtils {
    public static boolean checkPermissoes(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result_1 = ContextCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE);
            int result_2 = ContextCompat.checkSelfPermission(act,Manifest.permission.CAMERA);
            int result_3 = ContextCompat.checkSelfPermission(act,Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return (result_1 == PackageManager.PERMISSION_GRANTED &&
                    result_2 == PackageManager.PERMISSION_GRANTED &&
                    result_3 == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    public static void requestPermissoes(Activity act) throws Exception {
        try {
            ActivityCompat.requestPermissions(act,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                    }, 0x3);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
