package com.wd.basezk.composer.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

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
    //--------------------------> [TidakAda]

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
    @Command
    public void doNew() throws InterruptedException {

    }

    @Command
    public void doEdit(@BindingParam("record") Cmenu objNya) throws InterruptedException {

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
        loadData();
        Events.postEvent(Events.ON_SELECT, treeNya, null);
    }

/*************************************************************************************
 * Event dan Listener (Diawali dengan "on" / Fungsinya sama dengan Do's, yaitu Command)
 **************************************************************************************/


/*************************************************************************************
 * Custom Methods (Untuk method-method private)
 **************************************************************************************/
    @SuppressWarnings("unused")
    private void executeDetail(Cmenu cmenuNya) throws InterruptedException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("objListCtrl", this);
        map.put("selected", cmenuNya);

        try {
            Executions.createComponents("/frontend/core/menu/vFormMenu.zul", null, map);
        } catch (Exception e) {
            //LOGGING
        }
    }

    private void loadData() {
        treeNya.setModel(getTreeModel());
        treeNya.setItemRenderer(rendering_tree_allMenus());
        treeNya.setMultiple(true);
        treeNya.setCheckmark(true);
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

    public DefaultTreeModel<Cmenu> getTreeModel() {
        return new DefaultTreeModel<Cmenu>(generateTreeModel());
    }

    private DefaultTreeNode<Cmenu> generateTreeModel() {
        List<DefaultTreeNode<Cmenu>> inner1 = new ArrayList<DefaultTreeNode<Cmenu>>();

        List<Cmenu> allMenus = new ArrayList<Cmenu>();
        allMenus = createMenuRoot();

        for (int i=0; i<allMenus.size(); i++) {
            List<DefaultTreeNode<Cmenu>> inner2 = new ArrayList<DefaultTreeNode<Cmenu>>();
            generateSubTreeModel(allMenus.get(i), inner2);

            if (inner2.size() == 0) {
                inner1.add(new DefaultTreeNode<Cmenu>(allMenus.get(i)));
            } else {
                inner1.add(new DefaultTreeNode<Cmenu>(allMenus.get(i), inner2));
            }
        }

        return new DefaultTreeNode<Cmenu>(null, inner1);
    }

    private void generateSubTreeModel(Cmenu parent, List<DefaultTreeNode<Cmenu>> innerList) {
        List<Cmenu> allSubMenus = new ArrayList<Cmenu>();
        allSubMenus = createMenuChildByParentId(parent);

        for (int i=0; i<allSubMenus.size(); i++) {
            List<DefaultTreeNode<Cmenu>> inner2 = new ArrayList<DefaultTreeNode<Cmenu>>();
            generateSubTreeModel(allSubMenus.get(i), inner2);

            if (inner2.size() == 0) {
                innerList.add(new DefaultTreeNode<Cmenu>(allSubMenus.get(i)));
            } else {
                innerList.add(new DefaultTreeNode<Cmenu>(allSubMenus.get(i), inner2));
            }
        }
    }

    private List<Cmenu> createMenuRoot() {
        List<Cmenu> retVal = new ArrayList<Cmenu>();

        String[] whereArgs = { "AND (cmenu_parent_id IS NULL OR cmenu_parent_id = '')" };
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
    private void initComponent() {

    }

    @SuppressWarnings("rawtypes")
    private TreeitemRenderer rendering_tree_allMenus() {
        return new TreeitemRenderer() {
            public void render(Treeitem ti, Object data, int index) throws Exception {
                DefaultTreeNode tn = (DefaultTreeNode) data;
                Cmenu menuNya = (Cmenu) tn.getData();
                Treerow tr = new Treerow();
                ti.appendChild(tr);
                tr.appendChild(new Treecell(String.valueOf(menuNya.getCmenuLabel())));
                tr.appendChild(new Treecell(String.valueOf(menuNya.getCmenuSrc())));
                tr.appendChild(new Treecell(String.valueOf(menuNya.getCmenuIsTab())));
                tr.appendChild(new Treecell(String.valueOf(menuNya.getCmenuIsPopup())));
                tr.appendChild(new Treecell(String.valueOf(menuNya.getCmenuPopupWidth())));
                tr.appendChild(new Treecell(String.valueOf(menuNya.getCmenuPopupHeight())));
                tr.appendChild(new Treecell(String.valueOf(menuNya.getCmenuPopupIsResizeable())));
                tr.appendChild(new Treecell(String.valueOf(menuNya.getCmenuCloseable())));
                tr.appendChild(new Treecell(String.valueOf(menuNya.getCmenuDesc())));
                tr.appendChild(new Treecell(String.valueOf(menuNya.getCmenuIconMenu())));
                tr.appendChild(new Treecell(String.valueOf(menuNya.getCmenuIsCreateShortcut())));
                tr.appendChild(new Treecell(String.valueOf(menuNya.getCmenuIconShortcut())));
                tr.appendChild(new Treecell(String.valueOf(menuNya.getCmenuToolbar())));
                ti.setOpen(true);
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
