package com.metro.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetroCard {
	private Integer cardId;
	private Integer passengerId;
	private Double balance;
}
