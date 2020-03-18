package com.vetweb.api.model.auth;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

//Model for user information
@Entity
@Table(name = "tbl_user")
@AllArgsConstructor
@Builder
public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	
	private String email;
	
	@JsonIgnore
	private String password;
	
	@Column(name = "using_2fa")
	private Boolean using2FA;
	
	private String twoFASecret;
	
	@Column(name = "is_social_login")
	private boolean socialLogin;
	
	@Column(name = "inclusion_date")
	private LocalDate inclusionDate;
	
	@Getter
	@Setter
	private Boolean enabled;
	
	@Getter
	@Setter
	@Column(name = "temp_password")
	private String temporaryPassword;
	
	@Getter
	@Setter
	@OneToOne(mappedBy = "user")
	private ExpiringConfirmationCode confirmationCode;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JsonManagedReference
	@JoinTable(name = "tbl_user_profile",
				joinColumns = @JoinColumn(
          			name = "user_id", referencedColumnName = "id"), 
    			inverseJoinColumns = @JoinColumn(
					name = "profile_id", referencedColumnName = "role"))
	private List<Profile> profiles = new ArrayList<Profile>();
	
	public User(Long id, String name, String password, Boolean isSocialLogin,
			List<Profile> profiles) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.socialLogin = isSocialLogin;
		this.profiles = profiles;
	}

	public User(String name, String email, String password, Boolean using2fa, boolean socialLogin) {
		this.name = name;
		this.email = email;
		this.temporaryPassword = password;
		this.password = password;
		using2FA = using2fa;
		this.socialLogin = socialLogin;
	}

	public User(String userRegistration) {
		this.name = userRegistration;
	}

	public User() {
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return profiles;
	}

	@Override
	public String getUsername() {
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public boolean isSocialLogin() {
		return socialLogin;
	}

	public void setSocialLogin(Boolean socialLogin) {
		this.socialLogin = socialLogin;
	}

	public boolean isUsing2FA() {
		return using2FA;
	}

	public void setUsing2FA(boolean using2fa) {
		using2FA = using2fa;
	}

	public String getTwoFASecret() {
		return twoFASecret;
	}

	public void setTwoFASecret(String twoFASecret) {
		this.twoFASecret = twoFASecret;
	}

	public LocalDate getInclusionDate() {
		return inclusionDate;
	}

	public void setInclusionDate(LocalDate inclusionDate) {
		this.inclusionDate = inclusionDate;
	}

}
