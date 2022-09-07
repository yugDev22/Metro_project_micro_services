package com.metro.bean;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwipeTransaction {
	private String transactionId;
	private Integer cardId;
	private Integer boardingStationId;
	private Integer destinationStationId;
	private Double fare;
	private LocalDateTime swipeInTime;
	private LocalDateTime swipeOutTime;

}