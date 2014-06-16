package com.wd.basezk.composer.role;

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

import com.wd.basezk.model.Crole;
import com.wd.basezk.service.CroleService;

/**
 * @author (ariv.wd@gmail.com)
 * <created-on-20140328-stabilized-vm>
 *
 */

/*Anotasi supaya STS bisa mendeteksi VM ini sebagai sebuah Bean*/
@Controller
/*Anotasi supaya semua Spring-Bean bisa dideteksi oleh ZK*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)

public class ListRoleVM {
    // Default Variables untuk VM-Component
    @Wire("#listboxNya")
    private Listbox listboxNya;

    // Default Variables untuk VM-Model
    //--------------------------> [TidakAda]

    // Untuk WireComponentSelector
    private Component wComSel;

    // Untuk Wire Service Variables (butuh: Setter Getter)
    @WireVariable
    private CroleService croleService;

    // Untuk Inisiate Variable yang digunakan di ZUL (butuh: Setter Getter)
    private List<Crole> allRoles;

    // Untuk Wiring Renderer (butuh: Setter Getter)
    //--------------------------> [TidakAda]

/*************************************************************************************
 * Initialize
 **************************************************************************************/
    @AfterCompose
    public void onCreate(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        setwComSel(view);
        wiringComponent();
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
        ListModelList<Crole> lml = (ListModelList) listboxNya.getModel();
        lml.clearSelection();
        this.executeDetail( new Crole() );
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Command
    public void doEdit(@BindingParam("record") Crole objNya) throws InterruptedException {
        ListModelList<Crole> lml = (ListModelList) listboxNya.getModel();
        lml.clearSelection();
        for (int i=0; i<lml.size(); i++) {
            if (lml.get(i).equals(objNya)) {
                lml.addToSelection(lml.get(i));
                break;
            }
        }
        this.executeDetail( objNya );
        listboxNya.invalidate();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Command
    public void doDelete() {
        ListModelList<Crole> lml = (ListModelList) listboxNya.getModel();
        final Map<String, Crole> objsToDel = new HashMap<String, Crole>();

        for (Crole objs : lml) {
            if (lml.isSelected(objs)) {
                objsToDel.put(objs.getCroleId(), objs);
            }
        }

        if (objsToDel.size()>0) {
            // ----------------------------------------------------------
            // Show a confirm box
            // ----------------------------------------------------------
            //TODO: Labeling!
            Messagebox.show("XXXXXXXXXXXX", "Confirmation", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    if(((Integer)event.getData()).intValue()==Messagebox.YES){
                        deletingData(objsToDel);
                    }
                }
            });
            // ----------------------------------------------------------

        } else {
            // ----------------------------------------------------------
            // Show a confirm box
            // ----------------------------------------------------------
            //TODO: Labeling!
            Messagebox.show("XXXXXXXXXXXX", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
            return;
            // ----------------------------------------------------------
        }
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
        Events.postEvent(Events.ON_SELECT, listboxNya, null);
    }

/*************************************************************************************
 * Event dan Listener (Diawali dengan "on" / Fungsinya sama dengan Do's, yaitu Command)
 **************************************************************************************/


/*************************************************************************************
 * Custom Methods (Untuk method-method private)
 **************************************************************************************/
    private void executeDetail(Crole croleNya) throws InterruptedException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("objListCtrl", this);
        map.put("selected", croleNya);

        try {
            Executions.createComponents("/frontend/core/role/vFormRole.zul", null, map);
        } catch (Exception e) {
            //LOGGING
        }
    }

    private void loadData() {
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("null", "null");
        allRoles = getCroleService().getByRequest(requestMap, false, null);

        BindUtils.postNotifyChange(null, null, this, "allRoles");
    }

    private void deletingData(final Map<String, Crole> objsToDel) {
        for (Map.Entry<String, Crole> mapNya : objsToDel.entrySet()) {
            try {
                Crole v = mapNya.getValue();
                getCroleService().deleteData(v.getCroleId());
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
    // Untuk Wiring Component yang ada di ZUL apabila diperlukan,
    // Persiapan Wiring Component via wComSel - (DEFAULT)
    private void wiringComponent() {
        listboxNya.getPagingChild().setAutohide(false);
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

    public List<Crole> getAllRoles() {
        return allRoles;
    }
    public void setAllRoles(List<Crole> allRoles) {
        this.allRoles = allRoles;
    }

    public CroleService getCroleService() {
        return croleService;
    }
    public void setCroleService(CroleService croleService) {
        this.croleService = croleService;
    }

    public void getDeletingData(final Map<String, Crole> objsToDel) {
        this.deletingData(objsToDel);
    }

}
