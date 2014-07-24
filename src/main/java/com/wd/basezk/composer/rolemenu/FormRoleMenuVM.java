package com.wd.basezk.composer.rolemenu;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;

import org.springframework.stereotype.Controller;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

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

public class FormRoleMenuVM {
    // Default Variables untuk VM-Component
    @Wire("#dialogWindow")
    private Window dialogWindow;
    @Wire("#cmbRoles")
    private Combobox cmbRoles;
    @Wire("#treeNya")
    private Tree treeNya;

    // Default Variables untuk VM-Model
    //--------------------------> [TidakAda]

    // Untuk WireComponentSelector
    private Component wComSel;
    private ListRoleMenuVM wObjList;

    // Untuk Wire Service Variables (butuh: Setter Getter)
    @WireVariable
    private CroleMenuService croleMenuService;
    @WireVariable
    private CroleService croleService;
    @WireVariable
    private CmenuService cmenuService;

    // Untuk Inisiate Variable yang digunakan di ZUL (butuh: Setter Getter)
    private Crole selected = new Crole();
    private Map<String, Integer> txtMaxLength;
    private List<Crole> allRoles;
    private List<Cmenu> selectedMenus = new ArrayList<Cmenu>();

    // Untuk Wiring Renderer (butuh: Setter Getter)
    //--------------------------> [TidakAda]

/*************************************************************************************
 * Initialize
 **************************************************************************************/
    @AfterCompose
    public void onCreate(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("objListCtrl") ListRoleMenuVM arg, @ExecutionArgParam("selected") Crole arg2) {
        Selectors.wireComponents(view, this, false);
        setwComSel(view);
        if (arg != null) { setwObjList(arg); }
        if (arg2 != null) { setSelected(arg2); }
        initComponent();
        prepareAll();
    }

/*************************************************************************************
 * Preparation (Load Variables Value)
 **************************************************************************************/
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void prepareAll() {
        txtMaxLength = new HashMap<String, Integer>();
        try {
            setMaxLength4All();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        loadData();

        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("null", "null");
        allRoles = getCroleService().getByRequest(requestMap, false, null);

        for (int i=0; i<allRoles.size(); i++) {
            if (allRoles.get(i).getCroleId().equals(selected.getCroleId())) {
                setSelected(allRoles.get(i));

                DefaultTreeModel<Cmenu> dtm = (DefaultTreeModel) treeNya.getModel();
                TreeNode rootNya = dtm.getRoot();
                if (dtm.getSelectionCount() > 0) { dtm.clearSelection(); }
                Crole roleNya = selected;
                selected = roleNya;
                if (roleNya.getCroleMenus().size()>0) {
                    selectedOnlyTreeNodeChilds(roleNya, dtm, rootNya);
                }

                break;
            }
        }
    }

/*************************************************************************************
 * Do's (Berisi kumpulan Command yang dipanggil dari ZUL, diawali dengan kata "do")
 **************************************************************************************/
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Command
    @NotifyChange("selected")
    public void doSave() throws InterruptedException {
        if (selectedMenus.size() > 0) {
            manualValidateForm();
            Set<Cmenu> selectedMenus2 = new HashSet<Cmenu>();

            DefaultTreeModel<Cmenu> dtm = (DefaultTreeModel) treeNya.getModel();
            Iterator iterator = dtm.getSelection().iterator();
            while (iterator.hasNext()){
                TreeNode tn = (TreeNode) iterator.next();
                Cmenu cmenuNya = (Cmenu) tn.getData();
                selectedMenus2.add(cmenuNya);
            }

            getCroleMenuService().insertData(selected, selectedMenus2);
            getwObjList().doRefresh();

            Set selectedMenusSet = new HashSet(selectedMenus);
            selected.setCroleMenus(selectedMenusSet);
        }
        dialogWindow.setPosition("nocenter");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Command
    public void doDelete() {
        final Map<String, Crole> objsToDel = new HashMap<String, Crole>();
        final Window windowNya = dialogWindow;
        objsToDel.put(selected.getCroleId(), selected);

        // ----------------------------------------------------------
        // Show a confirm box
        // ----------------------------------------------------------
        //TODO: Labeling!
        Messagebox.show("XXXXXXXXXXXX", "Confirmation", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if(((Integer)event.getData()).intValue()==Messagebox.YES){
                    getwObjList().getDeletingData(objsToDel);
                    Events.postEvent(Events.ON_CLOSE, windowNya, null);
                }
            }
        });
        // ----------------------------------------------------------
        dialogWindow.setPosition("nocenter");
    }

