package com.metaconsultoria.root.scfilemanager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
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
        myArquiveList=((MainNavigationDrawerActivity)getActivity()).db.mySelect(5);
        if(myArquiveList==null){ }
        else{
            ListAdapter m_listAdapter = null;
            ArrayList m_item = new ArrayList<String>();
            ArrayList m_filesPath = new ArrayList<String>();
            final ArrayList m_filesUri = new ArrayList<Uri>();

            for(MyArquive now_arquive: myArquiveList){
            m_item.add(now_arquive.getNome());
            Uri file3= Uri.parse(now_arquive.getPath());
            m_filesPath.add(file3.getPath());
            m_filesUri.add(file3);
            }
            m_listAdapter =new ListAdapter(this,m_item,m_filesPath,false);
            m_ArqList.setAdapter(m_listAdapter);
            final ArrayList teste = m_filesPath;
            m_ArqList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Uri file2 = (Uri)m_filesUri.get(position);

                        if (file2.toString().substring(file2.toString().lastIndexOf('.')).equalsIgnoreCase(".pdf")) {

                            Log.i("teste","teste");
                            FragmentFileEx.FragmentListener mListener = (FragmentFileEx.FragmentListener) getActivity();
                            MyArquive arq = new MyArquive(
                                    file2.getPath().substring(file2.getPath().lastIndexOf('/')+1,file2.getPath().lastIndexOf('.')),
                                    file2.toString()
                            );
                            mListener.setPdfActivity(arq);
                        }
                }
            });
        }
    }


}
