package com.metaconsultoria.root.scfilemanager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Listedfiles {
    public ArrayList m_itemp = new ArrayList<String>();
    public ArrayList m_pathp = new ArrayList<String>();

    String getLastDate(int p_pos) {
        File m_file = new File((String) m_pathp.get(p_pos));
        SimpleDateFormat m_dateFormat = null;
        m_dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return m_dateFormat.format(m_file.lastModified());
    }
}
