package com.passenger.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passenger {
	@Id
	private Integer passengerId;
	private String passengerName;
	private String passengerPhoneNumber;
	@Column(unique = true)
	private String passengerEmail;
	private Integer age;
	private String password;
}
