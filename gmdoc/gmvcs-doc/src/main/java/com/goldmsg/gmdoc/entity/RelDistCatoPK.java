package com.goldmsg.gmdoc.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the rel_dist_cato database table.
 * 
 */
@Embeddable
public class RelDistCatoPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "dist_id", insertable = false, updatable = false)
	private int distId;

	@Column(name = "cato_id", insertable = false, updatable = false)
	private int catoId;

	public RelDistCatoPK() {
	}

	public RelDistCatoPK(int distId, int catoId) {
		this.distId = distId;
		this.catoId = catoId;
	}

	public int getDistId() {
		return this.distId;
	}

	public void setDistId(int distId) {
		this.distId = distId;
	}

	public int getCatoId() {
		return this.catoId;
	}

	public void setCatoId(int catoId) {
		this.catoId = catoId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RelDistCatoPK)) {
			return false;
		}
		RelDistCatoPK castOther = (RelDistCatoPK) other;
		return (this.distId == castOther.distId) && (this.catoId == castOther.catoId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.distId;
		hash = hash * prime + this.catoId;

		return hash;
	}
}