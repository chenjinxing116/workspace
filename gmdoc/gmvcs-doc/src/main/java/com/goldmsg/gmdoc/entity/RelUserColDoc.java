package com.goldmsg.gmdoc.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the rel_user_col_doc database table.
 * 
 */
@Entity
@Table(name = "rel_user_col_doc")
@NamedQuery(name = "RelUserColDoc.findAll", query = "SELECT r FROM RelUserColDoc r")
public class RelUserColDoc implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RelUserColDocPK id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cole_time")
	private Date coleTime;

	// bi-directional many-to-one association to TDocInfo
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "doc_id", insertable = false, updatable = false)
	private TDocInfo docInfo;

	// bi-directional many-to-one association to TUserInfo
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private TUserInfo userInfo;

	public RelUserColDoc() {
	}

	public RelUserColDocPK getId() {
		return this.id;
	}

	public void setId(RelUserColDocPK id) {
		this.id = id;
	}

	public Date getColeTime() {
		if (this.coleTime == null) {
			return null;
		} else {
			return (Date) this.coleTime.clone();
		}
	}

	public void setColeTime(Date coleTime) {
		if (coleTime == null) {
			this.coleTime = null;
		} else {
			this.coleTime = (Date) coleTime.clone();
		}
	}

	public TDocInfo getDocInfo() {
		return this.docInfo;
	}

	public void setDocInfo(TDocInfo docInfo) {
		this.docInfo = docInfo;
	}

	public TUserInfo getUserInfo() {
		return this.userInfo;
	}

	public void setUserInfo(TUserInfo userInfo) {
		this.userInfo = userInfo;
	}
}