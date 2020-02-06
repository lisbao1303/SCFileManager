package com.metaconsultoria.root.scfilemanager;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private final Listedfiles list;
    private final Context m_context;
    private ListOnClickListener listOnClickListener;
    private final boolean isGenerator;

    public ListAdapter(Context p_context, Listedfiles list, ListOnClickListener listOnClickListener, @NonNull boolean isGenerator) {
        this.m_context = p_context;
        this.list = list;
        this.listOnClickListener=listOnClickListener;
        this.isGenerator=isGenerator;
    }

    @Override
    public ListViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(m_context).inflate(R.layout.list_row,viewGroup,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder( final ListViewHolder listViewHolder,final int i) {
        listViewHolder.m_tvFileName.setText((String) list.m_itemp.get(i));
        listViewHolder.m_ivIcon.setImageResource(setFileImageType(new File( (String) list.m_pathp.get(i))));
        listViewHolder.m_tvDate.setText(list.getLastDate(i));
        if(isGenerator) {
            listViewHolder.imageViewGenerate.setVisibility(ImageView.VISIBLE);
            listViewHolder.imageViewGenerate.setClickable(true);
            listViewHolder.imageViewGenerate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listOnClickListener.onAddQRClick(v,i);
                }
            });
        }
        if(listOnClickListener!=null){
            listViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               listOnClickListener.onClickList(v,i);
                                                           }
                                                       }

            );
        }
    }

    private int setFileImageType(File m_file) {
        int m_lastIndex = m_file.getAbsolutePath().lastIndexOf(".");
        String m_filepath = m_file.getAbsolutePath();
        if (m_file.isDirectory())
            return R.mipmap.file_folder_ic_hd;
        else {
            if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".pdf")) {
                return R.mipmap.file_pdf_ic_hd;
            } else {
                return R.mipmap.file_ic_hd;
            }
        }
    }

    @Override
    public int getItemCount() {
        if(list==null){ return 0;}
        else{return this.list.m_itemp.size();}
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        public ImageView m_ivIcon;
        public TextView m_tvFileName;
        public TextView m_tvDate;
        public ImageView imageViewGenerate;
        public ListViewHolder( View view) {
            super(view);
            m_tvFileName = (TextView) view.findViewById(R.id.func_nome);
            m_tvDate = (TextView) view.findViewById(R.id.func_matricula);
            m_ivIcon = (ImageView) view.findViewById(R.id.lr_ivFileIcon);
            imageViewGenerate = (ImageView) view.findViewById(R.id.imageView_generateQr);
        }
    }

    public interface ListOnClickListener{
        public void onClickList(View view,int idx);
        public void onAddQRClick(View view,int idx);
    }

}
