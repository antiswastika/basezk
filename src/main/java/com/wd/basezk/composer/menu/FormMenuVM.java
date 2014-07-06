package com.wd.basezk.composer.menu;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

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

public class FormMenuVM {
    // Default Variables untuk VM-Component
    @Wire("#dialogWindow")
    private Window dialogWindow;
    @Wire("#treeNya")
    private Tree treeNya;

    // Default Variables untuk VM-Model
    //--------------------------> [TidakAda]

    // Untuk WireComponentSelector
    private Component wComSel;
    private ListMenuVM wObjList;

    // Untuk Wire Service Variables (butuh: Setter Getter)
    @WireVariable
    private CmenuService cmenuService;

    // Untuk Inisiate Variable yang digunakan di ZUL (butuh: Setter Getter)
    private Cmenu selected = new Cmenu();
    private Map<String, Integer> txtMaxLength;

    // Untuk Wiring Renderer (butuh: Setter Getter)
    //--------------------------> [TidakAda]

/*************************************************************************************
 * Initialize
 **************************************************************************************/
    @AfterCompose
    public void onCreate(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("objListCtrl") ListMenuVM arg, @ExecutionArgParam("selected") Cmenu arg2) {
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
    }

/*************************************************************************************
 * Do's (Berisi kumpulan Command yang dipanggil dari ZUL, diawali dengan kata "do")
 **************************************************************************************/
    @Command
    @NotifyChange("selected")
    public void doSave() throws InterruptedException {
        if (selected.getCmenuId() == null) {
            getCmenuService().insertData(selected);
            getwObjList().doRefresh();
        } else {
            doEdit();
        }
    }

    private void doEdit() {
        getCmenuService().updateData(selected);
        getwObjList().doRefresh();
        BindUtils.postNotifyChange(null, null, this, "selected");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Command
    public void doDelete() {
        final Map<String, Cmenu> objsToDel = new HashMap<String, Cmenu>();
        final Window windowNya = dialogWindow;
        objsToDel.put(selected.getCmenuId(), selected);

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
    }

    @Command
    @NotifyChange("selected")
    public void doReload() { }

    @Command
    public void doClose(@BindingParam("eventNya") Event eventNya) throws InterruptedException {
        dialogWindow.onClose();
    }

/*************************************************************************************
 * Event dan Listener (Diawali dengan "on" / Fungsinya sama dengan Do's, yaitu Command)
 **************************************************************************************/


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

    private void loadData() {
        treeNya.setModel(getTreeModel());
        treeNya.setItemRenderer(rendering_tree_allMenus());
        treeNya.setMultiple(false);
        treeNya.setCheckmark(true);
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

/*************************************************************************************
 * Renderer
 **************************************************************************************/
    private void initComponent() {

    }

    @SuppressWarnings("rawtypes")
    private TreeitemRenderer rendering_tree_allMenus() {
        return new TreeitemRenderer() {

            public void render(final Treeitem ti, Object data, int index) throws Exception {
                DefaultTreeNode tn = (DefaultTreeNode) data;
                final Cmenu menuNya = (Cmenu) tn.getData();

                Treerow tr = new Treerow();
                ti.appendChild(tr);
                //----------------------//
                tr.appendChild(new Treecell(String.valueOf(menuNya.getCmenuLabel())));

                ti.setOpen(true);

                if (selected.getCmenuId() != null && selected.getCmenuParentId() != null) {
                    Cmenu parentNya = getCmenuService().getById(selected.getCmenuParentId());
                    if (menuNya.getCmenuId().equals(parentNya.getCmenuId())) {
                        ti.setSelected(true);
                        ti.setFocus(true);
                    } else {
                        ti.setSelected(false);
                        ti.setFocus(false);
                    }
                }
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

    public ListMenuVM getwObjList() {
        return wObjList;
    }
    public void setwObjList(ListMenuVM wObjList) {
        this.wObjList = wObjList;
    }

    public Cmenu getSelected() {
        return selected;
    }
    public void setSelected(Cmenu selected) {
        this.selected = selected;
    }

    public Map<String, Integer> getTxtMaxLength() {
        return txtMaxLength;
    }
    public void setTxtMaxLength(Map<String, Integer> txtMaxLength) {
        this.txtMaxLength = txtMaxLength;
    }

    public CmenuService getCmenuService() {
        return cmenuService;
    }
    public void setCmenuService(CmenuService cmenuService) {
        this.cmenuService = cmenuService;
    }

}
