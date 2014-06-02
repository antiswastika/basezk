package com.wd.basezk.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the cmenu database table.
 *
 */
@Entity
@Table(name="cmenu", schema="core")
public class Cmenu implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="cmenu_id", unique=true, nullable=false, length=15)
    private String cmenuId;

    @Column(name="cmenu_closeable", nullable=false)
    private Boolean cmenuCloseable;

    @Column(name="cmenu_deleteable", nullable=false)
    private Boolean cmenuDeleteable;

    @Column(name="cmenu_desc", length=100)
    private String cmenuDesc;

    @Column(name="cmenu_icon_menu", length=30)
    private String cmenuIconMenu;

    @Column(name="cmenu_icon_shortcut", length=30)
    private String cmenuIconShortcut;

    @Column(name="cmenu_inputby", length=15)
    private String cmenuInputby;

    @Column(name="cmenu_inputon")
    private Timestamp cmenuInputon;

    @Column(name="cmenu_is_create_shortcut", nullable=false)
    private Boolean cmenuIsCreateShortcut;

    @Column(name="cmenu_is_popup", nullable=false)
    private Boolean cmenuIsPopup;

    @Column(name="cmenu_is_tab", nullable=false)
    private Boolean cmenuIsTab;

    @Column(name="cmenu_label", nullable=false, length=30)
    private String cmenuLabel;

    @Column(name="cmenu_parent_id", length=15)
    private String cmenuParentId;

    @Column(name="cmenu_popup_height", nullable=false)
    private Integer cmenuPopupHeight;

    @Column(name="cmenu_popup_is_resizeable", nullable=false)
    private Boolean cmenuPopupIsResizeable;

    @Column(name="cmenu_popup_width", nullable=false)
    private Integer cmenuPopupWidth;

    @Column(name="cmenu_seq")
    private Integer cmenuSeq;

    @Column(name="cmenu_src", length=100)
    private String cmenuSrc;

    @Column(name="cmenu_toolbar", length=30)
    private String cmenuToolbar;

    @Column(name="cmenu_updateby", length=15)
    private String cmenuUpdateby;

    @Column(name="cmenu_updateon")
    private Timestamp cmenuUpdateon;

    //bi-directional many-to-one association to CroleMenu
    @OneToMany(mappedBy="cmenu")
    private Set<CroleMenu> croleMenus;

    public Cmenu() {
    }

    public String getCmenuId() {
        return this.cmenuId;
    }

    public void setCmenuId(String cmenuId) {
        this.cmenuId = cmenuId;
    }

    public Boolean getCmenuCloseable() {
        return this.cmenuCloseable;
    }

    public void setCmenuCloseable(Boolean cmenuCloseable) {
        this.cmenuCloseable = cmenuCloseable;
    }

    public Boolean getCmenuDeleteable() {
        return this.cmenuDeleteable;
    }

    public void setCmenuDeleteable(Boolean cmenuDeleteable) {
        this.cmenuDeleteable = cmenuDeleteable;
    }

    public String getCmenuDesc() {
        return this.cmenuDesc;
    }

    public void setCmenuDesc(String cmenuDesc) {
        this.cmenuDesc = cmenuDesc;
    }

    public String getCmenuIconMenu() {
        return this.cmenuIconMenu;
    }

    public void setCmenuIconMenu(String cmenuIconMenu) {
        this.cmenuIconMenu = cmenuIconMenu;
    }

    public String getCmenuIconShortcut() {
        return this.cmenuIconShortcut;
    }

    public void setCmenuIconShortcut(String cmenuIconShortcut) {
        this.cmenuIconShortcut = cmenuIconShortcut;
    }

    public String getCmenuInputby() {
        return this.cmenuInputby;
    }

    public void setCmenuInputby(String cmenuInputby) {
        this.cmenuInputby = cmenuInputby;
    }

    public Timestamp getCmenuInputon() {
        return this.cmenuInputon;
    }

    public void setCmenuInputon(Timestamp cmenuInputon) {
        this.cmenuInputon = cmenuInputon;
    }

    public Boolean getCmenuIsCreateShortcut() {
        return this.cmenuIsCreateShortcut;
    }

    public void setCmenuIsCreateShortcut(Boolean cmenuIsCreateShortcut) {
        this.cmenuIsCreateShortcut = cmenuIsCreateShortcut;
    }

    public Boolean getCmenuIsPopup() {
        return this.cmenuIsPopup;
    }

    public void setCmenuIsPopup(Boolean cmenuIsPopup) {
        this.cmenuIsPopup = cmenuIsPopup;
    }

    public Boolean getCmenuIsTab() {
        return this.cmenuIsTab;
    }

    public void setCmenuIsTab(Boolean cmenuIsTab) {
        this.cmenuIsTab = cmenuIsTab;
    }

    public String getCmenuLabel() {
        return this.cmenuLabel;
    }

    public void setCmenuLabel(String cmenuLabel) {
        this.cmenuLabel = cmenuLabel;
    }

    public String getCmenuParentId() {
        return this.cmenuParentId;
    }

    public void setCmenuParentId(String cmenuParentId) {
        this.cmenuParentId = cmenuParentId;
    }

    public Integer getCmenuPopupHeight() {
        return this.cmenuPopupHeight;
    }

    public void setCmenuPopupHeight(Integer cmenuPopupHeight) {
        this.cmenuPopupHeight = cmenuPopupHeight;
    }

    public Boolean getCmenuPopupIsResizeable() {
        return this.cmenuPopupIsResizeable;
    }

    public void setCmenuPopupIsResizeable(Boolean cmenuPopupIsResizeable) {
        this.cmenuPopupIsResizeable = cmenuPopupIsResizeable;
    }

    public Integer getCmenuPopupWidth() {
        return this.cmenuPopupWidth;
    }

    public void setCmenuPopupWidth(Integer cmenuPopupWidth) {
        this.cmenuPopupWidth = cmenuPopupWidth;
    }

    public Integer getCmenuSeq() {
        return this.cmenuSeq;
    }

    public void setCmenuSeq(Integer cmenuSeq) {
        this.cmenuSeq = cmenuSeq;
    }

    public String getCmenuSrc() {
        return this.cmenuSrc;
    }

    public void setCmenuSrc(String cmenuSrc) {
        this.cmenuSrc = cmenuSrc;
    }

    public String getCmenuToolbar() {
        return this.cmenuToolbar;
    }

    public void setCmenuToolbar(String cmenuToolbar) {
        this.cmenuToolbar = cmenuToolbar;
    }

    public String getCmenuUpdateby() {
        return this.cmenuUpdateby;
    }

    public void setCmenuUpdateby(String cmenuUpdateby) {
        this.cmenuUpdateby = cmenuUpdateby;
    }

    public Timestamp getCmenuUpdateon() {
        return this.cmenuUpdateon;
    }

    public void setCmenuUpdateon(Timestamp cmenuUpdateon) {
        this.cmenuUpdateon = cmenuUpdateon;
    }

    public Set<CroleMenu> getCroleMenus() {
        return this.croleMenus;
    }

    public void setCroleMenus(Set<CroleMenu> croleMenus) {
        this.croleMenus = croleMenus;
    }

    public CroleMenu addCroleMenus(CroleMenu croleMenus) {
        getCroleMenus().add(croleMenus);
        croleMenus.setCmenu(this);

        return croleMenus;
    }

    public CroleMenu removeCroleMenus(CroleMenu croleMenus) {
        getCroleMenus().remove(croleMenus);
        croleMenus.setCmenu(null);

        return croleMenus;
    }

}
