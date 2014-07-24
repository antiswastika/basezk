package com.wd.basezk.composer.rolemenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import com.wd.basezk.model.Cmenu;
import com.wd.basezk.model.Crole;
import com.wd.basezk.model.CroleMenu;
import com.wd.basezk.service.CmenuService;
import com.wd.basezk.service.CroleMenuService;
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
    @WireVariable
    private CroleMenuService croleMenuService;
    @WireVariable
    private CmenuService cmenuService;

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
                getCroleMenuService().deleteData(v.getCroleId());
            } catch (Exception e) {
                //
            }
        }
        doRefresh();
    }

    @SuppressWarnings("rawtypes")
    private DefaultTreeNode<Cmenu> generateTreeModel(Set<CroleMenu> croleMenus) {
        List<DefaultTreeNode<Cmenu>> inner1 = new ArrayList<DefaultTreeNode<Cmenu>>();

        List<Cmenu> allMenus = new ArrayList<Cmenu>();
        allMenus = createMenuRoot();

        for (int i=0; i<allMenus.size(); i++) {
            List<DefaultTreeNode<Cmenu>> inner2 = new ArrayList<DefaultTreeNode<Cmenu>>();
            generateSubTreeModel(allMenus.get(i), inner2, croleMenus);

            Iterator iterator = croleMenus.iterator();
            while (iterator.hasNext()){
                CroleMenu roleMenuNya = (CroleMenu) iterator.next();
                Cmenu menuNya2 = roleMenuNya.getCmenu();

                if (menuNya2.getCmenuId().equals(allMenus.get(i).getCmenuId())) {
                    if (inner2.size() == 0) {
                        inner1.add(new DefaultTreeNode<Cmenu>(allMenus.get(i)));
                    } else {
                        inner1.add(new DefaultTreeNode<Cmenu>(allMenus.get(i), inner2));
                    }
                }
            }

        }

        return new DefaultTreeNode<Cmenu>(null, inner1);
    }

    @SuppressWarnings("rawtypes")
    private void generateSubTreeModel(Cmenu parent, List<DefaultTreeNode<Cmenu>> innerList, Set<CroleMenu> croleMenus) {
        List<Cmenu> allSubMenus = new ArrayList<Cmenu>();
        allSubMenus = createMenuChildByParentId(parent);

        for (int i=0; i<allSubMenus.size(); i++) {
            List<DefaultTreeNode<Cmenu>> inner2 = new ArrayList<DefaultTreeNode<Cmenu>>();
            generateSubTreeModel(allSubMenus.get(i), inner2, croleMenus);

            Iterator iterator = croleMenus.iterator();
            while (iterator.hasNext()){
                CroleMenu roleMenuNya = (CroleMenu) iterator.next();
                Cmenu menuNya2 = roleMenuNya.getCmenu();

                if (menuNya2.getCmenuId().equals(allSubMenus.get(i).getCmenuId())) {
                    if (inner2.size() == 0) {
                        innerList.add(new DefaultTreeNode<Cmenu>(allSubMenus.get(i)));
                    } else {
                        innerList.add(new DefaultTreeNode<Cmenu>(allSubMenus.get(i), inner2));
                    }
                }
            }

        }
    }

    private List<Cmenu> createMenuRoot() {
        List<Cmenu> retVal = new ArrayList<Cmenu>();

        String[] whereArgs = { "AND (cmenu_parent_id IS NULL OR cmenu_parent_id = '') " };
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("null", "null");
        retVal = getCmenuService().getByRequest(requestMap, false, whereArgs);

        return retVal;
    }

    private List<Cmenu> createMenuChildByParentId(Cmenu parentMenu) {
        List<Cmenu> retVal = new ArrayList<Cmenu>();

        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("cmenu_parent_id", parentMenu.getCmenuId());
        retVal = getCmenuService().getByRequest(requestMap, false, null);

        return retVal;
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
    private TreeitemRenderer rendering_tree_allMenus(final Tree treeNya) {
        return new TreeitemRenderer() {

            public void render(final Treeitem ti, Object data, int index) throws Exception {
                final DefaultTreeNode tn = (DefaultTreeNode) data;
                final Cmenu menuNya = (Cmenu) tn.getData();

                Treerow tr = new Treerow();
                tr.appendChild(new Treecell(String.valueOf(menuNya.getCmenuLabel())));
                ti.appendChild(tr);
                //----------------------//
                ti.setOpen(true);
            }
        };
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

                Tree treeNya = new Tree();
                treeNya.setHeight("210px");
                treeNya.setVflex(true);
                treeNya.setStyle("background-color: #ffffff; border-top-width: 1px; border-left-width: 1px; border-right-width: 1px; border-bottom-width: 1px;");

                DefaultTreeModel<Cmenu> treeModelNya = new DefaultTreeModel<Cmenu>(generateTreeModel(objNya.getCroleMenus()));
                treeNya.setModel( treeModelNya );
                treeNya.setItemRenderer(rendering_tree_allMenus(treeNya));

                lc.appendChild(treeNya);
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

    public CroleMenuService getCroleMenuService() {
        return croleMenuService;
    }
    public void setCroleMenuService(CroleMenuService croleMenuService) {
        this.croleMenuService = croleMenuService;
    }

    public CmenuService getCmenuService() {
        return cmenuService;
    }
    public void setCmenuService(CmenuService cmenuService) {
        this.cmenuService = cmenuService;
    }

    public void getDeletingData(final Map<String, Crole> objsToDel) {
        this.deletingData(objsToDel);
    }

}
