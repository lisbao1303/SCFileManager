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

public class StaredAdapter extends RecyclerView.Adapter<StaredAdapter.StaredViewHolder>{
    private final List<MyArquive> stareds;
    private final Context context;
    private final boolean inline;
    private StaredAdapter.StaredOnClickListener staredOnClickListener;

    public StaredAdapter(Context context,List<MyArquive> stareds, StaredAdapter.StaredOnClickListener staredOnClickListener,boolean inline){
        this.context=context;
        this.stareds=stareds;
        this.staredOnClickListener=staredOnClickListener;
        this.inline=inline;
    }
    @NonNull
    @Override
    public StaredAdapter.StaredViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if(inline){
            view= LayoutInflater.from(this.context).inflate(R.layout.stared_adapter_inline,viewGroup,false);
        }else {
            view = LayoutInflater.from(this.context).inflate(R.layout.stared_adapter, viewGroup, false);
        }
        return new StaredAdapter.StaredViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StaredAdapter.StaredViewHolder staredViewHolder, final int i) {
        MyArquive c = stareds.get(i);
        if(c!=null) {
            staredViewHolder.name.setText(c.getNome());
            staredViewHolder.data_hr.setText(c.getData());
            staredViewHolder.imagem.setImageResource(getImageFileTipeIcon(c));
            if(c.getStared()){staredViewHolder.star.setImageResource(R.drawable.ic_star_black_24dp);}
            else{staredViewHolder.star.setImageResource(R.drawable.ic_star_border_black_24dp);}

            if (staredOnClickListener != null) {
                staredViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(View v) {
                                                                     staredOnClickListener.onClickStared(v, i);
                                                                 }
                                                             }

                );
            }
        }
    }

    @Override
    public int getItemCount() {
        if(stareds==null){ return 0;}
        else{return this.stareds.size();}
    }

    public static class StaredViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView data_hr;
        public ImageView imagem;
        public ImageView star;
        public StaredViewHolder(@NonNull View itemView) {
            super(itemView);
            name =(TextView) itemView.findViewById(R.id.card_stared_nome);
            data_hr =(TextView) itemView.findViewById(R.id.card_stared_data);
            imagem =(ImageView) itemView.findViewById(R.id.card_stared_imageView);
            star =(ImageView) itemView.findViewById(R.id.card_stared_button);
        }
    }

    public interface StaredOnClickListener{
        public void onClickStared(View view,int idx);
    }

    private int getImageFileTipeIcon (MyArquive arq){
        int lastpoint= arq.getPath().lastIndexOf('.');
        if (arq.getPath().substring(lastpoint).equalsIgnoreCase(".pdf")) {
            return R.mipmap.file_pdf_ic_hd;
        }
        return 0;
    }
}
