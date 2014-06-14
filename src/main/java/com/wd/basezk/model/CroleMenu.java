package com.wd.basezk.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the crole_menu database table.
 *
 */
@Entity
@Table(name="crole_menu", schema="core")
public class CroleMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="crole_menu_id", unique=true, nullable=false, length=15)
    private String croleMenuId;

    @Column(name="crole_menu_deleteable", nullable=false)
    private Boolean croleMenuDeleteable;

    @Column(name="crole_menu_deleteby", length=15)
    private String croleMenuDeleteby;

    @Column(name="crole_menu_deleteon")
    private Timestamp croleMenuDeleteon;

    @Column(name="crole_menu_desc", length=100)
    private String croleMenuDesc;

    @Column(name="crole_menu_inputby", length=15)
    private String croleMenuInputby;

    @Column(name="crole_menu_inputon")
    private Timestamp croleMenuInputon;

    @Column(name="crole_menu_updateby", length=15)
    private String croleMenuUpdateby;

    @Column(name="crole_menu_updateon")
    private Timestamp croleMenuUpdateon;

    //bi-directional many-to-one association to Cmenu
    @ManyToOne
    @JoinColumn(name="cmenu_id", nullable=false)
    private Cmenu cmenu;

    //bi-directional many-to-one association to Crole
    @ManyToOne
    @JoinColumn(name="crole_id", nullable=false)
    private Crole crole;

    public CroleMenu() {
    }

    public String getCroleMenuId() {
        return this.croleMenuId;
    }

    public void setCroleMenuId(String croleMenuId) {
        this.croleMenuId = croleMenuId;
    }

    public Boolean getCroleMenuDeleteable() {
        return this.croleMenuDeleteable;
    }

    public void setCroleMenuDeleteable(Boolean croleMenuDeleteable) {
        this.croleMenuDeleteable = croleMenuDeleteable;
    }

    public String getCroleMenuDeleteby() {
        return this.croleMenuDeleteby;
    }

    public void setCroleMenuDeleteby(String croleMenuDeleteby) {
        this.croleMenuDeleteby = croleMenuDeleteby;
    }

    public Timestamp getCroleMenuDeleteon() {
        return this.croleMenuDeleteon;
    }

    public void setCroleMenuDeleteon(Timestamp croleMenuDeleteon) {
        this.croleMenuDeleteon = croleMenuDeleteon;
    }

    public String getCroleMenuDesc() {
        return this.croleMenuDesc;
    }

    public void setCroleMenuDesc(String croleMenuDesc) {
        this.croleMenuDesc = croleMenuDesc;
    }

    public String getCroleMenuInputby() {
        return this.croleMenuInputby;
    }

    public void setCroleMenuInputby(String croleMenuInputby) {
        this.croleMenuInputby = croleMenuInputby;
    }

    public Timestamp getCroleMenuInputon() {
        return this.croleMenuInputon;
    }

    public void setCroleMenuInputon(Timestamp croleMenuInputon) {
        this.croleMenuInputon = croleMenuInputon;
    }

    public String getCroleMenuUpdateby() {
        return this.croleMenuUpdateby;
    }

    public void setCroleMenuUpdateby(String croleMenuUpdateby) {
        this.croleMenuUpdateby = croleMenuUpdateby;
    }

    public Timestamp getCroleMenuUpdateon() {
        return this.croleMenuUpdateon;
    }

    public void setCroleMenuUpdateon(Timestamp croleMenuUpdateon) {
        this.croleMenuUpdateon = croleMenuUpdateon;
    }

    public Cmenu getCmenu() {
        return this.cmenu;
    }

    public void setCmenu(Cmenu cmenu) {
        this.cmenu = cmenu;
    }

    public Crole getCrole() {
        return this.crole;
    }

    public void setCrole(Crole crole) {
        this.crole = crole;
    }

}
