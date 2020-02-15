package com.metaconsultoria.root.scfilemanager;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class FragmentAddQR extends Fragment implements View.OnClickListener{

    private FragmentFileEx mfragment;

    public FragmentAddQR() {
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
        return inflater.inflate(R.layout.fragment_fragment_add_qr, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageView imageView =this.getActivity().findViewById(R.id.imageView_icon1);
        imageView.setOnClickListener(this);
        this.getActivity().findViewById(R.id.imageView_icon2).setOnClickListener(this);
        this.getActivity().findViewById(R.id.imageView_icon3).setOnClickListener(this);
        this.getActivity().findViewById(R.id.imageView_icon4).setOnClickListener(this);
        this.getActivity().findViewById(R.id.imageView_help).setOnClickListener(this);

        if(ConstantesDoProjeto.getInstance().isProtect()){
            imageView.setAlpha(0.3f);
            imageView.setClickable(false);
        }
        if(savedInstanceState==null) {
            mfragment = new FragmentFileEx();
            Bundle arguments = new Bundle();
            if(ConstantesDoProjeto.getInstance().isProtect()) {
                arguments.putString("arqpath", ConstantesDoProjeto.getInstance().getMainPathProtected());
            }else{
                arguments.putString("arqpath", ConstantesDoProjeto.getInstance().getMainPath());
            }
            arguments.putString("text", null);
            arguments.putBoolean("isGenerator",true);
            mfragment.setArguments(arguments);

            this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.replace_add_qr_fragment, mfragment).commit();
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

        }else if(id==R.id.imageView_help){
            Snackbar.make(getView(),"teste",Snackbar.LENGTH_LONG).show();
        }
    }
}
