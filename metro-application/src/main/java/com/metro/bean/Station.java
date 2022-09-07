package com.metro.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Station {
	private Integer stationId;
	private String stationName;
	private Integer prevStationId;
	private Integer nextStationId;
}
