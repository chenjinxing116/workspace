package com.goldmsg.gmdoc.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;

/**
 * The persistent class for the t_catogory database table.
 * 
 */
@Entity
@Table(name = "t_catogory")
@NamedQuery(name = "TCatogory.findAll", query = "SELECT t FROM TCatogory t")
public class TCatogory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cato_id")
	private int catoId;

	@Column(name = "cato_code")
	private String catoCode;

	@Column(name = "cato_name")
	private String catoName;

	@Column(name = "cato_status")
	private String catoStatus;

	@Column(name = "parent_id")
	private int parentId;

	// bi-directional many-to-many association to TDistrictDict
	@ManyToMany(mappedBy = "catoInfos", fetch = FetchType.LAZY)
	private Set<TDistrictDict> distInfos;

	// bi-directional many-to-one association to TDocInfo
	@OneToMany(mappedBy = "catoInfo", fetch = FetchType.LAZY)
	private Set<TDocInfo> docInfos;

	public TCatogory() {
	}

	public int getCatoId() {
		return this.catoId;
	}

	public void setCatoId(int catoId) {
		this.catoId = catoId;
	}

	public String getCatoCode() {
		return this.catoCode;
	}

	public void setCatoCode(String catoCode) {
		this.catoCode = catoCode;
	}

	public String getCatoName() {
		return this.catoName;
	}

	public void setCatoName(String catoName) {
		this.catoName = catoName;
	}

	public String getCatoStatus() {
		return this.catoStatus;
	}

	public void setCatoStatus(String catoStatus) {
		this.catoStatus = catoStatus;
	}

	public int getParentId() {
		return this.parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public Set<TDistrictDict> getDistInfos() {
		return this.distInfos;
	}

	public void setDistInfos(Set<TDistrictDict> distInfos) {
		this.distInfos = distInfos;
	}

	public Set<TDocInfo> getDocInfos() {
		return this.docInfos;
	}

	public void setDocInfos(Set<TDocInfo> docInfos) {
		this.docInfos = docInfos;
	}

	public TDocInfo addDocInfo(TDocInfo docInfo) {
		getDocInfos().add(docInfo);
		docInfo.setCatoInfo(this);
		return docInfo;
	}

	public TDocInfo removeDocInfo(TDocInfo docInfo) {
		getDocInfos().remove(docInfo);
		docInfo.setCatoInfo(null);
		return docInfo;
	}

}