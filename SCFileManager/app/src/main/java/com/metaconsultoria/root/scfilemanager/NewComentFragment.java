package com.metaconsultoria.root.scfilemanager;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;


public class NewComentFragment extends Fragment {
    private int fragHeight;
    public NewComentFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inf=inflater.inflate(R.layout.fragment_new_coment, container, false);

        fragHeight=inf.getMeasuredHeight();
        Log.wtf("tamanho",String.valueOf(fragHeight));
        return inf;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }


    public int getFragHeight() {
        return fragHeight;
    }

    public void setFragHeight(int fragHeight) {
        this.fragHeight = fragHeight;
    }
}
