package com.card.bean;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MetroCard {
	@Id
	private Integer cardId;
	private Integer passengerId;
	private Double balance;
}
