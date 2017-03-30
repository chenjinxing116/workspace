package com.goldmsg.gmdoc.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;

/**
 * The persistent class for the t_district_dict database table.
 * 
 */
@Entity
@Table(name = "t_district_dict")
@NamedQuery(name = "TDistrictDict.findAll", query = "SELECT t FROM TDistrictDict t")
public class TDistrictDict implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dist_id")
	private int distId;

	@Column(name = "dist_code")
	private String distCode;

	@Column(name = "dist_name")
	private String distName;

	@Column(name = "dist_status")
	private String distStatus;

	// bi-directional many-to-many association to TCatogory
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "rel_dist_cato", joinColumns = { @JoinColumn(name = "dist_id") }, inverseJoinColumns = {
			@JoinColumn(name = "cato_id") })
	private Set<TCatogory> catoInfos;

	// bi-directional many-to-one association to TUserInfo
	@OneToMany(mappedBy = "distInfo",fetch=FetchType.LAZY)
	private Set<TUserInfo> userInfos;

	public TDistrictDict() {
	}

	public int getDistId() {
		return this.distId;
	}

	public void setDistId(int distId) {
		this.distId = distId;
	}

	public String getDistCode() {
		return this.distCode;
	}

	public void setDistCode(String distCode) {
		this.distCode = distCode;
	}

	public String getDistName() {
		return this.distName;
	}

	public void setDistName(String distName) {
		this.distName = distName;
	}

	public String getDistStatus() {
		return this.distStatus;
	}

	public void setDistStatus(String distStatus) {
		this.distStatus = distStatus;
	}

	public Set<TCatogory> getCatoInfos() {
		return this.catoInfos;
	}

	public void setCatoInfos(Set<TCatogory> catoInfos) {
		this.catoInfos = catoInfos;
	}

	public Set<TUserInfo> getUserInfos() {
		return this.userInfos;
	}

	public void setUserInfos(Set<TUserInfo> TUserInfos) {
		this.userInfos = TUserInfos;
	}

	public TUserInfo addUserInfo(TUserInfo userInfo) {
		getUserInfos().add(userInfo);
		userInfo.setDistInfo(this);
		return userInfo;
	}

	public TUserInfo removeTUserInfo(TUserInfo userInfo) {
		getUserInfos().remove(userInfo);
		userInfo.setDistInfo(null);
		return userInfo;
	}

}