    @Command
    @NotifyChange("selected")
    public void doReload() { }

    @Command
    public void doClose(@BindingParam("eventNya") Event eventNya) throws InterruptedException {
        dialogWindow.onClose();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Command
    public void doSelectAll(@BindingParam("eventNya") Event eventNya) throws InterruptedException {
        DefaultTreeModel<Cmenu> dtm = (DefaultTreeModel) treeNya.getModel();
        TreeNode rootNya = dtm.getRoot();
        selectAlsoTreeNodeChilds(dtm, rootNya, true);
        treeNya.invalidate();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Command
    public void doSelectNone(@BindingParam("eventNya") Event eventNya) throws InterruptedException {
        DefaultTreeModel<Cmenu> dtm = (DefaultTreeModel) treeNya.getModel();
        TreeNode rootNya = dtm.getRoot();
        selectAlsoTreeNodeChilds(dtm, rootNya, false);
    }

/*************************************************************************************
 * Event dan Listener (Diawali dengan "on" / Fungsinya sama dengan Do's, yaitu Command)
 **************************************************************************************/
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @NotifyChange("selected")
    @Command
    public void onChangeRole(@BindingParam("event") Event eventNya, @BindingParam("data") Comboitem dataNya) throws InterruptedException {
        DefaultTreeModel<Cmenu> dtm = (DefaultTreeModel) treeNya.getModel();
        TreeNode rootNya = dtm.getRoot();

        if (dtm.getSelectionCount() > 0) { dtm.clearSelection(); }
        Crole roleNya = dataNya.getValue();
        selected = roleNya;
        if (roleNya.getCroleMenus().size()>0) {
            selectedOnlyTreeNodeChilds(dataNya, dtm, rootNya);
        }

        treeNya.invalidate();
        dialogWindow.setPosition("nocenter");
    }

/*************************************************************************************
 * Custom Methods (Untuk method-method private)
 **************************************************************************************/
    private void setMaxLength4All() throws NoSuchFieldException, SecurityException {
        Field[] fields = selected.getClass().getDeclaredFields();
        for (int i=0; i<fields.length; i++) {
            if (fields[i].getType().toString().toLowerCase().contains("string")) {
                txtMaxLength.put(fields[i].getName(), selected.getClass().getDeclaredField(fields[i].getName()).getAnnotation(Column.class).length());
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private void loadData() {
        if (treeNya.getModel() == null) {
            DefaultTreeModel<Cmenu> treeModelNya = new DefaultTreeModel<Cmenu>(generateTreeModel());
            treeModelNya.setMultiple(true);
            treeNya.setModel(treeModelNya);
        } else {
            DefaultTreeModel treeModelNya = (DefaultTreeModel) treeNya.getModel();
            treeModelNya = new DefaultTreeModel<Cmenu>(generateTreeModel());
            treeModelNya.setMultiple(true);
            treeNya.setModel(treeModelNya);
        }

        treeNya.setItemRenderer(rendering_tree_allMenus());
        treeNya.setCheckmark(true);
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void selectAlsoTreeNodeChilds(DefaultTreeModel<Cmenu> dtm, TreeNode parentNya, Boolean select) {
        int counterChildren = dtm.getChildCount(parentNya);
        if (counterChildren > 0) {
            for (int i=0; i<counterChildren; i++) {
                TreeNode childNya = dtm.getChild(parentNya, i);
                Cmenu cmenuNya = (Cmenu) childNya.getData();
                if (select.equals(true)) {
                    dtm.addToSelection(childNya);
                    selectedMenus.add(cmenuNya);
                } else {
                    dtm.removeFromSelection(childNya);
                    selectedMenus.remove(cmenuNya);
                }
                selectAlsoTreeNodeChilds(dtm, childNya, select);
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void selectedOnlyTreeNodeChilds(Comboitem dataNya, DefaultTreeModel<Cmenu> dtm, TreeNode parentNya) {
        int counterChildren = dtm.getChildCount(parentNya);
        if (counterChildren > 0) {
            for (int i=0; i<counterChildren; i++) {
                TreeNode childNya = dtm.getChild(parentNya, i);
                Cmenu cmenuNya = (Cmenu) childNya.getData();

                Crole roleNya = dataNya.getValue();
                Iterator iterator = roleNya.getCroleMenus().iterator();
                while (iterator.hasNext()){
                    CroleMenu roleMenuNya = (CroleMenu) iterator.next();
                    Cmenu menuNya2 = roleMenuNya.getCmenu();

                    if (menuNya2.getCmenuId().equals(cmenuNya.getCmenuId())) {
                        dtm.addToSelection(childNya);
                        selectedMenus.add(menuNya2);
                        break;
                    }
                }

                selectedOnlyTreeNodeChilds(dataNya, dtm, childNya);
            }
        }
        BindUtils.postNotifyChange(null, null, this, "selectedMenus");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void selectedOnlyTreeNodeChilds(Crole dataNya, DefaultTreeModel<Cmenu> dtm, TreeNode parentNya) {
        int counterChildren = dtm.getChildCount(parentNya);
        if (counterChildren > 0) {
            for (int i=0; i<counterChildren; i++) {
                TreeNode childNya = dtm.getChild(parentNya, i);
                Cmenu cmenuNya = (Cmenu) childNya.getData();

                Crole roleNya = dataNya;
                Iterator iterator = roleNya.getCroleMenus().iterator();
                while (iterator.hasNext()){
                    CroleMenu roleMenuNya = (CroleMenu) iterator.next();
                    Cmenu menuNya2 = roleMenuNya.getCmenu();

                    if (menuNya2.getCmenuId().equals(cmenuNya.getCmenuId())) {
                        dtm.addToSelection(childNya);
                        selectedMenus.add(menuNya2);
                        break;
                    }
                }

                selectedOnlyTreeNodeChilds(dataNya, dtm, childNya);
            }
        }
        BindUtils.postNotifyChange(null, null, this, "selectedMenus");
    }

/*************************************************************************************
 * Validator
 **************************************************************************************/
    public Validator getValidateTextboxNotNull() {
        return new AbstractValidator() {
            @Override
            public void validate(ValidationContext ctx) {
                InputElement componentNya = (InputElement)ctx.getBindContext().getValidatorArg("component");
                String text = (String)ctx.getBindContext().getValidatorArg("text");
                Clients.clearWrongValue(componentNya);
                if(text.trim().equals("") || text == null) {
                    componentNya.setFocus(true);
                    throw new WrongValueException(componentNya, "Required Field!");
                }
            }
        };
    }

    private void manualValidateForm() {
        //Cek Roles
        if (selected.getCroleId() == null) {
            cmbRoles.setFocus(true);
            throw new WrongValueException(cmbRoles, "Required Field!");
        }
    }

/*************************************************************************************
 * Renderer
 **************************************************************************************/
    private void initComponent() {

    }

    @SuppressWarnings("rawtypes")
    private TreeitemRenderer rendering_tree_allMenus() {
        return new TreeitemRenderer() {

            @SuppressWarnings("unchecked")
            public void render(final Treeitem ti, Object data, int index) throws Exception {
                final DefaultTreeNode tn = (DefaultTreeNode) data;
                final Cmenu menuNya = (Cmenu) tn.getData();
                final DefaultTreeModel<Cmenu> dtm = (DefaultTreeModel) treeNya.getModel();

                Treerow tr = new Treerow();
                tr.appendChild(new Treecell(String.valueOf(menuNya.getCmenuLabel())));
                ti.appendChild(tr);
                //----------------------//

                ti.addEventListener("onClick", new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        selectAlsoTreeNodeChilds(dtm, tn, ti.isSelected());
                    }
                });

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

    public ListRoleMenuVM getwObjList() {
        return wObjList;
    }
    public void setwObjList(ListRoleMenuVM wObjList) {
        this.wObjList = wObjList;
    }

    public CroleMenuService getCroleMenuService() {
        return croleMenuService;
    }
    public void setCroleMenuService(CroleMenuService croleMenuService) {
        this.croleMenuService = croleMenuService;
    }

    public Crole getSelected() {
        return selected;
    }
    public void setSelected(Crole selected) {
        this.selected = selected;
    }

    public Map<String, Integer> getTxtMaxLength() {
        return txtMaxLength;
    }
    public void setTxtMaxLength(Map<String, Integer> txtMaxLength) {
        this.txtMaxLength = txtMaxLength;
    }

    public List<Crole> getAllRoles() {
        return allRoles;
    }
    public void setAllRoles(List<Crole> allRoles) {
        this.allRoles = allRoles;
    }

    public List<Cmenu> getSelectedMenus() {
        return selectedMenus;
    }
    public void setSelectedMenus(List<Cmenu> selectedMenus) {
        this.selectedMenus = selectedMenus;
    }

    public CroleService getCroleService() {
        return croleService;
    }
    public void setCroleService(CroleService croleService) {
        this.croleService = croleService;
    }

    public CmenuService getCmenuService() {
        return cmenuService;
    }
    public void setCmenuService(CmenuService cmenuService) {
        this.cmenuService = cmenuService;
    }

}
