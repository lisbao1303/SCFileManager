package com.metaconsultoria.root.scfilemanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FuncAdapter extends RecyclerView.Adapter<FuncAdapter.FuncViewHolder> {
    private Context context;
    private List<Funcionario> funcs;
    private FuncAdapter.FuncOnClickListener funcListener;

    public FuncAdapter(Context context, List<Funcionario> funcs, FuncAdapter.FuncOnClickListener funcListener){
        this.context=context;
        this.funcs=funcs;
        this.funcListener=funcListener;
    }

    @Override
    public int getItemCount() {
        if(funcs==null){ return 0;}
        else{return this.funcs.size();}
    }

    @Override
    public void onBindViewHolder(@NonNull final FuncAdapter.FuncViewHolder staredViewHolder, final int i) {
        Funcionario c = funcs.get(i);
        if(c!=null) {
            staredViewHolder.nome.setText(context.getText(R.string.nome_const)+ c.getNome());
            staredViewHolder.matricula.setText(context.getText(R.string.matricula_const)+c.getMatricula());

            if (funcListener != null) {
                staredViewHolder.removeView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        funcListener.onClickRemove(v, i);
                    }
                });
                staredViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(View v) {
                                                                     funcListener.onClickFunc(v, i);
                                                                 }
                                                             }

                );
            }
        }
    }

    @NonNull
    @Override
    public FuncAdapter.FuncViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(this.context).inflate(R.layout.func_adapter, viewGroup, false);
        return new FuncAdapter.FuncViewHolder(view);
    }

    public static class FuncViewHolder extends RecyclerView.ViewHolder{
        public TextView nome;
        public TextView matricula;
        public ImageView removeView;
        public FuncViewHolder(@NonNull View itemView) {
            super(itemView);
            nome =(TextView) itemView.findViewById(R.id.func_nome);
            matricula =(TextView) itemView.findViewById(R.id.func_matricula);
            removeView=(ImageView) itemView.findViewById(R.id.imageView_remove);
         }
    }

    public interface FuncOnClickListener{
        public void onClickFunc(View v,int id);
        public void onClickRemove(View v, int id);
    }
}
