package com.goldmsg.gmdoc.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Set;

/**
 * The persistent class for the t_user_info database table.
 * 
 */
@Entity
@Table(name = "t_user_info")
@NamedQuery(name = "TUserInfo.findAll", query = "SELECT t FROM TUserInfo t")
public class TUserInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private int userId;

	@Column(name = "user_code")
	private String userCode;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "security_level")
	private int secLevel;

	// bi-directional many-to-one association to RelUserColDoc
	@OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY)
	private Set<RelUserColDoc> relUserColDocs;

	// bi-directional many-to-one association to RelUserReadDoc
	@OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY)
	private Set<RelUserReadDoc> relUserReadDocs;

	// bi-directional many-to-many association to TDocInfo
	@ManyToMany
	@JoinTable(name = "rel_user_col_doc", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "doc_id") })
	private Set<TDocInfo> coleDocInfos;

	// bi-directional many-to-many association to TDocInfo
	@ManyToMany
	@JoinTable(name = "rel_user_read_doc", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "doc_id") })
	private Set<TDocInfo> readDocInfos;

	// bi-directional many-to-one association to TDistrictDict
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dist_id")
	private TDistrictDict distInfo;

	public TUserInfo() {
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Set<RelUserColDoc> getRelUserColDocs() {
		return this.relUserColDocs;
	}

	public void setRelUserColDocs(Set<RelUserColDoc> relUserColDocs) {
		this.relUserColDocs = relUserColDocs;
	}

	public RelUserColDoc addRelUserColDoc(RelUserColDoc relUserColDoc) {
		getRelUserColDocs().add(relUserColDoc);
		relUserColDoc.setUserInfo(this);

		return relUserColDoc;
	}

	public RelUserColDoc removeRelUserColDoc(RelUserColDoc relUserColDoc) {
		getRelUserColDocs().remove(relUserColDoc);
		relUserColDoc.setUserInfo(null);

		return relUserColDoc;
	}

	public Set<RelUserReadDoc> getRelUserReadDocs() {
		return this.relUserReadDocs;
	}

	public void setRelUserReadDocs(Set<RelUserReadDoc> relUserReadDocs) {
		this.relUserReadDocs = relUserReadDocs;
	}

	public RelUserReadDoc addRelUserReadDoc(RelUserReadDoc relUserReadDoc) {
		getRelUserReadDocs().add(relUserReadDoc);
		relUserReadDoc.setUserInfo(this);

		return relUserReadDoc;
	}

	public RelUserReadDoc removeRelUserReadDoc(RelUserReadDoc relUserReadDoc) {
		getRelUserReadDocs().remove(relUserReadDoc);
		relUserReadDoc.setUserInfo(null);

		return relUserReadDoc;
	}

	public TDistrictDict getDistInfo() {
		return this.distInfo;
	}

	public void setDistInfo(TDistrictDict distInfo) {
		this.distInfo = distInfo;
	}

	public Set<TDocInfo> getColeDocInfos() {
		return coleDocInfos;
	}

	public void setColeDocInfos(Set<TDocInfo> coleDocInfos) {
		this.coleDocInfos = coleDocInfos;
	}

	public Set<TDocInfo> getReadDocInfos() {
		return readDocInfos;
	}

	public void setReadDocInfos(Set<TDocInfo> readDocInfos) {
		this.readDocInfos = readDocInfos;
	}

	public int getSecLevel() {
		return secLevel;
	}

	public void setSecLevel(int secLevel) {
		this.secLevel = secLevel;
	}
}