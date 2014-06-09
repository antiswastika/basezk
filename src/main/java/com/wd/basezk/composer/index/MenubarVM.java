package com.wd.basezk.composer.index;

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
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Center;
import org.zkoss.zul.Include;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Menuseparator;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.West;

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

public class MenubarVM {
    // Default Variables untuk VM-Component
    private North northFrameNya;
    private West westFrameNya;
    private Center centerFrameNya;
    private South southFrameNya;
    private Include centerFrameIncludeNya;
    private Tabbox tabboxCenterNya;
    private Tabs tabsNya;
    private Tab tabNya;
    private Tabpanels tabpanelsNya;
    private Tabpanel tabpanelNya;
    private Include menubarFrameIncludeNya;
    private Menubar menubarNorthNya;

    // Default Variables untuk VM-Model
    private Cmenu cmenuNya;

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
        wiringComponent();
        prepareToView();
    }

/*************************************************************************************
 * Preparation (Load Variables Value)
 **************************************************************************************/
    private void prepareToView() {
        createDashboard();
        createMenu();
    }

/*************************************************************************************
 * Do's (Berisi kumpulan Command yang dipanggil dari ZUL, diawali dengan kata "do")
 **************************************************************************************/
    @Command
    public void doCreateTab(@BindingParam("id") String idNya, @BindingParam("src") String srcNya, @BindingParam("lbl") String lblNya, @BindingParam("closeable") Boolean closeable) {
        Tab tabNya = null;
        String tabPrefixMenuId = "tabid";
        String tabpanelPrefixMenuId = "tabpanelid";
        Boolean found = false;

        for (int i=0; i<tabsNya.getChildren().size(); i++) {
            tabNya = (Tab) tabsNya.getChildren().get(i);
            if (tabNya.getId().equals(tabPrefixMenuId + idNya)==true) {
                found = true;
                break;
            }
        }

        if (found==true) {
            tabNya.setSelected(true);
        } else {
            Tab newTab = new Tab();
                newTab.setId(tabPrefixMenuId + idNya);
                newTab.setLabel(lblNya);
                newTab.setSelected(true);
                newTab.setClosable(closeable);
                tabsNya.appendChild(newTab);
            Tabpanel newTabpanel = new Tabpanel();
                newTabpanel.setId(tabpanelPrefixMenuId + idNya);

                //-->Dengan Cara Include :
                //=============================================
                Include incNya = new Include(srcNya + ".zul");
                incNya.setClass("baseInclude");
                incNya.setMode("instant");
                newTabpanel.appendChild(incNya);
                newTabpanel.setClass("baseTabPanelCenter");
                tabpanelsNya.appendChild(newTabpanel);

                //-->Dengan Cara Executions.createComponents() :
                //=============================================
                //newTabpanel.setClass("baseTabPanelCenter");
                //tabpanelsNya.appendChild(newTabpanel);
                //Executions.createComponents(srcNya + ".zul", newTabpanel, null);


                /*newTabpanel.addEventListener("onSize", new EventListener() {
                    @Override
                    public void onEvent(Event event) throws Exception {
                        System.out.println("onClientInfo.................................");
                    }
                });*/
                /*<attribute name="onClientInfo"><![CDATA[
                    Desktop desktopNya = Executions.getCurrent().getDesktop();
                    Window windowNya = desktopNya.getPage("mainPage").getFellow("mainWindow");
                    windowNya.setTitle("YYUTDSYTSYUTSYUD");
                ]]></attribute>*/
        }

        //Contoh retrieve Object murni berdasarkan IDnya (beserta attributnya komplit);
        //=============================================================================
        /*cmenuNya = new Cmenu();
        cmenuNya = cmenuService.getById("MNU201404030601");
        System.out.println("###### ---> " + cmenuNya.getCmenuLabel());*/

        //Contoh retrieve Object berdasarkan kriteria tertentu (RequestMap)
        //=============================================================================
        /*Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("cmenuId", "MNU201404030601");
        requestMap.put("cmenuLabel", "Dashboard");
        List<Cmenu> listNyaCMenu = cmenuService.getByRequest(requestMap, false, null);
        System.out.println("###### ---> " + listNyaCMenu.size());*/
    }

/*************************************************************************************
 * Event dan Listener (Diawali dengan "on" / Fungsinya sama dengan Do's, yaitu Command)
 **************************************************************************************/


