package com.metaconsultoria.root.scfilemanager;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import java.io.File;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class FragmentScanner extends Fragment implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    private String mainpath = Environment.getExternalStorageDirectory().getPath();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentscanner,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mScannerView = view.findViewById(R.id.z_xing_scanner);
        mScannerView.setBorderColor(Color.YELLOW);
        mScannerView.setLaserColor(Color.BLUE);
        mScannerView.setAutoFocus(true);
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void onStop() {
        super.onStop();
        mScannerView.stopCamera();
    }
    @Override
    public void handleResult(Result result) {
        if(result != null){
            if (result.getText() != null){
                boolean exists = (new File(mainpath + result.getText())).exists();
                if(exists){
                    FragmentFileEx.FragmentListener mListener = (FragmentFileEx.FragmentListener) getActivity();
                    mListener.Scanner(mainpath+result.getText());
                }else{
                    onResume();
                    Toast.makeText(getActivity().getApplicationContext(),"Arquivo não encontrado",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}

