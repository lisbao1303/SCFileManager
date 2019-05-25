package com.example.root.recomeco;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentMain extends Fragment {
    public FragmentMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_main, container, false);

        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction fragtran = fm.beginTransaction();
        MainFragmentUm frag = new MainFragmentUm();
        fragtran.add(R.id.mapas,frag,"MainFragmentUm");
        fragtran.commit();

        FragmentManager fm1 = this.getFragmentManager();
        FragmentTransaction fragtran1 = fm1.beginTransaction();
        MainFragmentDois frag1 = new MainFragmentDois();
        fragtran1.add(R.id.descricao,frag1,"MainFragmentUm");
        fragtran1.commit();
        return view;
    }
}