/*************************************************************************************
 * Custom Methods
 **************************************************************************************/
    private void createDashboard() {
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("null", "null");
        String[] whereArgs = {" AND cmenuParentId IS NULL AND cmenuSeq = 0"};
        List<Cmenu> listNyaCMenu = cmenuService.getByRequest(requestMap, false, whereArgs);

        for (int i=0; i<listNyaCMenu.size(); i++) {
            Cmenu cmenuNya = listNyaCMenu.get(i);
            createMenuItems(menubarNorthNya, cmenuNya);
            doCreateTab(cmenuNya.getCmenuId(), cmenuNya.getCmenuSrc(), cmenuNya.getCmenuLabel(), cmenuNya.getCmenuCloseable());
        }
    }

    private void createMenu() {
        createMenuOrMenuItem();
    }

    private void createMenuOrMenuItem() {
        Menuseparator ms;

        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("null", "null");
        String[] whereArgs = {" AND cmenuParentId IS NULL AND cmenuSeq > 0 "};
        List<Cmenu> listNyaCMenu = cmenuService.getByRequest(requestMap, false, whereArgs);

        if (listNyaCMenu.size()>0) {
            ms = new Menuseparator();
            menubarNorthNya.appendChild((Component) ms);
        }

        for (int i=0; i<listNyaCMenu.size(); i++) {
            Cmenu cmenuNya = listNyaCMenu.get(i);

            //Cari untuk menu root ini, apakah ada submenu-nya, jika :
            //1. Ada, maka dia jadi Menu
            //2. Tidak ada, maka dia jadi Menuitem
            //--------------------------------------------------------
            Map<String, String> requestMap_01 = new HashMap<String, String>();
            requestMap_01.put("cmenuParentId", cmenuNya.getCmenuId());
            List<Cmenu> listNyaCMenu_01 = cmenuService.getByRequest(requestMap_01, false, null);

            if (listNyaCMenu_01.size()>0) {
                createMenues(menubarNorthNya, cmenuNya);
            } else {
                createMenuItems(menubarNorthNya, cmenuNya);
            }

            if (listNyaCMenu.size()>1 && i<listNyaCMenu.size()-1) {
                ms = new Menuseparator();
                menubarNorthNya.appendChild((Component) ms);
            }
        }
    }

    private void createMenues(Menubar parentNya, Cmenu cmenuNya) {
        //Cari untuk menu ini, apakah ada submenu-nya, jika :
        //1. Ada, maka dia jadi Menu
        //2. Tidak ada, maka dia jadi Menuitem
        //--------------------------------------------------------
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("cmenuParentId", cmenuNya.getCmenuId());
        List<Cmenu> listNyaCMenu = cmenuService.getByRequest(requestMap, false, null);

        if (listNyaCMenu.size()>0) {
            //Buat element Menu
            Menu menuNya = new Menu(cmenuNya.getCmenuLabel());
            parentNya.appendChild(menuNya);
            //Buat element Menupopup
            Menupopup menuPopupNya = new Menupopup();
            menuNya.appendChild(menuPopupNya);

            for (int i=0; i<listNyaCMenu.size(); i++) {
                Cmenu new_cmenuNya = listNyaCMenu.get(i);
                if (listNyaCMenu.size()>0) {
                    createMenues(menuPopupNya, new_cmenuNya);
                } else {
                    createMenuItems(menuPopupNya, new_cmenuNya);
                }
            }

        } else {
            createMenuItems(parentNya, cmenuNya);
        }
    }

    private void createMenues(Menupopup parentNya, Cmenu cmenuNya) {
        //Cari untuk menu ini, apakah ada submenu-nya, jika :
        //1. Ada, maka dia jadi Menu
        //2. Tidak ada, maka dia jadi Menuitem
        //--------------------------------------------------------
        Map<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("cmenuParentId", cmenuNya.getCmenuId());
        List<Cmenu> listNyaCMenu = cmenuService.getByRequest(requestMap, false, null);

        if (listNyaCMenu.size()>0) {
            //Buat element Menu
            Menu menuNya = new Menu(cmenuNya.getCmenuLabel());
            parentNya.appendChild(menuNya);
            //Buat element Menupopup
            Menupopup menuPopupNya = new Menupopup();
            menuNya.appendChild(menuPopupNya);

            for (int i=0; i<listNyaCMenu.size(); i++) {
                Cmenu new_cmenuNya = listNyaCMenu.get(i);
                if (listNyaCMenu.size()>0) {
                    createMenues(menuPopupNya, new_cmenuNya);
                } else {
                    createMenuItems(menuPopupNya, new_cmenuNya);
                }
            }
        } else {
            createMenuItems(parentNya, cmenuNya);
        }
    }

    //Hanya untuk Menuitem "root".
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void createMenuItems(Menubar parentNya, final Cmenu cmenuNya) {
        Menuitem menuItemNya = new Menuitem(cmenuNya.getCmenuLabel());
        if (cmenuNya.getCmenuSrc().equals("/") == false) {
            menuItemNya.addEventListener("onClick", new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    //Merujuk ke @Command doCreateTab()
                    doCreateTab(cmenuNya.getCmenuId(), cmenuNya.getCmenuSrc(), cmenuNya.getCmenuLabel(), cmenuNya.getCmenuCloseable());
                }
            });
        }
        parentNya.appendChild(menuItemNya);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void createMenuItems(Menupopup parentNya, final Cmenu cmenuNya) {
        Menuitem menuItemNya = new Menuitem(cmenuNya.getCmenuLabel());
        if (cmenuNya.getCmenuSrc().equals("/") == false) {
            menuItemNya.addEventListener("onClick", new EventListener() {
                @Override
                public void onEvent(Event event) throws Exception {
                    //Merujuk ke @Command doCreateTab()
                    doCreateTab(cmenuNya.getCmenuId(), cmenuNya.getCmenuSrc(), cmenuNya.getCmenuLabel(), cmenuNya.getCmenuCloseable());
                }
            });
        }
        parentNya.appendChild(menuItemNya);
    }

    @SuppressWarnings("unused")
    private void createTabSessionBase() {
        //
    }

