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
 * The persistent class for the cuser_role database table.
 *
 */
@Entity
@Table(name="cuser_role", schema="core")
public class CuserRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="cuser_role_id", unique=true, nullable=false, length=15)
    private String cuserRoleId;

    @Column(name="cuser_role_deleteable", nullable=false)
    private Boolean cuserRoleDeleteable;

    @Column(name="cuser_role_deleteby", length=15)
    private String cuserRoleDeleteby;

    @Column(name="cuser_role_deleteon")
    private Timestamp cuserRoleDeleteon;

    @Column(name="cuser_role_inputby", length=15)
    private String cuserRoleInputby;

    @Column(name="cuser_role_inputon")
    private Timestamp cuserRoleInputon;

    @Column(name="cuser_role_updateby", length=15)
    private String cuserRoleUpdateby;

    @Column(name="cuser_role_updateon")
    private Timestamp cuserRoleUpdateon;

    //bi-directional many-to-one association to Crole
    @ManyToOne
    @JoinColumn(name="crole_id", nullable=false)
    private Crole crole;

    //bi-directional many-to-one association to Cuser
    @ManyToOne
    @JoinColumn(name="cuser_id", nullable=false)
    private Cuser cuser;

    public CuserRole() {
    }

    public String getCuserRoleId() {
        return this.cuserRoleId;
    }

    public void setCuserRoleId(String cuserRoleId) {
        this.cuserRoleId = cuserRoleId;
    }

    public Boolean getCuserRoleDeleteable() {
        return this.cuserRoleDeleteable;
    }

    public void setCuserRoleDeleteable(Boolean cuserRoleDeleteable) {
        this.cuserRoleDeleteable = cuserRoleDeleteable;
    }

    public String getCuserRoleDeleteby() {
        return this.cuserRoleDeleteby;
    }

    public void setCuserRoleDeleteby(String cuserRoleDeleteby) {
        this.cuserRoleDeleteby = cuserRoleDeleteby;
    }

    public Timestamp getCuserRoleDeleteon() {
        return this.cuserRoleDeleteon;
    }

    public void setCuserRoleDeleteon(Timestamp cuserRoleDeleteon) {
        this.cuserRoleDeleteon = cuserRoleDeleteon;
    }

    public String getCuserRoleInputby() {
        return this.cuserRoleInputby;
    }

    public void setCuserRoleInputby(String cuserRoleInputby) {
        this.cuserRoleInputby = cuserRoleInputby;
    }

    public Timestamp getCuserRoleInputon() {
        return this.cuserRoleInputon;
    }

    public void setCuserRoleInputon(Timestamp cuserRoleInputon) {
        this.cuserRoleInputon = cuserRoleInputon;
    }

    public String getCuserRoleUpdateby() {
        return this.cuserRoleUpdateby;
    }

    public void setCuserRoleUpdateby(String cuserRoleUpdateby) {
        this.cuserRoleUpdateby = cuserRoleUpdateby;
    }

    public Timestamp getCuserRoleUpdateon() {
        return this.cuserRoleUpdateon;
    }

    public void setCuserRoleUpdateon(Timestamp cuserRoleUpdateon) {
        this.cuserRoleUpdateon = cuserRoleUpdateon;
    }

    public Crole getCrole() {
        return this.crole;
    }

    public void setCrole(Crole crole) {
        this.crole = crole;
    }

    public Cuser getCuser() {
        return this.cuser;
    }

    public void setCuser(Cuser cuser) {
        this.cuser = cuser;
    }

}
