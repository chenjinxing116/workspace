package com.goldmsg.gmdoc.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the rel_user_read_doc database table.
 * 
 */
@Entity
@Table(name = "rel_user_read_doc")
@NamedQuery(name = "RelUserReadDoc.findAll", query = "SELECT r FROM RelUserReadDoc r")
public class RelUserReadDoc implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RelUserReadDocPK id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "read_time")
	private Date readTime;

	// bi-directional many-to-one association to TDocInfo
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doc_id", insertable = false, updatable = false)
	private TDocInfo docInfo;

	// bi-directional many-to-one association to TUserInfo
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private TUserInfo userInfo;

	public RelUserReadDoc() {
	}

	public RelUserReadDoc(RelUserReadDocPK pk) {
		this.id = pk;
	}

	public RelUserReadDocPK getId() {
		return id;
	}

	public void setId(RelUserReadDocPK id) {
		this.id = id;
	}

	public Date getReadTime() {
		if (this.readTime == null) {
			return null;
		} else {
			return (Date) this.readTime.clone();
		}
	}

	public void setReadTime(Date readTime) {
		if (readTime == null) {
			this.readTime = null;
		} else {
			this.readTime = (Date) readTime.clone();
		}
	}

	public TDocInfo getDocInfo() {
		return docInfo;
	}

	public void setDocInfo(TDocInfo docInfo) {
		this.docInfo = docInfo;
	}

	public TUserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(TUserInfo userInfo) {
		this.userInfo = userInfo;
	}

}