package com.wd.basezk.composer.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;

import com.wd.basezk.model.Cmenu;
import com.wd.basezk.service.CmenuService;

/**
 * @author (ariv.wd@gmail.com)
 * <created-on-20140328-stabilized-vm>
 *
 */

/*Anotasi supaya STS bisa mendeteksi VM ini sebagai sebuah Bean*/
@Controller
/*Anotasi supaya semua Spring-Bean bisa dideteksi oleh ZK*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)

public class ListMenuVM {
    // Default Variables untuk VM-Component
    @Wire("#treeNya")
    private Tree treeNya;

    // Default Variables untuk VM-Model
    //--------------------------> [TidakAda]

    // Untuk WireComponentSelector
    private Component wComSel;

    // Untuk Wire Service Variables (butuh: Setter Getter)
    @WireVariable
    private CmenuService cmenuService;

    // Untuk Inisiate Variable yang digunakan di ZUL (butuh: Setter Getter)
    private List<Cmenu> allMenus;

    // Untuk Wiring Renderer (butuh: Setter Getter)
    //--------------------------> [TidakAda]

/*************************************************************************************
 * Initialize
 **************************************************************************************/
    @AfterCompose
    public void onCreate(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        setwComSel(view);
        initComponent();
        loadData();
    }

/*************************************************************************************
 * Preparation (Load Variables Value)
 **************************************************************************************/


/*************************************************************************************
 * Do's (Berisi kumpulan Command yang dipanggil dari ZUL, diawali dengan kata "do")
 **************************************************************************************/
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Command
    public void doNew() throws InterruptedException {

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Command
    public void doEdit(@BindingParam("record") Cmenu objNya) throws InterruptedException {

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
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
        loadData();
        Events.postEvent(Events.ON_SELECT, treeNya, null);
    }

/*************************************************************************************
 * Event dan Listener (Diawali dengan "on" / Fungsinya sama dengan Do's, yaitu Command)
 **************************************************************************************/


/*************************************************************************************
 * Custom Methods (Untuk method-method private)
 **************************************************************************************/
    private void executeDetail(Cmenu cmenuNya) throws InterruptedException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("objListCtrl", this);
        map.put("selected", cmenuNya);

        try {
            Executions.createComponents("/frontend/core/menu/vFormMenu.zul", null, map);
        } catch (Exception e) {
            //LOGGING
        }
    }

    private void loadData() {
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("null", "null");
        allMenus = getCmenuService().getByRequest(requestMap, false, null);

        BindUtils.postNotifyChange(null, null, this, "allMenus");
    }

    private void deletingData(final Map<String, Cmenu> objsToDel) {
        for (Map.Entry<String, Cmenu> mapNya : objsToDel.entrySet()) {
            try {
                Cmenu v = mapNya.getValue();
                getCmenuService().deleteData(v.getCmenuId());
            } catch (Exception e) {
                //
            }
        }
        doRefresh();
    }

/*************************************************************************************
 * Validator
 **************************************************************************************/


/*************************************************************************************
 * Renderer
 **************************************************************************************/
    private void initComponent() {

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

    public List<Cmenu> getAllMenus() {
        return allMenus;
    }
    public void setAllMenus(List<Cmenu> allMenus) {
        this.allMenus = allMenus;
    }

    public CmenuService getCmenuService() {
        return cmenuService;
    }
    public void setCmenuService(CmenuService cmenuService) {
        this.cmenuService = cmenuService;
    }

    public void getDeletingData(final Map<String, Cmenu> objsToDel) {
        this.deletingData(objsToDel);
    }

}
