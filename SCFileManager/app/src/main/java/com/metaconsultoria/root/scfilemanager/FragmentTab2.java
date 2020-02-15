package com.metaconsultoria.root.scfilemanager;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentTab2 extends Fragment{
    private Bundle instance;
    private String mainpath;
    public FragmentFileEx mfragment;
    public FragmentTab2() {
        // Required empty public constructor
    }

    public static FragmentTab2 newInstance() {
        FragmentTab2 fragment = new FragmentTab2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance =savedInstanceState;
        setRetainInstance(true);
        if (getArguments() != null) {
        }
        if(ConstantesDoProjeto.getInstance().isProtect()) {
            mainpath = ConstantesDoProjeto.getInstance().getMainPathProtected();
        }else{
            mainpath = ConstantesDoProjeto.getInstance().getMainPath();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_tab2, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState==null) {
            mfragment = new FragmentFileEx();
            Bundle arguments = new Bundle();
            Log.wtf("teste",mainpath);
            arguments.putString("arqpath", mainpath);
            arguments.putString("text", null);
            arguments.putBoolean("isGenerator",false);
            mfragment.setArguments(arguments);
            this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.file_ex_area, mfragment).commit();

            RecentFilesFragment mFragmentRecent = new RecentFilesFragment();
            this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.file_recent_area, mFragmentRecent).commit();
            ((MainNavigationDrawerActivity) getActivity()).setMainFragmentFileEx(mfragment);
        }
    }

    @Override
    public void onStart() {
        Log.wtf("here it is","porra");
        super.onStart();
    }
}
