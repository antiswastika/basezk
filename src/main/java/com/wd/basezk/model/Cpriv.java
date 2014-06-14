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
 * The persistent class for the cpriv database table.
 *
 */
@Entity
@Table(name="cpriv", schema="core")
public class Cpriv implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="cpriv_id", unique=true, nullable=false, length=15)
    private String cprivId;

    @Column(name="cpriv_delete", nullable=false)
    private Boolean cprivDelete;

    @Column(name="cpriv_delete_all", nullable=false)
    private Boolean cprivDeleteAll;

    @Column(name="cpriv_deleteable", nullable=false)
    private Boolean cprivDeleteable;

    @Column(name="cpriv_deleteby", length=15)
    private String cprivDeleteby;

    @Column(name="cpriv_deleteon")
    private Timestamp cprivDeleteon;

    @Column(name="cpriv_desc", length=255)
    private String cprivDesc;

    @Column(name="cpriv_inputby", length=15)
    private String cprivInputby;

    @Column(name="cpriv_inputon")
    private Timestamp cprivInputon;

    @Column(name="cpriv_insert", nullable=false)
    private Boolean cprivInsert;

    @Column(name="cpriv_print", nullable=false)
    private Boolean cprivPrint;

    @Column(name="cpriv_update", nullable=false)
    private Boolean cprivUpdate;

    @Column(name="cpriv_update_all", nullable=false)
    private Boolean cprivUpdateAll;

    @Column(name="cpriv_updateby", length=15)
    private String cprivUpdateby;

    @Column(name="cpriv_updateon")
    private Timestamp cprivUpdateon;

    @Column(name="cpriv_view_all", nullable=false)
    private Boolean cprivViewAll;

    //bi-directional many-to-one association to Cmodule
    @ManyToOne
    @JoinColumn(name="cmodule_id", nullable=false)
    private Cmodule cmodule;

    //bi-directional many-to-one association to Crole
    @ManyToOne
    @JoinColumn(name="crole_id", nullable=false)
    private Crole crole;

    public Cpriv() {
    }

    public String getCprivId() {
        return this.cprivId;
    }

    public void setCprivId(String cprivId) {
        this.cprivId = cprivId;
    }

    public Boolean getCprivDelete() {
        return this.cprivDelete;
    }

    public void setCprivDelete(Boolean cprivDelete) {
        this.cprivDelete = cprivDelete;
    }

    public Boolean getCprivDeleteAll() {
        return this.cprivDeleteAll;
    }

    public void setCprivDeleteAll(Boolean cprivDeleteAll) {
        this.cprivDeleteAll = cprivDeleteAll;
    }

    public Boolean getCprivDeleteable() {
        return this.cprivDeleteable;
    }

    public void setCprivDeleteable(Boolean cprivDeleteable) {
        this.cprivDeleteable = cprivDeleteable;
    }

    public String getCprivDeleteby() {
        return this.cprivDeleteby;
    }

    public void setCprivDeleteby(String cprivDeleteby) {
        this.cprivDeleteby = cprivDeleteby;
    }

    public Timestamp getCprivDeleteon() {
        return this.cprivDeleteon;
    }

    public void setCprivDeleteon(Timestamp cprivDeleteon) {
        this.cprivDeleteon = cprivDeleteon;
    }

    public String getCprivDesc() {
        return this.cprivDesc;
    }

    public void setCprivDesc(String cprivDesc) {
        this.cprivDesc = cprivDesc;
    }

    public String getCprivInputby() {
        return this.cprivInputby;
    }

    public void setCprivInputby(String cprivInputby) {
        this.cprivInputby = cprivInputby;
    }

    public Timestamp getCprivInputon() {
        return this.cprivInputon;
    }

    public void setCprivInputon(Timestamp cprivInputon) {
        this.cprivInputon = cprivInputon;
    }

    public Boolean getCprivInsert() {
        return this.cprivInsert;
    }

    public void setCprivInsert(Boolean cprivInsert) {
        this.cprivInsert = cprivInsert;
    }

    public Boolean getCprivPrint() {
        return this.cprivPrint;
    }

    public void setCprivPrint(Boolean cprivPrint) {
        this.cprivPrint = cprivPrint;
    }

    public Boolean getCprivUpdate() {
        return this.cprivUpdate;
    }

    public void setCprivUpdate(Boolean cprivUpdate) {
        this.cprivUpdate = cprivUpdate;
    }

    public Boolean getCprivUpdateAll() {
        return this.cprivUpdateAll;
    }

    public void setCprivUpdateAll(Boolean cprivUpdateAll) {
        this.cprivUpdateAll = cprivUpdateAll;
    }

    public String getCprivUpdateby() {
        return this.cprivUpdateby;
    }

    public void setCprivUpdateby(String cprivUpdateby) {
        this.cprivUpdateby = cprivUpdateby;
    }

    public Timestamp getCprivUpdateon() {
        return this.cprivUpdateon;
    }

    public void setCprivUpdateon(Timestamp cprivUpdateon) {
        this.cprivUpdateon = cprivUpdateon;
    }

    public Boolean getCprivViewAll() {
        return this.cprivViewAll;
    }

    public void setCprivViewAll(Boolean cprivViewAll) {
        this.cprivViewAll = cprivViewAll;
    }

    public Cmodule getCmodule() {
        return this.cmodule;
    }

    public void setCmodule(Cmodule cmodule) {
        this.cmodule = cmodule;
    }

    public Crole getCrole() {
        return this.crole;
    }

    public void setCrole(Crole crole) {
        this.crole = crole;
    }

}
