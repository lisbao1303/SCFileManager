package com.metaconsultoria.root.scfilemanager;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RecentFilesFragment extends Fragment {
    private View mView;
    private ListView m_ArqList;
    private List<MyArquive> myArquiveList;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fraglayoutex,null);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mView=view;
        m_ArqList = mView.findViewById(R.id.rl_lvListRoot);
        getListFromDB();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getListFromDB(){
        ListAdapter m_listAdapter = null;
        myArquiveList=((MainNavigationDrawerActivity)getActivity()).db.mySelect(5);
        ArrayList m_item = new ArrayList<String>();
        ArrayList m_filesPath = new ArrayList<String>();
        for(MyArquive now_arquive: myArquiveList){
            m_item.add(now_arquive.getNome());
            m_filesPath.add(now_arquive.getPath());
        }
        m_listAdapter =new ListAdapter(this,m_item,m_filesPath,false);
        m_ArqList.setAdapter(m_listAdapter);
    }


    public void refresh(Bundle data){
        String mPath;
        mPath = data.getString("arqpath");
    }
}
