package com.metaconsultoria.root.scfilemanager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecentFilesFragment extends Fragment implements RecentAdapter.RecentOnClickListener{
    private View mView;
    private ListView m_ArqList;
    private List<MyArquive> myArquiveList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<MyArquive> recents;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stared, container, false);
        recyclerView=v.findViewById(R.id.stared_recicler_view);
        layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return v;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecentFilesDB db=new RecentFilesDB(getContext());
        recents=db.mySelect(15);
        if(recents!=null) {recyclerView.setAdapter(new RecentAdapter(getContext(),recents,this));}
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    public void onClickStared(View view, int idx) {
        ((MainNavigationDrawerActivity)getActivity()).setPdfActivity(recents.get(idx));
    }


}
