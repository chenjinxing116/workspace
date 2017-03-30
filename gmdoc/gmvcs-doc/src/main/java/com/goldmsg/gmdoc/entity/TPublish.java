package com.goldmsg.gmdoc.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.goldmsg.gmdoc.entity.TUserInfo;

/**
 * The persistent class for the t_publish database table.
 * 
 */
@Entity
@Table(name = "t_publish")
@NamedQuery(name = "TPublish.findAll", query = "SELECT t FROM TPublish t")
public class TPublish implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pub_id")
	private int pubId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "operate_time")
	private Date operateTime;

	@Column(name = "pub_status")
	private int pubStatus;

	// uni-directional many-to-one association to TUserInfo
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "audit_user_id")
	private TUserInfo auditUserInfo;

	// uni-directional many-to-one association to TUserInfo
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pub_user_id")
	private TUserInfo pubUserInfo;

	// bi-directional one-to-one association to TDocInfo
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pub_doc_id")
	private TDocInfo docInfo;

	public TPublish() {
	}

	public int getPubId() {
		return this.pubId;
	}

	public void setPubId(int pubId) {
		this.pubId = pubId;
	}

	public Date getOperateTime() {
		if (this.operateTime == null) {
			return null;
		} else {
			return (Date) this.operateTime.clone();
		}
	}

	public void setOperateTime(Date operateTime) {
		if (operateTime == null) {
			this.operateTime = null;
		} else {
			this.operateTime = (Date) operateTime.clone();
		}
	}

	public int getPubStatus() {
		return this.pubStatus;
	}

	public void setPubStatus(int pubStatus) {
		this.pubStatus = pubStatus;
	}

	public TUserInfo getAuditUserInfo() {
		return this.auditUserInfo;
	}

	public void setAuditUserInfo(TUserInfo auditUserInfo) {
		this.auditUserInfo = auditUserInfo;
	}

	public TUserInfo getPubUserInfo() {
		return this.pubUserInfo;
	}

	public void setPubUserInfo(TUserInfo pubUserInfo) {
		this.pubUserInfo = pubUserInfo;
	}

	public TDocInfo getDocInfo() {
		return docInfo;
	}

	public void setDocInfo(TDocInfo docInfo) {
		this.docInfo = docInfo;
	}
}