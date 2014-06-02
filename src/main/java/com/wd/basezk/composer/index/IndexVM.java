package com.wd.basezk.composer.index;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;

public class IndexVM {
    private String judul;

    @AfterCompose
    public void onCreate(@ContextParam(ContextType.VIEW) Component view) {

    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }
}
