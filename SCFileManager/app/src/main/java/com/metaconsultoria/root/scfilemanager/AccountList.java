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
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountList extends Fragment implements FuncAdapter.FuncOnClickListener{
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private List<Funcionario> funcs;
    private FuncAdapter funcAdapter;

    public AccountList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FuncDB funcDB=new FuncDB(getContext());
        funcs=funcDB.findAll();

        View v= inflater.inflate(R.layout.fragment_account_list, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recicler_func);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        funcAdapter= new FuncAdapter(getContext(),funcs,this);
        recyclerView.setAdapter(funcAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onClickFunc(View view, int idx) {

    }

    @Override
    public void onClickRemove(View v, int id) {
        Funcionario func = funcs.get(id);
        FuncDB funcDB= new FuncDB(getContext());
        funcDB.deleteByMatricula(func.getMatricula());
        funcs.remove(id);
        funcAdapter.notifyItemRemoved(id);
    }
}
