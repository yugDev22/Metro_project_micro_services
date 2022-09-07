package com.swipe.bean;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SwipeTransaction {
	@Id
	private String transactionId;
	private Integer cardId;
	private Integer boardingStationId;
	private Integer destinationStationId;
	private Double fare;
	private LocalDateTime swipeInTime;
	private LocalDateTime swipeOutTime;

}