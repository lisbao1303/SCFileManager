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

    public ShowComentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_show_coments, container, false);
        recyclerView=(RecyclerView) v.findViewById(R.id.reciclerView);
        layoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        coments=new ArrayList<MyComent>();
        coments.add(new MyComent("nome1","texto1","00/00/00"));
        coments.add(new MyComent("nome2","texto1","00/00/00"));
        coments.add(new MyComent("nome2","texto1","00/00/00"));
        coments.add(new MyComent("nome2","texto1","00/00/00"));
        coments.add(new MyComent("nome2","texto1","00/00/00"));
        coments.add(new MyComent("nome2","texto1","00/00/00"));
        recyclerView.setAdapter(new ComentAdapter(getContext(),coments,this));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClickComent(View view, int idx) {

    }
}
