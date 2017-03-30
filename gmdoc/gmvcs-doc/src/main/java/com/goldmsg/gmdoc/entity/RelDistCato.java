package com.goldmsg.gmdoc.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the rel_dist_cato database table.
 * 
 */
@Entity
@Table(name="rel_dist_cato")
@NamedQuery(name="RelDistCato.findAll", query="SELECT r FROM RelDistCato r")
public class RelDistCato implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private RelDistCatoPK id;

	public RelDistCato() {
	}

	public RelDistCatoPK getId() {
		return this.id;
	}

	public void setId(RelDistCatoPK id) {
		this.id = id;
	}

}