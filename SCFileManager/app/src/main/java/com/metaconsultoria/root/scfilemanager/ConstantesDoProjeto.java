package com.metaconsultoria.root.scfilemanager;

public class ConstantesDoProjeto{
    private static ConstantesDoProjeto ourInstance = new ConstantesDoProjeto();
    public static final int NEW_USER_REQUEST = 1;
    private String mainPath;
    private String mainPathProtected;
    private String mainPathProtectedCopy;
    private String BackUpPath;
    private boolean isSaving;
    private boolean isRestoring;
    private boolean isEditScreenShow;
    private EditScreen editScreen;
    public static ConstantesDoProjeto getInstance() {
        return ourInstance;
    }
    private ConstantesDoProjeto() {
        isSaving=false;
        isRestoring=false;
        isEditScreenShow=false;
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

    public boolean isSaving() {
        return isSaving;
    }

    public void setSaving(boolean saving) {
        isSaving = saving;
    }

    public boolean isRestoring() {
        return isRestoring;
    }

    public void setRestoring(boolean restoring) {
        isRestoring = restoring;
    }

    public boolean isEditScreenShow() {
        return isEditScreenShow;
    }

    public void setEditScreenShow(boolean editScreenShow) {
        isEditScreenShow = editScreenShow;
    }

    public String getBackUpPath() {
        return BackUpPath;
    }

    public void setBackUpPath(String backUpPath) {
        BackUpPath = backUpPath;
    }

    public EditScreen getEditScreen() {
        return editScreen;
    }

    public void setEditScreen(EditScreen editScreen) {
        this.editScreen = editScreen;
    }

    public String getMainPathProtectedCopy() {
        return mainPathProtectedCopy;
    }

    public void setMainPathProtectedCopy(String mainPathProtectedCopy) {
        this.mainPathProtectedCopy = mainPathProtectedCopy;
    }
}
