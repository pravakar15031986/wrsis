package com.csmpl.wrsis.webportal.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="m_adm_leveldetails")
public class OrganizationMasterEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="intleveldetailid")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int intleveldetailid;
	
	@Column(name="nvchlevelname")
	public String nvchlevelname;
	
	@Column(name="intlevelid")
	public int intlevelid;
	
	@Column(name="intparentid")
	public int intparentid;
	
	@Column(name="vchalias")
	public String vchalias;
	
	@Column(name="vchtelno")
	public String vchtelno;
	
	@Column(name="vchfaxno")
	public String vchfaxno;
	
	@Column(name="bitstatus")
	public boolean bitstatus;
	
	@Column(name="vchaddress")
	public String vchaddress;
	
	@Column(name="inthierarchyid")
	public int inthierarchyid;
	
	@Column(name="intcreatedby")
	public int intcreatedby;
	
	@Column(name="dtmcreatedon")
	public Date dtmcreatedon;
	
	@Column(name="intupdatedby")
	public int intupdatedby;
	
	@Column(name="dtmupdatedon")
	public Date dtmupdatedon;

	public int getIntleveldetailid() {
		return intleveldetailid;
	}

	public void setIntleveldetailid(int intleveldetailid) {
		this.intleveldetailid = intleveldetailid;
	}

	public String getNvchlevelname() {
		return nvchlevelname;
	}

	public void setNvchlevelname(String nvchlevelname) {
		this.nvchlevelname = nvchlevelname;
	}

	public int getIntlevelid() {
		return intlevelid;
	}

	public void setIntlevelid(int intlevelid) {
		this.intlevelid = intlevelid;
	}

	public int getIntparentid() {
		return intparentid;
	}

	public void setIntparentid(int intparentid) {
		this.intparentid = intparentid;
	}

	public String getVchalias() {
		return vchalias;
	}

	public void setVchalias(String vchalias) {
		this.vchalias = vchalias;
	}

	public String getVchtelno() {
		return vchtelno;
	}

	public void setVchtelno(String vchtelno) {
		this.vchtelno = vchtelno;
	}

	public String getVchfaxno() {
		return vchfaxno;
	}

	public void setVchfaxno(String vchfaxno) {
		this.vchfaxno = vchfaxno;
	}

	public boolean isBitstatus() {
		return bitstatus;
	}

	public void setBitstatus(boolean bitstatus) {
		this.bitstatus = bitstatus;
	}

	public String getVchaddress() {
		return vchaddress;
	}

	public void setVchaddress(String vchaddress) {
		this.vchaddress = vchaddress;
	}

	public int getInthierarchyid() {
		return inthierarchyid;
	}

	public void setInthierarchyid(int inthierarchyid) {
		this.inthierarchyid = inthierarchyid;
	}

	public int getIntcreatedby() {
		return intcreatedby;
	}

	public void setIntcreatedby(int intcreatedby) {
		this.intcreatedby = intcreatedby;
	}

	public Date getDtmcreatedon() {
		return dtmcreatedon;
	}

	public void setDtmcreatedon(Date dtmcreatedon) {
		this.dtmcreatedon = dtmcreatedon;
	}

	public int getIntupdatedby() {
		return intupdatedby;
	}

	public void setIntupdatedby(int intupdatedby) {
		this.intupdatedby = intupdatedby;
	}

	public Date getDtmupdatedon() {
		return dtmupdatedon;
	}

	public void setDtmupdatedon(Date dtmupdatedon) {
		this.dtmupdatedon = dtmupdatedon;
	}

	@Override
	public String toString() {
		return "OrganizationMasterEntity [intleveldetailid=" + intleveldetailid + ", nvchlevelname=" + nvchlevelname
				+ ", intlevelid=" + intlevelid + ", intparentid=" + intparentid + ", vchalias=" + vchalias
				+ ", vchtelno=" + vchtelno + ", vchfaxno=" + vchfaxno + ", bitstatus=" + bitstatus + ", vchaddress="
				+ vchaddress + ", inthierarchyid=" + inthierarchyid + ", intcreatedby=" + intcreatedby
				+ ", dtmcreatedon=" + dtmcreatedon + ", intupdatedby=" + intupdatedby + ", dtmupdatedon=" + dtmupdatedon
				+ "]";
	}
	
}
