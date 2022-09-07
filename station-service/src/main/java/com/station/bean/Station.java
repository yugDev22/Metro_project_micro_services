package com.station.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Station {
	@Id
	private Integer stationId;
	private String stationName;
	private Integer prevStationId;
	private Integer nextStationId;
}
