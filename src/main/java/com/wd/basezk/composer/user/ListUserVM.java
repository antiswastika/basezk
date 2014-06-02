package com.wd.basezk.composer.user;

import org.springframework.stereotype.Controller;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

import com.wd.basezk.model.Cuser;

/**
 * @author (ariv.wd@gmail.com)
 * <created-on-20140328-stabilized-vm>
 *
 */

/*Anotasi supaya STS bisa mendeteksi VM ini sebagai sebuah Bean*/
@Controller
/*Anotasi supaya semua Spring-Bean bisa dideteksi oleh ZK*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)

public class ListUserVM {
    // Default Variables untuk VM-Component
    //--------------------------> [TidakAda]

    // Default Variables untuk VM-Model
    //--------------------------> [TidakAda]

    // Untuk WireComponentSelector
    private Component wComSel;

    // Untuk Wire Service Variables (butuh: Setter Getter)
    //--------------------------> [TidakAda]

    // Untuk Inisiate Variable yang digunakan di ZUL (butuh: Setter Getter)
    //--------------------------> [TidakAda]

    // Untuk Wiring Renderer (butuh: Setter Getter)
    //--------------------------> [TidakAda]

    @AfterCompose
    public void onCreate(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        setwComSel(view);
        wiringComponent();
    }

/*************************************************************************************
 * Do's (Berisi kumpulan Command yang dipanggil dari ZUL, diawali dengan kata "do")
 **************************************************************************************/
    @Command
    public void doNew() throws InterruptedException {
        this.executeDetail( new Cuser() );
    }

    @Command
    public void doEdit() {

    }

    @Command
    public void doDelete() {

    }

    @Command
    public void doSearch() {

    }

    @Command
    public void doPrintPreview() {

    }

    @Command
    public void doRefresh() {

    }

/*************************************************************************************
 * Event dan Listener (Diawali dengan "on" / Fungsinya sama dengan Do's, yaitu Command)
 **************************************************************************************/


/*************************************************************************************
 * Custom Methods (Untuk method-method private)
 **************************************************************************************/
    public void executeDetail(Cuser cuserNya) throws InterruptedException {
        try {
            Executions.createComponents("/frontend/core/user/vFormUser.zul", null, null);
        } catch (Exception e) {
            //LOGGING
        }
    }

/*************************************************************************************
 * Renderer
 **************************************************************************************/
    // Untuk Wiring Component yang ada di ZUL apabila diperlukan,
    // Persiapan Wiring Component via wComSel - (DEFAULT)
    private void wiringComponent() {

    }

/*************************************************************************************
 * Set dan Get
 **************************************************************************************/
    public Component getwComSel() {
        return wComSel;
    }
    public void setwComSel(Component wComSel) {
        this.wComSel = wComSel;
    }

}
