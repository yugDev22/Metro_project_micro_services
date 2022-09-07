package com.metro.service;

import java.util.ArrayList;

import com.metro.bean.Station;

public interface StationService {

	public Station searchMetroStationById(int stationId);
	public int addMetroStation(Station station);
	public ArrayList<Station> getAllStations();
}
