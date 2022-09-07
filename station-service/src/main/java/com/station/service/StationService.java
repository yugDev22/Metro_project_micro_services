package com.station.service;

import java.util.List;

import com.station.bean.Station;

public interface StationService {

	public Station searchMetroStationById(int stationId);
	public int addMetroStation(Station station);
	public List<Station> getAllStations();
}
