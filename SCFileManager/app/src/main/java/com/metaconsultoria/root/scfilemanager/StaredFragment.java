package com.metaconsultoria.root.scfilemanager;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StaredFragment extends Fragment implements StaredAdapter.StaredOnClickListener{
        private RecyclerView recyclerView;
        private GridLayoutManager layoutManager;
        private List<MyArquive> stareds;
        private RecentFilesDB db;

    public StaredFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stared, container, false);
        recyclerView=v.findViewById(R.id.stared_recicler_view);
        if(getResources().getConfiguration().screenWidthDp>480) {
            layoutManager = new GridLayoutManager(getContext(), 5);
        }else{
            layoutManager = new GridLayoutManager(getContext(), 3);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return v;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db=new RecentFilesDB(getContext());
        stareds=db.selectStareds();
        if(stareds!=null) {recyclerView.setAdapter(new StaredAdapter(getContext(),stareds,this));}
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onClickStared(View view, int idx) {
        ((MainNavigationDrawerActivity)getActivity()).setPdfActivity(stareds.get(idx));
    }

}
