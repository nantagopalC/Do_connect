package com.example.demo.CapEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapAdmin
{
	@Id
	@Column(name = "admin_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "admin_name")
	@NotBlank(message = "Name is mandatory")
	private String name;
	
	@Column(name = "admin_password")
	@NotBlank(message = "password the mandatory")
	@Size(min = 8, message = "length should be 8")
	private String password;
	
	@Column(name = "admin_email")
	@NotBlank(message = "Email is mandatory")
	@Email(message = "provide a valid mail id only")
	private String email;
	
	@Column(name = "admin_mobile_number")
	@NotBlank(message = "Phone Number is mandatory")
	@Size(max = 10, min = 10)
	private String phoneNumber;

	@Column(name = "is_email_active")
	private Boolean isActive = true;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	

}
