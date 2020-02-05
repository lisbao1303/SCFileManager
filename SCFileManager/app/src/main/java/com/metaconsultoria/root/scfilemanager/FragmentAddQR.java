package com.metaconsultoria.root.scfilemanager;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class FragmentAddQR extends Fragment {


    public FragmentAddQR() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_add_qr, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentFileEx mfragment = new FragmentFileEx();
        Bundle arguments = new Bundle();
        arguments.putString("arqpath", Environment.getExternalStorageDirectory().getPath());
        arguments.putString("text", null);
        arguments.putBoolean("isGenerator",true);
        mfragment.setArguments(arguments);

        this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.replace_add_qr_fragment, mfragment).commit();
    }
}
