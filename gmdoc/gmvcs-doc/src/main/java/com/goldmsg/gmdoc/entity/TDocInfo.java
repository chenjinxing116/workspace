package com.goldmsg.gmdoc.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.solr.client.solrj.beans.Field;

import java.util.Date;
import java.util.Set;

/**
 * The persistent class for the t_doc_info database table.
 * 
 */
@Entity
@Table(name = "t_doc_info")
@NamedQuery(name = "TDocInfo.findAll", query = "SELECT t FROM TDocInfo t")
public class TDocInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "doc_id")
	private int docId;

	@Column(name = "cole_times")
	private int coleTimes;

	@Field
	@Column(name = "doc_code")
	private String docCode;

	@Column(name = "doc_label")
	private String docLabel;

	@Column(name = "doc_path")
	private String docPath;

	@Field
	@Column(name = "doc_size")
	private String docSize;

	@Field
	@Column(name = "doc_cato_id", insertable = false, updatable = false)
	private int docCatoId;

	@Field
	@Column(name = "security_level")
	private int secLevel;

	@Field
	@Column(name = "doc_title")
	private String docTitle;

	@Field
	@Column(name = "doc_type")
	private String docType;

	@Column(name = "read_times")
	private int readTimes;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "upload_time")
	private Date uploadTime;

	@Field
	@Column(name = "upload_user_id")
	private int uploadUserId;

	// bi-directional many-to-one association to RelUserColDoc
	@OneToMany(mappedBy = "docInfo", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	private Set<RelUserColDoc> relUserColDocs;

	// bi-directional many-to-one association to RelUserReadDoc
	@OneToMany(mappedBy = "docInfo", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	private Set<RelUserReadDoc> relUserReadDocs;

	// bi-directional many-to-many association to TUserInfo
	@ManyToMany(mappedBy = "coleDocInfos")
	private Set<TUserInfo> coleUserInfos;

	// bi-directional many-to-many association to TUserInfo
	@ManyToMany(mappedBy = "readDocInfos")
	private Set<TUserInfo> readUserInfos;

	// bi-directional many-to-one association to TCatogory
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_cato_id")
	private TCatogory catoInfo;

	// bi-directional one-to-one association to TPublish
	@OneToOne(mappedBy = "docInfo", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
	private TPublish pubInfo;

	public TDocInfo() {
	}

	public int getDocId() {
		return this.docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public int getColeTimes() {
		return this.coleTimes;
	}

	public void setColeTimes(int coleTimes) {
		this.coleTimes = coleTimes;
	}

	public String getDocCode() {
		return this.docCode;
	}

	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}

	public String getDocLabel() {
		return this.docLabel;
	}

	public void setDocLabel(String docLabel) {
		this.docLabel = docLabel;
	}

	public String getDocPath() {
		return this.docPath;
	}

	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}

	public String getDocSize() {
		return this.docSize;
	}

	public void setDocSize(String docSize) {
		this.docSize = docSize;
	}

	public String getDocTitle() {
		return this.docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	public String getDocType() {
		return this.docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public int getReadTimes() {
		return this.readTimes;
	}

	public void setReadTimes(int readTimes) {
		this.readTimes = readTimes;
	}

	public Date getUploadTime() {
		if (this.uploadTime == null) {
			return null;
		} else {
			return (Date) this.uploadTime.clone();
		}
	}

	public void setUploadTime(Date uploadTime) {
		if (uploadTime == null) {
			this.uploadTime = null;
		} else {
			this.uploadTime = (Date) uploadTime.clone();
		}
	}

	public int getUploadUserId() {
		return this.uploadUserId;
	}

	public void setUploadUserId(int uploadUserId) {
		this.uploadUserId = uploadUserId;
	}

	public Set<RelUserColDoc> getRelUserColDocs() {
		return this.relUserColDocs;
	}

	public void setRelUserColDocs(Set<RelUserColDoc> relUserColDocs) {
		this.relUserColDocs = relUserColDocs;
	}

	public RelUserColDoc addRelUserColDoc(RelUserColDoc relUserColDoc) {
		getRelUserColDocs().add(relUserColDoc);
		relUserColDoc.setDocInfo(this);
		return relUserColDoc;
	}

	public RelUserColDoc removeRelUserColDoc(RelUserColDoc relUserColDoc) {
		getRelUserColDocs().remove(relUserColDoc);
		relUserColDoc.setDocInfo(null);
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
		relUserReadDoc.setDocInfo(this);
		return relUserReadDoc;
	}

	public RelUserReadDoc removeRelUserReadDoc(RelUserReadDoc relUserReadDoc) {
		getRelUserReadDocs().remove(relUserReadDoc);
		relUserReadDoc.setDocInfo(null);
		return relUserReadDoc;
	}

	public TCatogory getCatoInfo() {
		return this.catoInfo;
	}

	public void setCatoInfo(TCatogory catoInfo) {
		this.catoInfo = catoInfo;
	}

	public int getDocCatoId() {
		return docCatoId;
	}

	public void setDocCatoId(int docCatoId) {
		this.docCatoId = docCatoId;
	}

	public Set<TUserInfo> getColeUserInfos() {
		return coleUserInfos;
	}

	public void setColeUserInfos(Set<TUserInfo> coleUserInfos) {
		this.coleUserInfos = coleUserInfos;
	}

	public Set<TUserInfo> getReadUserInfos() {
		return readUserInfos;
	}

	public void setReadUserInfos(Set<TUserInfo> readUserInfos) {
		this.readUserInfos = readUserInfos;
	}

	public int getSecLevel() {
		return secLevel;
	}

	public void setSecLevel(int secLevel) {
		this.secLevel = secLevel;
	}

	public TPublish getPubInfo() {
		return pubInfo;
	}

	public void setPubInfo(TPublish pubInfo) {
		this.pubInfo = pubInfo;
	}
}