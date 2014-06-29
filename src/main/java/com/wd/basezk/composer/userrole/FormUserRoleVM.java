package com.wd.basezk.composer.userrole;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

import com.wd.basezk.model.Crole;
import com.wd.basezk.model.Cuser;
import com.wd.basezk.model.CuserRole;
import com.wd.basezk.service.CroleService;
import com.wd.basezk.service.CuserRoleService;
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

public class FormUserRoleVM {
    // Default Variables untuk VM-Component
    @Wire("#dialogWindow")
    private Window dialogWindow;

    // Default Variables untuk VM-Model
    //--------------------------> [TidakAda]

    // Untuk WireComponentSelector
    private Component wComSel;
    private ListUserRoleVM wObjList;

    // Untuk Wire Service Variables (butuh: Setter Getter)
    @WireVariable
    private CuserRoleService cuserRoleService;
    @WireVariable
    private CuserService cuserService;
    @WireVariable
    private CroleService croleService;

    // Untuk Inisiate Variable yang digunakan di ZUL (butuh: Setter Getter)
    private CuserRole selected = new CuserRole();
    private Map<String, Integer> txtMaxLength;
    private List<Cuser> allUsers;
    private List<Crole> allRoles;
    private String strSelectedUser;
    private String strSelectedRoles;

    // Untuk Wiring Renderer (butuh: Setter Getter)
    //--------------------------> [TidakAda]

/*************************************************************************************
 * Initialize
 **************************************************************************************/
    @AfterCompose
    public void onCreate(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("objListCtrl") ListUserRoleVM arg, @ExecutionArgParam("selected") CuserRole arg2) {
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

        Map<String, String> requestMapUser = new HashMap<String, String>();
        requestMapUser.put("null", "null");
        setAllUsers(getCuserService().getByRequest(requestMapUser, false, null));

        Map<String, String> requestMapRole = new HashMap<String, String>();
        requestMapRole.put("null",null);
        setAllRoles(getCroleService().getByRequest(requestMapRole, false, null));
    }

/*************************************************************************************
 * Do's (Berisi kumpulan Command yang dipanggil dari ZUL, diawali dengan kata "do")
 **************************************************************************************/
    @Command
    @NotifyChange("selected")
    public void doSave() throws InterruptedException {
        /*if (selected.getCuserRoleId() == null) {
            getCuserRoleService().insertData(selected);
            getwObjList().doRefresh();
        } else {
            doEdit();
        }*/


    }

    @SuppressWarnings("unused")
    private void doEdit() {
        /*getCuserRoleService().updateData(selected);
        getwObjList().doRefresh();
        BindUtils.postNotifyChange(null, null, this, "selected");*/
    }

    @Command
    public void doDelete() {
//        final Map<String, CuserRole> objsToDel = new HashMap<String, CuserRole>();
//        final Window windowNya = dialogWindow;
//        objsToDel.put(selected.getCuserRoleId(), selected);
//
//        // ----------------------------------------------------------
//        // Show a confirm box
//        // ----------------------------------------------------------
//        //TODO: Labeling!
//        Messagebox.show("XXXXXXXXXXXX", "Confirmation", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
//            @Override
//            public void onEvent(Event event) throws Exception {
//                if(((Integer)event.getData()).intValue()==Messagebox.YES){
//                    getwObjList().getDeletingData(objsToDel);
//                    Events.postEvent(Events.ON_CLOSE, windowNya, null);
//                }
//            }
//        });
//        // ----------------------------------------------------------
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
    @SuppressWarnings("rawtypes")
    @NotifyChange("strSelectedUser")
    @Command
    public void onSelectUser(@BindingParam("data") Set<Cuser> dataNya) throws InterruptedException {
        setStrSelectedUser("");
        Iterator iterator = dataNya.iterator();
        while (iterator.hasNext()){
            Listitem li = (Listitem) iterator.next();
            Cuser liVal = li.getValue();
            setStrSelectedUser( liVal.getCuserUsername() );
        }
    }

    @SuppressWarnings("rawtypes")
    @NotifyChange("strSelectedRoles")
    @Command
    public void onSelectRole(@BindingParam("data") Set<Crole> dataNya) throws InterruptedException {
        setStrSelectedRoles("");
        Iterator iterator = dataNya.iterator();
        List<String> listStrCrole = new ArrayList<String>();
        while (iterator.hasNext()){
            Listitem li = (Listitem) iterator.next();
            Crole liVal = li.getValue();
            listStrCrole.add(liVal.getCroleRolename());
        }
        String strRoles = "";
        for (int i=0; i<listStrCrole.size(); i++) {
            strRoles = listStrCrole.get(i) + (i > 0 ? ", " : "") + strRoles;
        }
        setStrSelectedRoles(strRoles);
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

/*************************************************************************************
 * Set dan Get
 **************************************************************************************/
    public Component getwComSel() {
        return wComSel;
    }
    public void setwComSel(Component wComSel) {
        this.wComSel = wComSel;
    }

    public ListUserRoleVM getwObjList() {
        return wObjList;
    }
    public void setwObjList(ListUserRoleVM wObjList) {
        this.wObjList = wObjList;
    }

    public CuserRole getSelected() {
        return selected;
    }
    public void setSelected(CuserRole selected) {
        this.selected = selected;
    }

    public Map<String, Integer> getTxtMaxLength() {
        return txtMaxLength;
    }
    public void setTxtMaxLength(Map<String, Integer> txtMaxLength) {
        this.txtMaxLength = txtMaxLength;
    }

    public List<Cuser> getAllUsers() {
        return allUsers;
    }
    public void setAllUsers(List<Cuser> allUsers) {
        this.allUsers = allUsers;
    }

    public List<Crole> getAllRoles() {
        return allRoles;
    }
    public void setAllRoles(List<Crole> allRoles) {
        this.allRoles = allRoles;
    }

    public String getStrSelectedUser() {
        return strSelectedUser;
    }
    public void setStrSelectedUser(String strSelectedUser) {
        this.strSelectedUser = strSelectedUser;
    }

    public String getStrSelectedRoles() {
        return strSelectedRoles;
    }
    public void setStrSelectedRoles(String strSelectedRoles) {
        this.strSelectedRoles = strSelectedRoles;
    }

    public CuserRoleService getCuserRoleService() {
        return cuserRoleService;
    }
    public void setCuserRoleService(CuserRoleService cuserRoleService) {
        this.cuserRoleService = cuserRoleService;
    }

    public CuserService getCuserService() {
        return cuserService;
    }
    public void setCuserService(CuserService cuserService) {
        this.cuserService = cuserService;
    }

    public CroleService getCroleService() {
        return croleService;
    }
    public void setCroleService(CroleService croleService) {
        this.croleService = croleService;
    }

}
