package com.wd.basezk.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the cuser database table.
 *
 */
@Entity
@Table(name="cuser", schema="core")
public class Cuser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="cuser_id", unique=true, nullable=false, length=15)
    private String cuserId;

    @Column(name="cuser_active", nullable=false)
    private Boolean cuserActive;

    @Column(name="cuser_deleteable", nullable=false)
    private Boolean cuserDeleteable;

    @Column(name="cuser_deleteby", length=15)
    private String cuserDeleteby;

    @Column(name="cuser_deleteon")
    private Timestamp cuserDeleteon;

    @Column(name="cuser_desc", length=100)
    private String cuserDesc;

    @Column(name="cuser_email", nullable=false, length=100)
    private String cuserEmail;

    @Column(name="cuser_hint_a", length=100)
    private String cuserHintA;

    @Column(name="cuser_hint_q", length=100)
    private String cuserHintQ;

    @Column(name="cuser_inputby", length=15)
    private String cuserInputby;

    @Column(name="cuser_inputon")
    private Timestamp cuserInputon;

    @Column(name="cuser_password", nullable=false, length=30)
    private String cuserPassword;

    @Column(name="cuser_updateby", length=15)
    private String cuserUpdateby;

    @Column(name="cuser_updateon")
    private Timestamp cuserUpdateon;

    @Column(name="cuser_username", nullable=false, length=30)
    private String cuserUsername;

    //bi-directional many-to-one association to CuserRole
    @OneToMany(mappedBy="cuser", fetch=FetchType.EAGER)
    private Set<CuserRole> cuserRoles;

    public Cuser() {
    }

    public String getCuserId() {
        return this.cuserId;
    }

    public void setCuserId(String cuserId) {
        this.cuserId = cuserId;
    }

    public Boolean getCuserActive() {
        return this.cuserActive;
    }

    public void setCuserActive(Boolean cuserActive) {
        this.cuserActive = cuserActive;
    }

    public Boolean getCuserDeleteable() {
        return this.cuserDeleteable;
    }

    public void setCuserDeleteable(Boolean cuserDeleteable) {
        this.cuserDeleteable = cuserDeleteable;
    }

    public String getCuserDeleteby() {
        return this.cuserDeleteby;
    }

    public void setCuserDeleteby(String cuserDeleteby) {
        this.cuserDeleteby = cuserDeleteby;
    }

    public Timestamp getCuserDeleteon() {
        return this.cuserDeleteon;
    }

    public void setCuserDeleteon(Timestamp cuserDeleteon) {
        this.cuserDeleteon = cuserDeleteon;
    }

    public String getCuserDesc() {
        return this.cuserDesc;
    }

    public void setCuserDesc(String cuserDesc) {
        this.cuserDesc = cuserDesc;
    }

    public String getCuserEmail() {
        return this.cuserEmail;
    }

    public void setCuserEmail(String cuserEmail) {
        this.cuserEmail = cuserEmail;
    }

    public String getCuserHintA() {
        return this.cuserHintA;
    }

    public void setCuserHintA(String cuserHintA) {
        this.cuserHintA = cuserHintA;
    }

    public String getCuserHintQ() {
        return this.cuserHintQ;
    }

    public void setCuserHintQ(String cuserHintQ) {
        this.cuserHintQ = cuserHintQ;
    }

    public String getCuserInputby() {
        return this.cuserInputby;
    }

    public void setCuserInputby(String cuserInputby) {
        this.cuserInputby = cuserInputby;
    }

    public Timestamp getCuserInputon() {
        return this.cuserInputon;
    }

    public void setCuserInputon(Timestamp cuserInputon) {
        this.cuserInputon = cuserInputon;
    }

    public String getCuserPassword() {
        return this.cuserPassword;
    }

    public void setCuserPassword(String cuserPassword) {
        this.cuserPassword = cuserPassword;
    }

    public String getCuserUpdateby() {
        return this.cuserUpdateby;
    }

    public void setCuserUpdateby(String cuserUpdateby) {
        this.cuserUpdateby = cuserUpdateby;
    }

    public Timestamp getCuserUpdateon() {
        return this.cuserUpdateon;
    }

    public void setCuserUpdateon(Timestamp cuserUpdateon) {
        this.cuserUpdateon = cuserUpdateon;
    }

    public String getCuserUsername() {
        return this.cuserUsername;
    }

    public void setCuserUsername(String cuserUsername) {
        this.cuserUsername = cuserUsername;
    }

    public Set<CuserRole> getCuserRoles() {
        return this.cuserRoles;
    }

    public void setCuserRoles(Set<CuserRole> cuserRoles) {
        this.cuserRoles = cuserRoles;
    }

    public CuserRole addCuserRole(CuserRole cuserRole) {
        getCuserRoles().add(cuserRole);
        cuserRole.setCuser(this);

        return cuserRole;
    }

    public CuserRole removeCuserRole(CuserRole cuserRole) {
        getCuserRoles().remove(cuserRole);
        cuserRole.setCuser(null);

        return cuserRole;
    }

}
