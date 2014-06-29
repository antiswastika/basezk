package com.wd.basezk.composer.userrole;

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
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;

import com.wd.basezk.model.Cuser;
import com.wd.basezk.service.CuserService;

/**
 * @author (ariv.wd@gmail.com)
 * <created-on-20140328-stabilized-vm>
 *
 */

/*Anotasi supaya STS bisa mendeteksi VM ini sebagai sebuah Bean*/
@Controller
/*Anotasi supaya semua Spring-Bean bisa dideteksi oleh ZK*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)

public class ListUserRoleVM {
    // Default Variables untuk VM-Component
    @Wire("#listboxNya")
    private Listbox listboxNya;

    // Default Variables untuk VM-Model
    //--------------------------> [TidakAda]

    // Untuk WireComponentSelector
    private Component wComSel;

    // Untuk Wire Service Variables (butuh: Setter Getter)
    @WireVariable
    private CuserService cuserService;

    // Untuk Inisiate Variable yang digunakan di ZUL (butuh: Setter Getter)
    private List<Cuser> allUsers;

    // Untuk Wiring Renderer (butuh: Setter Getter)
    private ListitemRenderer<Cuser> allCusersItemRenderer;

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
        ListModelList<Cuser> lml = (ListModelList) listboxNya.getModel();
        lml.clearSelection();
        this.executeDetail( new Cuser() );
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Command
    public void doEdit(@BindingParam("record") Cuser objNya) throws InterruptedException {
        ListModelList<Cuser> lml = (ListModelList) listboxNya.getModel();
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
        ListModelList<Cuser> lml = (ListModelList) listboxNya.getModel();
        final Map<String, Cuser> objsToDel = new HashMap<String, Cuser>();

        for (Cuser objs : lml) {
            if (lml.isSelected(objs)) {
                objsToDel.put(objs.getCuserId(), objs);
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
    private void executeDetail(Cuser cuserNya) throws InterruptedException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("objListCtrl", this);
        map.put("selected", cuserNya);

        try {
            Executions.createComponents("/frontend/core/userrole/vFormUserRole.zul", null, map);
        } catch (Exception e) {
            //LOGGING
        }
    }

    private void loadData() {
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("null", "null");
        allUsers = getCuserService().getByRequest(requestMap, false, null);

        BindUtils.postNotifyChange(null, null, this, "allUsers");
    }

    private void deletingData(final Map<String, Cuser> objsToDel) {
        for (Map.Entry<String, Cuser> mapNya : objsToDel.entrySet()) {
            try {
                Cuser v = mapNya.getValue();
                getCuserService().deleteData(v.getCuserId());
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
    @SuppressWarnings("unchecked")
    private void initComponent() {
        listboxNya.getPagingChild().setAutohide(false);
        setAllCusersItemRenderer(rendering_listbox_allUsers());
    }

    @SuppressWarnings("rawtypes")
    private ListitemRenderer rendering_listbox_allUsers() {
        return new ListitemRenderer() {
            @SuppressWarnings("unchecked")
            public void render(Listitem li, Object data, int arg) throws Exception {
                final Cuser objNya = (Cuser) data;

                Listcell lc;
                //----------------------//
                lc = new Listcell();
                lc.setParent(li);
                //----------------------//
                lc = new Listcell();
                lc.setImage("/assets/img/icon16x16/Modify.png");
                lc.addEventListener("onClick", new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        doEdit(objNya);
                    }
                });
                lc.setParent(li);
                //----------------------//
                lc = new Listcell(Integer.toString(arg+1));
                lc.setParent(li);
                //----------------------//
                lc = new Listcell(objNya.getCuserUsername());
                lc.setParent(li);
                //----------------------//
                lc = new Listcell(objNya.getCuserRoles().toString());
                lc.setParent(li);
                //----------------------//
                li.setAttribute("data", data);
            }
        };
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

    public List<Cuser> getAllUsers() {
        return allUsers;
    }
    public void setAllUsers(List<Cuser> allUsers) {
        this.allUsers = allUsers;
    }

    public ListitemRenderer<Cuser> getAllCusersItemRenderer() {
        return allCusersItemRenderer;
    }
    public void setAllCusersItemRenderer(ListitemRenderer<Cuser> allCusersItemRenderer) {
        this.allCusersItemRenderer = allCusersItemRenderer;
    }

    public CuserService getCuserService() {
        return cuserService;
    }
    public void setCuserService(CuserService cuserService) {
        this.cuserService = cuserService;
    }

    public void getDeletingData(final Map<String, Cuser> objsToDel) {
        this.deletingData(objsToDel);
    }

}
