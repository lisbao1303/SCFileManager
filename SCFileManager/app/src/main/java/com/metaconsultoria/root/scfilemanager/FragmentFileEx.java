package com.metaconsultoria.root.scfilemanager;

import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentFileEx extends Fragment {
    private ListView m_RootList;
    private String m_root =null;
    private ArrayList m_itemp = new ArrayList<String>();
    private ArrayList m_pathp = new ArrayList<String>();
    ArrayList m_filesp = new ArrayList<String>();
    ArrayList m_filesPathp = new ArrayList<String>();
    public String ultimodir = null;
    public Uri file;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fraglayoutex,null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_RootList = view.findViewById(R.id.rl_lvListRoot);
        Bundle bundle = getArguments();
        m_root = bundle.getString("arqpath");
        ultimodir = m_root;
        getDirFromRoot(m_root,null);
    }
    public void NewSearch(String text){
        m_itemp = new ArrayList<String>();
        m_pathp = new ArrayList<String>();
        m_filesp = new ArrayList<String>();
        m_filesPathp = new ArrayList<String>();
        if(text!=null && text.length()!=0) {
            getDirFromRoot(ultimodir, text);
        }else{
            getDirFromRoot(ultimodir,null);
        }
    }
    public class ListAdapter extends BaseAdapter {
        private List<String> m_item;
        private List<String> m_path;
        public ArrayList<Integer> m_selectedItem;
        FragmentFileEx m_context;
        Boolean m_isRoot;

        public ListAdapter(FragmentFileEx p_context, List<String> p_item, List<String> p_path, Boolean p_isRoot) {
            m_context = p_context;
            m_item = p_item;
            m_path = p_path;
            m_selectedItem = new ArrayList<>();
            m_isRoot = p_isRoot;
        }

        @Override
        public int getCount() {
            return m_item.size();
        }

        @Override
        public Object getItem(int position) {
            return m_item.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int p_position, View p_convertView, ViewGroup p_parent) {
            View m_view = null;
            ViewHolder m_viewHolder = null;
            if (p_convertView == null) {
                LayoutInflater m_inflater = LayoutInflater.from(m_context.getContext());
                m_view = m_inflater.inflate(R.layout.list_row, null);
                m_viewHolder = new ViewHolder();
                m_viewHolder.m_tvFileName = (TextView) m_view.findViewById(R.id.lr_tvFileName);
                m_viewHolder.m_tvDate = (TextView) m_view.findViewById(R.id.lr_tvdate);
                m_viewHolder.m_ivIcon = (ImageView) m_view.findViewById(R.id.lr_ivFileIcon);
                m_view.setTag(m_viewHolder);
            } else {
                m_view = p_convertView;
                m_viewHolder = ((ViewHolder) m_view.getTag());
            }
            m_viewHolder.m_tvFileName.setText(m_item.get(p_position));
            m_viewHolder.m_ivIcon.setImageResource(setFileImageType(new File(m_path.get(p_position))));
            m_viewHolder.m_tvDate.setText(getLastDate(p_position));
            return m_view;
        }
        class ViewHolder {
            ImageView m_ivIcon;
            TextView m_tvFileName;
            TextView m_tvDate;
        }
        private int setFileImageType(File m_file) {
            int m_lastIndex = m_file.getAbsolutePath().lastIndexOf(".");
            String m_filepath = m_file.getAbsolutePath();
            if (m_file.isDirectory())
                return R.drawable.ic_paste;
            else {
                if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".pdf")) {
                    return R.drawable.ic_pdf;
                } else {
                    return R.drawable.ic_file;
                }
            }
        }
        String getLastDate(int p_pos) {
            File m_file = new File(m_path.get(p_pos));
            SimpleDateFormat m_dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return m_dateFormat.format(m_file.lastModified());
        }
    }
    //// Obtendo os arquivos da memória
    public void getDirFromRoot(String p_rootPath,String procura) {
        ArrayList m_item = new ArrayList<String>();
        Boolean m_isRoot = true;
        final ArrayList m_path = new ArrayList<String>();
        ArrayList m_files = new ArrayList<String>();
        ArrayList m_filesPath = new ArrayList<String>();
        File m_file = new File(p_rootPath);
        ListAdapter m_listAdapter = null;
        if (procura == null || procura == " ") {
          if (m_file.isDirectory()) {
            File[] m_filesArray = m_file.listFiles();

                if (!p_rootPath.equals(m_root)) {
                    m_item.add("../");
                    m_path.add(m_file.getParent());
                    m_isRoot = false;
                }
                String m_curDir = p_rootPath;
                //sorting file list in alphabetical order
                Arrays.sort(m_filesArray);
                for (int i = 0; i < m_filesArray.length; i++) {
                    File file = m_filesArray[i];
                    if (file.isDirectory()) {
                        m_item.add(file.getName());
                        m_path.add(file.getPath());
                    } else {
                        m_files.add(file.getName());
                        m_filesPath.add(file.getPath());
                    }
                }

                for (Object m_AddFile : m_files) {
                    m_item.add(m_AddFile);
                }
                for (Object m_AddPath : m_filesPath) {
                    m_path.add(m_AddPath);
                }
                m_listAdapter = new ListAdapter(this, m_item, m_path, m_isRoot);

            } else {
                m_files.add(m_file.getName());
                m_filesPath.add(m_file.getPath());
                for (Object m_AddFile : m_files) {
                    m_item.add(m_AddFile);
                }
                for (Object m_AddPath : m_filesPath) {
                    m_path.add(m_AddPath);
                }
                m_isRoot = false;
                m_listAdapter = new ListAdapter(this, m_item, m_path, m_isRoot);
            }
            m_RootList.setAdapter(m_listAdapter);
            m_RootList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    File m_isFile = new File(m_path.get(position).toString());
                    int m_ultimoponto = m_isFile.getAbsolutePath().lastIndexOf(".");
                    String m_caminhofile = m_isFile.getAbsolutePath();
                    file = Uri.fromFile(m_isFile);
                    if (m_isFile.isDirectory()) {
                        ultimodir= m_isFile.toString();
                        getDirFromRoot(m_isFile.toString(), null);
                    } else {
                        if (m_caminhofile.substring(m_ultimoponto).equalsIgnoreCase(".pdf")) {
                            FragmentListener mListener = (FragmentListener) getActivity();
                            mListener.metodo();
                        }
                    }
                }
            });
          }else{
            File[] m_filesArray = m_file.listFiles();
            Arrays.sort(m_filesArray);
            for (int i = 0; i < m_filesArray.length; i++) {
                File file = m_filesArray[i];
                final boolean contains = file.getName().toLowerCase().contains(procura.toLowerCase());
                if (file.isDirectory()) {
                    if(contains){
                    m_itemp.add(file.getName());
                    m_pathp.add(file.getPath());
                    }
                    getDirFromRootSEC(file.toString(), procura);
                } else {
                     if(contains) {
                    m_filesp.add(file.getName());
                    m_filesPathp.add(file.getPath());
                    }
                }
            }
                for (Object m_AddFile : m_filesp) {
                    m_itemp.add(m_AddFile);
                }
                for (Object m_AddPath : m_filesPathp) {
                    m_pathp.add(m_AddPath);
                }

            if(m_itemp.toArray().length>0) {
                m_listAdapter = new ListAdapter(this, m_itemp, m_pathp, m_isRoot);
            }else{
                Toast.makeText(getContext().getApplicationContext(),"Arquivo não encontrado",Toast.LENGTH_LONG).show();
            }
            m_RootList.setAdapter(m_listAdapter);
            m_RootList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    File m_isFile = new File(m_pathp.get(position).toString());
                    int m_ultimoponto = m_isFile.getAbsolutePath().lastIndexOf(".");
                    String m_caminhofile = m_isFile.getAbsolutePath();
                    file = Uri.fromFile(m_isFile);
                    if (m_isFile.isDirectory()) {
                        ultimodir =m_isFile.toString();
                        getDirFromRoot(m_isFile.toString(), null);
                    } else {
                        if (m_caminhofile.substring(m_ultimoponto).equalsIgnoreCase(".pdf")) {
                            FragmentListener mListener = (FragmentListener) getActivity();
                            mListener.metodo();
                        }
                    }
                }
            });
        }
    }
    public void getDirFromRootSEC (String R2Path, String procura){
        File m_file = new File(R2Path);
        File[] m_filesArray = m_file.listFiles();
        Arrays.sort(m_filesArray);
        for (int i = 0; i < m_filesArray.length; i++) {
            File file = m_filesArray[i];
            final boolean contains = file.getName().toLowerCase().contains(procura.toLowerCase());
            if (file.isDirectory()) {
                if(contains){
                    m_itemp.add(file.getName());
                    m_pathp.add(file.getPath());
                }
                getDirFromRootSEC(file.toString(), procura);
            } else {
                if(contains) {
                    m_filesp.add(file.getName());
                    m_filesPathp.add(file.getPath());
                }
            }
        }
    }
    public interface FragmentListener {
        public void metodo();
    }


}
