package com.example.root.recomeco;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import java.util.ArrayList;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragmentDois extends Fragment {
    private List<Oportunidade> Oportunidades;
    public MainFragmentDois() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_fragment_dois, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.reciclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        Oportunidades = new ArrayList<Oportunidade>();
        Oportunidades.add(new Oportunidade("Pizzaiolo","Pizaria atalaia"));
        Oportunidades.add(new Oportunidade("Garçom","Casa das Massas"));
        Oportunidades.add(new Oportunidade("Garçom","Casa das Massas"));
        Oportunidades.add(new Oportunidade("Assougueiro","Casa de Carnes Jaco"));
        Oportunidades.add(new Oportunidade("Mecanico","Oficina do Joao"));
        Oportunidades.add(new Oportunidade("Mecanico","Oficina do Joao"));
        Oportunidades.add(new Oportunidade("Mecanico","Oficina do Joao"));
        Oportunidades.add(new Oportunidade("Mecanico","Oficina do Joao"));

        recyclerView.setAdapter(new OportunidadeAdapter(getContext(),Oportunidades,onClickOportunidade()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }
    private OportunidadeAdapter.OportunidadeOnClickListener onClickOportunidade(){
        return new OportunidadeAdapter.OportunidadeOnClickListener(){
            @Override
            public void onClickOportunidade(View view, int idx){
                //
                //
                //
            }
        };
    }

}