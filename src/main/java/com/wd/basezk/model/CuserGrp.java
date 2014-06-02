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
 * The persistent class for the cuser_grp database table.
 *
 */
@Entity
@Table(name="cuser_grp", schema="core")
public class CuserGrp implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="cuser_grp_id", unique=true, nullable=false, length=15)
    private String cuserGrpId;

    @Column(name="cuser_grp_deleteable", nullable=false)
    private Boolean cuserGrpDeleteable;

    @Column(name="cuser_grp_deleteby", length=15)
    private String cuserGrpDeleteby;

    @Column(name="cuser_grp_deleteon")
    private Timestamp cuserGrpDeleteon;

    @Column(name="cuser_grp_desc", length=100)
    private String cuserGrpDesc;

    @Column(name="cuser_grp_inputby", length=15)
    private String cuserGrpInputby;

    @Column(name="cuser_grp_inputon")
    private Timestamp cuserGrpInputon;

    @Column(name="cuser_grp_name", nullable=false, length=30)
    private String cuserGrpName;

    @Column(name="cuser_grp_updateby", length=15)
    private String cuserGrpUpdateby;

    @Column(name="cuser_grp_updateon")
    private Timestamp cuserGrpUpdateon;

    //bi-directional many-to-one association to Cuser
    @OneToMany(mappedBy="cuserGrp")
    private Set<Cuser> cusers;

    public CuserGrp() {
    }

    public String getCuserGrpId() {
        return this.cuserGrpId;
    }

    public void setCuserGrpId(String cuserGrpId) {
        this.cuserGrpId = cuserGrpId;
    }

    public Boolean getCuserGrpDeleteable() {
        return this.cuserGrpDeleteable;
    }

    public void setCuserGrpDeleteable(Boolean cuserGrpDeleteable) {
        this.cuserGrpDeleteable = cuserGrpDeleteable;
    }

    public String getCuserGrpDeleteby() {
        return this.cuserGrpDeleteby;
    }

    public void setCuserGrpDeleteby(String cuserGrpDeleteby) {
        this.cuserGrpDeleteby = cuserGrpDeleteby;
    }

    public Timestamp getCuserGrpDeleteon() {
        return this.cuserGrpDeleteon;
    }

    public void setCuserGrpDeleteon(Timestamp cuserGrpDeleteon) {
        this.cuserGrpDeleteon = cuserGrpDeleteon;
    }

    public String getCuserGrpDesc() {
        return this.cuserGrpDesc;
    }

    public void setCuserGrpDesc(String cuserGrpDesc) {
        this.cuserGrpDesc = cuserGrpDesc;
    }

    public String getCuserGrpInputby() {
        return this.cuserGrpInputby;
    }

    public void setCuserGrpInputby(String cuserGrpInputby) {
        this.cuserGrpInputby = cuserGrpInputby;
    }

    public Timestamp getCuserGrpInputon() {
        return this.cuserGrpInputon;
    }

    public void setCuserGrpInputon(Timestamp cuserGrpInputon) {
        this.cuserGrpInputon = cuserGrpInputon;
    }

    public String getCuserGrpName() {
        return this.cuserGrpName;
    }

    public void setCuserGrpName(String cuserGrpName) {
        this.cuserGrpName = cuserGrpName;
    }

    public String getCuserGrpUpdateby() {
        return this.cuserGrpUpdateby;
    }

    public void setCuserGrpUpdateby(String cuserGrpUpdateby) {
        this.cuserGrpUpdateby = cuserGrpUpdateby;
    }

    public Timestamp getCuserGrpUpdateon() {
        return this.cuserGrpUpdateon;
    }

    public void setCuserGrpUpdateon(Timestamp cuserGrpUpdateon) {
        this.cuserGrpUpdateon = cuserGrpUpdateon;
    }

    public Set<Cuser> getCusers() {
        return this.cusers;
    }

    public void setCusers(Set<Cuser> cusers) {
        this.cusers = cusers;
    }

    public Cuser addCuser(Cuser cuser) {
        getCusers().add(cuser);
        cuser.setCuserGrp(this);

        return cuser;
    }

    public Cuser removeCuser(Cuser cuser) {
        getCusers().remove(cuser);
        cuser.setCuserGrp(null);

        return cuser;
    }

}
