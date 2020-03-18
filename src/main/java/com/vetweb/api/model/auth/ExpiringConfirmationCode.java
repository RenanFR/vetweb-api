package com.vetweb.api.model.auth;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_exp_conf_code")
@Getter
@Setter
public class ExpiringConfirmationCode {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String code;
	
	@Column(name = "issue_date")
	private LocalDateTime issuedAt;
	
	@Column(name = "expiration_date")
	private LocalDateTime expiration;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	public boolean isValid() {
		return expiration.isAfter(LocalDateTime.now());
	}

}
