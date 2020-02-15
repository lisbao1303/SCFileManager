package com.metaconsultoria.root.scfilemanager;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowComentsFragment extends Fragment implements ComentAdapter.ComentOnClickListener {
    protected RecyclerView recyclerView;
    private List<MyComent> coments;
    private LinearLayoutManager layoutManager;
    private RecentFilesDB db;
    private MyArquive arq;
    private ComentAdapter comentAdapter;

    public ShowComentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            db = new RecentFilesDB(getContext());
            coments = db.findAllComent(arq);
            if (coments == null) {
                return inflater.inflate(R.layout.fragment_show_coments_null, container, false);
            } else {
                View v = inflater.inflate(R.layout.fragment_show_coments, container, false);
                recyclerView = (RecyclerView) v.findViewById(R.id.reciclerView);
                layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                return v;
            }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            comentAdapter = new ComentAdapter(getContext(), coments, this);
            if (coments != null) {
                recyclerView.setAdapter(comentAdapter);
            }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClickComent(View view, int idx) {

    }

    @Override
    public void onClickDellComent(View view, int idx) {
        db.deleteComentByID(arq,coments.get(idx).getId());
        coments.remove(idx);
        comentAdapter.refreshComentList(coments);
        comentAdapter.notifyItemRemoved(idx);
    }

    @Override
    public void onClickEditComent(View view, int idx) {

    }

    public MyArquive getArq() {
        return arq;
    }

    public void setArq(MyArquive arq) {
        this.arq = arq;
    }
}