/*************************************************************************************
 * Renderer
 **************************************************************************************/
    // Untuk Wiring Component yang ada di ZUL apabila diperlukan,
    // Persiapan Wiring Component via wComSel - (DEFAULT)
    private void wiringComponent() {
        northFrameNya = (North) Path.getComponent("/mainWindow/mainFrame/northFrame");
        westFrameNya = (West) Path.getComponent("/mainWindow/mainFrame/westFrame");
        centerFrameNya = (Center) Path.getComponent("/mainWindow/mainFrame/centerFrame");
        southFrameNya = (South) Path.getComponent("/mainWindow/mainFrame/southFrame");

        for (int i=0; i<centerFrameNya.getChildren().size();) {
            //Hanya ada 1 component dalam "centerFrame"
            if (i==0) {
                centerFrameIncludeNya = (Include) centerFrameNya.getChildren().get(0);
                for (int j=0; j<centerFrameIncludeNya.getChildren().size();) {
                    if (j==0) {
                        //Ada 1 element dalam "centerFrameInclude" ini yaitu "tabboxCenter".
                        tabboxCenterNya = (Tabbox) centerFrameIncludeNya.getChildren().get(0);
                        //Ada 2 element dalam "tabboxCenter" yaitu : "tabs" dan "tabpanels"
                        for (int k=0; k<tabboxCenterNya.getChildren().size(); k++) {
                            if (k==0) {
                                tabsNya = (Tabs) tabboxCenterNya.getChildren().get(0);
                            } else if (k==1) {
                                tabpanelsNya = (Tabpanels) tabboxCenterNya.getChildren().get(1);
                            }
                        }
                    }
                    break;
                }
            }
            break;
        }

        for (int i=0; i<northFrameNya.getChildren().size();) {
            //Ada beberapa component dalam "northFrame"
            if (northFrameNya.getFellowIfAny("menubarFrameInclude", true) != null) {
                menubarFrameIncludeNya = (Include) northFrameNya.getFellow("menubarFrameInclude", true);
                for (int j=0; j<menubarFrameIncludeNya.getChildren().size();) {
                    if (j==0) {
                        //Ada 1 element dalam "menubarFrameInclude" ini yaitu "menubarNorth".
                        menubarNorthNya = (Menubar) menubarFrameIncludeNya.getChildren().get(0);
                    }
                    break;
                }

            }
            break;
        }

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

    public Cmenu getCmenuNya() {
        return cmenuNya;
    }
    public void setCmenuNya(Cmenu cmenuNya) {
        this.cmenuNya = cmenuNya;
    }

}
