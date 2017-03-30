package com.goldmsg.gmdoc.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the rel_user_read_doc database table.
 * 
 */
@Embeddable
public class RelUserReadDocPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "user_id", insertable = false, updatable = false)
	private int userId;

	@Column(name = "doc_id", insertable = false, updatable = false)
	private int docId;

	public RelUserReadDocPK() {
	}

	public RelUserReadDocPK(int user_id, int doc_id) {
		this.userId = user_id;
		this.docId = doc_id;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getDocId() {
		return this.docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RelUserReadDocPK)) {
			return false;
		}
		RelUserReadDocPK castOther = (RelUserReadDocPK) other;
		return (this.userId == castOther.userId) && (this.docId == castOther.docId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId;
		hash = hash * prime + this.docId;
		return hash;
	}
}