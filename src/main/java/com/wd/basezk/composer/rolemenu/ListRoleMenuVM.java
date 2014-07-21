package com.wd.basezk.composer.rolemenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import com.wd.basezk.model.Crole;
import com.wd.basezk.model.CroleMenu;
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

public class ListRoleMenuVM {
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
    private ListitemRenderer<Crole> allCrolesItemRenderer;

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
            Executions.createComponents("/frontend/core/rolemenu/vFormRoleMenu.zul", null, map);
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
    @SuppressWarnings("unchecked")
    private void initComponent() {
        listboxNya.getPagingChild().setAutohide(false);
        setAllCrolesItemRenderer(rendering_listbox_allRoles());
    }

    @SuppressWarnings("rawtypes")
    private ListitemRenderer rendering_listbox_allRoles() {
        return new ListitemRenderer() {
            @SuppressWarnings("unchecked")
            public void render(Listitem li, Object data, int arg) throws Exception {
                final Crole objNya = (Crole) data;

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
                lc = new Listcell(objNya.getCroleRolename());
                lc.setParent(li);
                //----------------------//
                lc = new Listcell();
                Iterator iterator = objNya.getCroleMenus().iterator();
                List<String> listStrCrole = new ArrayList<String>();
                while (iterator.hasNext()){
                    CroleMenu roleMenu = (CroleMenu) iterator.next();
                    listStrCrole.add(roleMenu.getCmenu().getCmenuLabel());
                }
                String strRoles = "";
                for (int i=0; i<listStrCrole.size(); i++) {
                    strRoles = listStrCrole.get(i) + (i > 0 ? ", " : "") + strRoles;
                }
                lc.setLabel(strRoles);
                lc.setParent(li);
                //----------------------//
                lc = new Listcell(objNya.getCroleDesc());
                lc.setParent(li);
                //----------------------//
                lc = new Listcell(objNya.getCroleDeleteon() == null ? "No" : "Yes");
                lc.setParent(li);
                //----------------------//
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                lc = new Listcell(objNya.getCroleDeleteon() == null ? "" : df.format(objNya.getCroleDeleteon()));
                lc.setParent(li);
                //----------------------//
                lc = new Listcell(objNya.getCroleInputby());
                lc.setParent(li);
                //----------------------//
                SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                lc = new Listcell(objNya.getCroleInputon() == null ? "" : df2.format(objNya.getCroleInputon()));
                lc.setParent(li);
                //----------------------//
                lc = new Listcell(objNya.getCroleUpdateby());
                lc.setParent(li);
                //----------------------//
                SimpleDateFormat df3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                lc = new Listcell(objNya.getCroleUpdateon() == null ? "" : df3.format(objNya.getCroleUpdateon()));
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

    public List<Crole> getAllRoles() {
        return allRoles;
    }
    public void setAllRoles(List<Crole> allRoles) {
        this.allRoles = allRoles;
    }

    public ListitemRenderer<Crole> getAllCrolesItemRenderer() {
        return allCrolesItemRenderer;
    }
    public void setAllCrolesItemRenderer(ListitemRenderer<Crole> allCrolesItemRenderer) {
        this.allCrolesItemRenderer = allCrolesItemRenderer;
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
