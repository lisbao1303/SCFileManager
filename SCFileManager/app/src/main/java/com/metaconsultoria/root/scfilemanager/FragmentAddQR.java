package com.metaconsultoria.root.scfilemanager;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Objects;


public class FragmentAddQR extends Fragment implements View.OnClickListener{

    private FragmentFileEx mfragment;
    private ImageView imageView;
    public FragmentAddQR() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vi=inflater.inflate(R.layout.fragment_fragment_add_qr, container, false);
        imageView =vi.findViewById(R.id.imageView_icon1);
        imageView.setOnClickListener(this);
        vi.findViewById(R.id.imageView_icon2).setOnClickListener(this);
        vi.findViewById(R.id.imageView_icon3).setOnClickListener(this);
        vi.findViewById(R.id.imageView_icon4).setOnClickListener(this);
        vi.findViewById(R.id.imageView_help).setOnClickListener(this);

        return vi;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if((new FuncDB(getContext())).getValor("is_protected").equals("true")){
            imageView.setAlpha(0.3f);
            imageView.setClickable(false);
        }
        if(savedInstanceState==null) {
            mfragment = new FragmentFileEx();
            Bundle arguments = new Bundle();
            if((new FuncDB(getContext())).getValor("is_protected").equals("true")) {
                arguments.putString("arqpath", ConstantesDoProjeto.getInstance().getMainPathProtected());
            }else{
                arguments.putString("arqpath", ConstantesDoProjeto.getInstance().getMainPath());
            }
            arguments.putString("text", null);
            arguments.putBoolean("isGenerator",true);
            mfragment.setArguments(arguments);
            Objects.requireNonNull(this.getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.replace_add_qr_fragment, mfragment).commit();
        }
        if(savedInstanceState!=null){
            mfragment=(FragmentFileEx) Objects.requireNonNull(this.getActivity()).getSupportFragmentManager().findFragmentById(R.id.replace_add_qr_fragment);
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.imageView_icon1){
            Bundle b=new Bundle();
            b.putString("arqpath",ConstantesDoProjeto.getInstance().getMainPath());
            mfragment.refresh(b);
        }else if(id==R.id.imageView_icon2){
            Bundle b=new Bundle();
            b.putString("arqpath",ConstantesDoProjeto.getInstance().getMainPathProtected());
            mfragment.refresh(b);
        }else if(id==R.id.imageView_icon3){
            mfragment.refresh();
        }else if(id==R.id.imageView_icon4){
            if(mfragment.isHideView()){
                ((ImageView)this.getActivity().findViewById(id)).setImageResource(R.drawable.ic_hidden);
                mfragment.setHideView(false);
            }else {
                ((ImageView)this.getActivity().findViewById(id)).setImageResource(R.drawable.ic_remove_red_eye_black_24dp);
                mfragment.setHideView(true);
            }mfragment.refresh();
        }else if(id==R.id.imageView_help){
            View view= getLayoutInflater().inflate(R.layout.layout_help, null);

            new AlertDialog.Builder(getContext())
                    .setTitle("Cuidado")
                    .setView(view)
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(R.drawable.ic_info_outline_black_24dp)
                    .show();
        }
    }
}
