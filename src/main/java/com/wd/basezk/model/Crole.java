package com.wd.basezk.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the crole database table.
 *
 */
@Entity
@Table(name="crole", schema="core")
public class Crole implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="crole_id", unique=true, nullable=false, length=15)
    private String croleId;

    @Column(name="crole_deleteable", nullable=false)
    private Boolean croleDeleteable;

    @Column(name="crole_deleteby", length=15)
    private String croleDeleteby;

    @Column(name="crole_deleteon")
    private Timestamp croleDeleteon;

    @Column(name="crole_desc", length=100)
    private String croleDesc;

    @Column(name="crole_inputby", length=15)
    private String croleInputby;

    @Column(name="crole_inputon")
    private Timestamp croleInputon;

    @Column(name="crole_rolename", nullable=false, length=30)
    private String croleRolename;

    @Column(name="crole_updateby", length=15)
    private String croleUpdateby;

    @Column(name="crole_updateon")
    private Timestamp croleUpdateon;

    //bi-directional many-to-one association to Cpriv
    @OneToMany(mappedBy="crole")
    private Set<Cpriv> cprivs;

    //bi-directional many-to-one association to CroleMenu
    @OneToMany(mappedBy="crole")
    private Set<CroleMenu> croleMenus;

    //bi-directional many-to-one association to CuserRole
    @OneToMany(mappedBy="crole")
    private Set<CuserRole> cuserRoles;

    public Crole() {
    }

    public String getCroleId() {
        return this.croleId;
    }

    public void setCroleId(String croleId) {
        this.croleId = croleId;
    }

    public Boolean getCroleDeleteable() {
        return this.croleDeleteable;
    }

    public void setCroleDeleteable(Boolean croleDeleteable) {
        this.croleDeleteable = croleDeleteable;
    }

    public String getCroleDeleteby() {
        return this.croleDeleteby;
    }

    public void setCroleDeleteby(String croleDeleteby) {
        this.croleDeleteby = croleDeleteby;
    }

    public Timestamp getCroleDeleteon() {
        return this.croleDeleteon;
    }

    public void setCroleDeleteon(Timestamp croleDeleteon) {
        this.croleDeleteon = croleDeleteon;
    }

    public String getCroleDesc() {
        return this.croleDesc;
    }

    public void setCroleDesc(String croleDesc) {
        this.croleDesc = croleDesc;
    }

    public String getCroleInputby() {
        return this.croleInputby;
    }

    public void setCroleInputby(String croleInputby) {
        this.croleInputby = croleInputby;
    }

    public Timestamp getCroleInputon() {
        return this.croleInputon;
    }

    public void setCroleInputon(Timestamp croleInputon) {
        this.croleInputon = croleInputon;
    }

    public String getCroleRolename() {
        return this.croleRolename;
    }

    public void setCroleRolename(String croleRolename) {
        this.croleRolename = croleRolename;
    }

    public String getCroleUpdateby() {
        return this.croleUpdateby;
    }

    public void setCroleUpdateby(String croleUpdateby) {
        this.croleUpdateby = croleUpdateby;
    }

    public Timestamp getCroleUpdateon() {
        return this.croleUpdateon;
    }

    public void setCroleUpdateon(Timestamp croleUpdateon) {
        this.croleUpdateon = croleUpdateon;
    }

    public Set<Cpriv> getCprivs() {
        return this.cprivs;
    }

    public void setCprivs(Set<Cpriv> cprivs) {
        this.cprivs = cprivs;
    }

    public Cpriv addCpriv(Cpriv cpriv) {
        getCprivs().add(cpriv);
        cpriv.setCrole(this);

        return cpriv;
    }

    public Cpriv removeCpriv(Cpriv cpriv) {
        getCprivs().remove(cpriv);
        cpriv.setCrole(null);

        return cpriv;
    }

    public Set<CroleMenu> getCroleMenus() {
        return this.croleMenus;
    }

    public void setCroleMenus(Set<CroleMenu> croleMenus) {
        this.croleMenus = croleMenus;
    }

    public CroleMenu addCroleMenus(CroleMenu croleMenus) {
        getCroleMenus().add(croleMenus);
        croleMenus.setCrole(this);

        return croleMenus;
    }

    public CroleMenu removeCroleMenus(CroleMenu croleMenus) {
        getCroleMenus().remove(croleMenus);
        croleMenus.setCrole(null);

        return croleMenus;
    }

    public Set<CuserRole> getCuserRoles() {
        return this.cuserRoles;
    }

    public void setCuserRoles(Set<CuserRole> cuserRoles) {
        this.cuserRoles = cuserRoles;
    }

    public CuserRole addCuserRole(CuserRole cuserRole) {
        getCuserRoles().add(cuserRole);
        cuserRole.setCrole(this);

        return cuserRole;
    }

    public CuserRole removeCuserRole(CuserRole cuserRole) {
        getCuserRoles().remove(cuserRole);
        cuserRole.setCrole(null);

        return cuserRole;
    }

}
