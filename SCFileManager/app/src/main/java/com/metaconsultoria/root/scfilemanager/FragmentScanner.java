package com.metaconsultoria.root.scfilemanager;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.graphics.Color;

import java.io.File;


public class FragmentScanner extends Fragment implements ZXingScannerView.ResultHandler {
    private View fragmentRootView;
    private ZXingScannerView scannerView;
    private LinearLayout scannerViewLayout;
    private CustomZXingScannerView customZXingScannerView;
    private String mainpath = Environment.getExternalStorageDirectory().getPath();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentRootView = inflater.inflate(R.layout.fragmentscanner, container, false);
        scannerViewLayout = fragmentRootView.findViewById(R.id.scanner_view_layout);
        return fragmentRootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        customZXingScannerView = new CustomZXingScannerView(getContext());
        customZXingScannerView.setBorderColor(Color.GRAY);
        customZXingScannerView.setLaserColor(Color.BLUE);

        scannerView = new ZXingScannerView(getContext()) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return customZXingScannerView;
            }
        };
        scannerViewLayout.addView(scannerView);
        scannerView.setResultHandler(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.startCamera(0);
    }

    @Override
    public void handleResult(Result result) {
        if (result != null) {
            boolean exists = (new File(mainpath+result.getText())).exists();
            if(exists) {
                FragmentFileEx.FragmentListener mListener = (FragmentFileEx.FragmentListener) getActivity();
                mListener.Scanner(mainpath+result.getText());
            }else{
                Toast.makeText(getContext().getApplicationContext(),"Arquivo não encontrado",Toast.LENGTH_LONG).show();
                scannerView.resumeCameraPreview(this);
            }
        }
    }
}

