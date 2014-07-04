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
import org.zkoss.zul.TreeModel;
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
        treeNya.setModel(generateTreeModel());
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private TreeModel generateTreeModel() {
        /* CONTOH 1:
         * TreeModel model = new DefaultTreeModel(
                new DefaultTreeNode(null,
                    new DefaultTreeNode[] {
                        new DefaultTreeNode(new FileInfo("/doc", "Release and License Notes")),
                        new DefaultTreeNode(new FileInfo("/dist", "Distribution"),
                        new DefaultTreeNode[] {
                            new DefaultTreeNode(new FileInfo("/lib", "ZK Libraries"),
                                new DefaultTreeNode[] {
                                    new DefaultTreeNode(new FileInfo("zcommon.jar", "ZK Common Library")),
                                    new DefaultTreeNode(new FileInfo("zk.jar", "ZK Core Library"))
                                }),
                                new DefaultTreeNode(new FileInfo("/src", "Source Code")),
                                new DefaultTreeNode(new FileInfo("/xsd", "XSD Files"))
                            }
                        )
                    }
                )
            );*/

        /* CONTOH 2:
         * TreeModel model = new DefaultTreeModel(
            new DefaultTreeNode(null,
                new DefaultTreeNode[] {
                    new DefaultTreeNode(new FileInfo("/doc", "Release and License Notes")),
                    new DefaultTreeNode(new FileInfo("/dist", "Distribution"))
                }
            )
        );*/

        //Cari Menu Level 1 (BERHASIL)
        /*List<Cmenu> allMenus = new ArrayList<Cmenu>();
        allMenus = createMenuRoot();
        List<DefaultTreeNode> newCol = new ArrayList<DefaultTreeNode>();
        for (int i=0; i<allMenus.size(); i++) {
            newCol.add(
                new DefaultTreeNode( allMenus.get(i) )
            );
        }*/

        List<Cmenu> allMenus = new ArrayList<Cmenu>();
        allMenus = createMenuRoot();
        List<DefaultTreeNode> newCol = new ArrayList<DefaultTreeNode>();
        for (int i=0; i<allMenus.size(); i++) {
            newCol.add(
                    new DefaultTreeNode( allMenus.get(i) )
            );
        }

        //Definisikan Treemodel
        TreeModel model = new DefaultTreeModel(
            new DefaultTreeNode(null,
                newCol
            )
        );

        return model;
    }

    private List<Cmenu> createMenuRoot() {
        List<Cmenu> retVal = new ArrayList<Cmenu>();

        String[] whereArgs = { "AND (cmenu_parent_id IS NULL OR cmenu_parent_id = '')" };
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("null", "null");
        retVal = getCmenuService().getByRequest(requestMap, false, whereArgs);

        return retVal;
    }

    private List<Cmenu> createMenuChildByParentId(String parentId) {
        List<Cmenu> retVal = new ArrayList<Cmenu>();

        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("cmenu_parent_id", parentId);
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
                /*DefaultTreeNode tn = (DefaultTreeNode) data;
                FileInfo menuNya = (FileInfo) tn.getData();
                Treerow tr = new Treerow();
                ti.appendChild(tr);
                tr.appendChild(new Treecell(menuNya.path));
                tr.appendChild(new Treecell(menuNya.description));
                ti.setOpen(true);*/

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
