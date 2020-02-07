package com.metaconsultoria.root.scfilemanager;

public class ConstantesDoProjeto {
    private static ConstantesDoProjeto ourInstance = new ConstantesDoProjeto();
    private static boolean protect;
    public static final int NEW_USER_REQUEST = 1;
    public static ConstantesDoProjeto getInstance() {
        return ourInstance;
    }
    private ConstantesDoProjeto() {
            protect =false;
    }

    public static boolean isProtect() {
        return protect;
    }

    public static void setProtect(boolean isProtect) {
        ConstantesDoProjeto.protect = isProtect;
    }
}
