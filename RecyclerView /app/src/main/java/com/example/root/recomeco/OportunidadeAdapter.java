package com.example.root.recomeco;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class OportunidadeAdapter extends RecyclerView.Adapter<OportunidadeAdapter.OportunidadeViewHolder> {
    private final List<Oportunidade> oportunidades;
    private final Context context;
    private final OportunidadeOnClickListener onClickListener;
    public interface OportunidadeOnClickListener{
        public void onClickOportunidade(View view , int idx);
    }
    public OportunidadeAdapter(Context context, List<Oportunidade> oportunidades, OportunidadeOnClickListener onClickListener) {
        this.context = context;
        this.onClickListener=onClickListener;
        this.oportunidades=oportunidades;
    }
    @Override
    public OportunidadeViewHolder onCreateViewHolder(ViewGroup viewGroup , int viewType){
          View view = LayoutInflater.from(context).inflate(R.layout.oportunity_views,viewGroup,false);

          OportunidadeViewHolder holder =new OportunidadeViewHolder(view);
          return holder;
    }

    public void onBindViewHolder(final OportunidadeViewHolder holder, final int position){
        Oportunidade x= oportunidades.get(position);
        holder.nome.setText(x.getTitulo());
        holder.proprietario.setText(x.getOfertante());

        if(onClickListener!= null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    onClickListener.onClickOportunidade(holder.view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount(){
        return (this.oportunidades==null ? 0 : this.oportunidades.size());
    }
    public static class OportunidadeViewHolder extends RecyclerView.ViewHolder{
        private View view;
        public TextView nome;
        TextView proprietario;
        public OportunidadeViewHolder(View view){
            super(view);
            this.view=view;

            nome= view.findViewById(R.id.nome_location);
            proprietario= view.findViewById(R.id.proprietario_location);
        }
    }
}
