package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_mobile_tokens")
public class MobileTokenEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "int_mtoken_id")
	private Integer mobileTokenId;

	@Column(name = "int_user_id")
	private Integer userId;

	@Column(name = "dtm_token_generate_time")
	private Date tokenGeneratedTime;

	@Column(name = "vch_token_value")
	private String token;

	@Column(name = "bit_token_status")
	private boolean bitStatus;

	@Column(name = "dtm_updated_on")
	private Date tokenUpdatedTime;

	public Integer getMobileTokenId() {
		return mobileTokenId;
	}

	public void setMobileTokenId(Integer mobileTokenId) {
		this.mobileTokenId = mobileTokenId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getTokenGeneratedTime() {
		return tokenGeneratedTime;
	}

	public void setTokenGeneratedTime(Date tokenGeneratedTime) {
		this.tokenGeneratedTime = tokenGeneratedTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	public Date getTokenUpdatedTime() {
		return tokenUpdatedTime;
	}

	public void setTokenUpdatedTime(Date tokenUpdatedTime) {
		this.tokenUpdatedTime = tokenUpdatedTime;
	}

	@Override
	public String toString() {
		return "MobileTokenEntity [mobileTokenId=" + mobileTokenId + ", userId=" + userId + ", tokenGeneratedTime="
				+ tokenGeneratedTime + ", token=" + token + ", bitStatus=" + bitStatus + ", tokenUpdatedTime="
				+ tokenUpdatedTime + "]";
	}

}
