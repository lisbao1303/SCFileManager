package com.metaconsultoria.root.scfilemanager;

public class ConstantesDoProjeto{
    private static ConstantesDoProjeto ourInstance = new ConstantesDoProjeto();
    public static final int NEW_USER_REQUEST = 1;
    private String mainPath;
    private String mainPathProtected;
    public static ConstantesDoProjeto getInstance() {
        return ourInstance;
    }
    private ConstantesDoProjeto() {
        mainPath= "/storage/E418-2511";
        mainPathProtected= "/storage/extSdCard/ArquivosSouza";
    }

    public String getMainPath() {
        return mainPath;
    }

    public void setMainPath(String mainPath) {
        this.mainPath = mainPath;
    }

    public String getMainPathProtected() {
        return mainPathProtected;
    }

    public void setMainPathProtected(String mainPathProtected) {
        this.mainPathProtected = mainPathProtected;
    }
}
