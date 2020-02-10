package com.metaconsultoria.root.scfilemanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class ComentAdapter extends RecyclerView.Adapter<ComentAdapter.ComentViewHolder> {
    private List<MyComent> coments;
    private final Context context;
    private ComentOnClickListener comentOnClickListener;

    public ComentAdapter(Context context,List<MyComent> coments, ComentOnClickListener comentOnClickListener){
            this.context=context;
            this.coments=coments;
            this.comentOnClickListener=comentOnClickListener;
    }
    @NonNull
    @Override
    public ComentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.coment_adapter,viewGroup,false);
        return new ComentViewHolder(view);
    }

    public void refreshComentList(List<MyComent> coments){
        this.coments=coments;
    }

    @Override
    public void onBindViewHolder(@NonNull final ComentViewHolder comentViewHolder,int i) {
        MyComent c = coments.get(i);

        comentViewHolder.name.setText(c.getName());
        comentViewHolder.coment.setText(c.getComent());
        comentViewHolder.data_hr.setText(c.getData_hr());

        if(comentOnClickListener!=null){
            comentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comentOnClickListener.onClickComent(v,comentViewHolder.getAdapterPosition());
                }
            });
            comentViewHolder.buttonDell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comentOnClickListener.onClickDellComent(v, comentViewHolder.getAdapterPosition());
                }
            });
            comentViewHolder.buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comentOnClickListener.onClickEditComent(v, comentViewHolder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(coments==null){ return 0;}
        else{return this.coments.size();}
    }

    public static class ComentViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView coment;
        public TextView data_hr;
        public ImageButton buttonDell;
        public ImageButton buttonEdit;
        public ComentViewHolder(@NonNull View itemView) {
            super(itemView);
            name =(TextView) itemView.findViewById(R.id.name_location_coment);
            coment =(TextView) itemView.findViewById(R.id.coment_location_coment);
            data_hr =(TextView) itemView.findViewById(R.id.date_hr_location_coment);
            buttonDell =(ImageButton) itemView.findViewById(R.id.imageButtonDell);
            buttonEdit =(ImageButton) itemView.findViewById(R.id.imageButtonEdit);
        }
    }

    public interface ComentOnClickListener{
        public void onClickComent(View view,int idx);
        public void onClickDellComent(View view,int idx);
        public void onClickEditComent(View view,int idx);
    }
}
