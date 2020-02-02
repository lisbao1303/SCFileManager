package com.metaconsultoria.root.scfilemanager;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;


public class FragmentFileEx extends Fragment  {
    private RecyclerView m_RootList;
    private String m_root =null;
    private String ultimodir;
    private Listedfiles clickablelist;
    private Uri file;
    private View mView;
    private ListAdapter m_listAdapter = null;
    private StorageAccess[] task = new StorageAccess[100];
    private ProgressBar progress;
    private String procura = null;
    int teste = 1;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mView= inflater.inflate(R.layout.fraglayoutex, container,false);
        m_RootList = mView.findViewById(R.id.rl_lvListRoot);
        m_RootList.setLayoutManager(new LinearLayoutManager(getActivity()));
        m_RootList.setItemAnimator(new DefaultItemAnimator());
        progress = (ProgressBar) mView.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        Bundle bundle = getArguments();
        m_root = bundle.getString("arqpath");
        if(ultimodir==null) {
            ultimodir = m_root;
        }
        getDirFromRoot(ultimodir,null);
        return mView;
    }

    public void refresh(Bundle data){
        String mPath;
        mPath = data.getString("arqpath");
        ultimodir = mPath;
        getDirFromRoot(mPath,null);
    }

    public void NewSearch(String text){
        if(text!=null && text.length()!=0) {
            procura = text;
            getDirFromRoot(ultimodir,procura);
        }else{
            procura = null;
            getDirFromRoot(ultimodir,null);
        }
    }
    public void Canceltask(int i){
        if(task[i] != null) {
            task[i].cancel(true);
        }
    }
    //// Obtendo os arquivos da memória
    private void getDirFromRoot(String p_rootPath, String search) {
        m_listAdapter = null;
        m_RootList.setAdapter(m_listAdapter);
        Canceltask(teste-1);
        task[teste] = new StorageAccess();
        task[teste].execute(p_rootPath, search);
        if(teste==99){
            teste = 1;
        }
        teste++;
    }
    private class StorageAccess extends AsyncTask<String,Void,Listedfiles>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }
        @Override
        protected Listedfiles doInBackground(String... strings) {
            File m_file = new File(strings[0]);
            Listedfiles listfiles = new Listedfiles();
            ArrayList m_filesp = new ArrayList<String>();
            ArrayList m_filesPathp = new ArrayList<String>();
            if (strings[1] == null) {
                if (isCancelled()){
                    return null;
                }
                if (m_file.isDirectory()) {
                    File[] m_filesArray = m_file.listFiles();
                    if (!ultimodir.equals(m_root)) {
                        listfiles.m_itemp.add("../");
                        listfiles.m_pathp.add(m_file.getParent());
                    }
                    Arrays.sort(m_filesArray);
                    for (int i = 0; i < m_filesArray.length; i++) {
                        File file = m_filesArray[i];
                        if (file.isDirectory()) {
                            listfiles.m_itemp.add(file.getName());
                            listfiles.m_pathp.add(file.getPath());
                        } else {
                            m_filesp.add(file.getName());
                            m_filesPathp.add(file.getPath());
                        }
                    }
                    for (Object m_AddFile : m_filesp) {
                        listfiles.m_itemp.add(m_AddFile);
                    }
                    for (Object m_AddPath : m_filesPathp) {
                        listfiles.m_pathp.add(m_AddPath);
                    }
                } else {
                    m_filesp.add(m_file.getName());
                    m_filesPathp.add(m_file.getPath());
                    for (Object m_AddFile : m_filesp) {
                        listfiles.m_itemp.add(m_AddFile);
                    }
                    for (Object m_AddPath : m_filesPathp) {
                        listfiles.m_pathp.add(m_AddPath);
                    }
                }
            }else{
                if (isCancelled()){
                    return null;
                }
               Collection m_filesArray = FileUtils.listFilesAndDirs(m_file, TrueFileFilter.INSTANCE,  TrueFileFilter.INSTANCE);
                if (isCancelled()){
                    return null;
                }
               for (Iterator iterator = m_filesArray.iterator(); iterator.hasNext();) {
                    File file = (File) iterator.next();
                   if (isCancelled()){
                       return null;
                   }
                    final boolean contains = file.getName().toLowerCase().contains(strings[1].toLowerCase());

                    if (file.isDirectory()) {
                        if(contains){
                            listfiles.m_itemp.add(file.getName());
                            listfiles.m_pathp.add(file.getPath());
                        }
                    }else{
                        if(contains) {
                            m_filesp.add(file.getName());
                            m_filesPathp.add(file.getPath());
                        }
                    }
                }
                for (Object m_AddFile : m_filesp) {
                    listfiles.m_itemp.add(m_AddFile);
                    if (isCancelled()){
                        return null;
                    }
                }
                for (Object m_AddPath : m_filesPathp) {
                    listfiles.m_pathp.add(m_AddPath);
                    if (isCancelled()){
                        return null;
                    }
                }
            }
            return listfiles;
        }

        @Override
        protected void onCancelled() {
            //task = null;
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Listedfiles filesarray) {
            //task = null;
            Toast.makeText(getContext().getApplicationContext(),"teste "+teste,Toast.LENGTH_SHORT).show();
            changelistview(filesarray);
        }
    }

    private void changelistview(Listedfiles list){
        if(list.m_itemp.toArray().length>0) {
            m_listAdapter = new ListAdapter(getActivity(), list,onClickitem());
        }else{
            Toast.makeText(getContext().getApplicationContext(),"Arquivo não encontrado",Toast.LENGTH_LONG).show();
        }
        m_RootList.setAdapter(m_listAdapter);
        clickablelist = list;
        progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        progress.setVisibility(View.INVISIBLE);
    }

    private ListAdapter.ListOnClickListener onClickitem(){
        return new ListAdapter.ListOnClickListener(){

            @Override
            public void onClickitem(View view, int idx) {
                File m_isFile = new File(clickablelist.m_pathp.get(idx).toString());
                int m_ultimoponto = m_isFile.getAbsolutePath().lastIndexOf(".");
                String m_caminhofile = m_isFile.getAbsolutePath();
                file = Uri.fromFile(m_isFile);
                if (m_isFile.isDirectory()) {
                    ultimodir= m_isFile.toString();
                    getDirFromRoot(m_isFile.toString(),null);
                } else {
                    if (m_caminhofile.substring(m_ultimoponto).equalsIgnoreCase(".pdf")) {
                        FragmentListener mListener = (FragmentListener) getActivity();
                        MyArquive arq = new MyArquive(
                                file.getPath().substring(file.getPath().lastIndexOf('/')+1,file.getPath().lastIndexOf('.')),
                                file.toString()
                        );
                        mListener.setPdfActivity(arq);
                    }
                }

            }
        };
    }
    public boolean upDir(){
        if(ultimodir==null || ultimodir.equals(m_root)){return true;}
        else{
            String path = ultimodir.substring(0,ultimodir.lastIndexOf('/'));
            Log.i("teste de path",path);
            Bundle arguments = new Bundle();
            arguments.putString("arqpath", path);
            arguments.putString("text",null);
            this.refresh(arguments);
            return false;
        }
    }

    public interface FragmentListener {
        void setPdfActivity(MyArquive arq);
        void Scanner(String qR);
    }
}
