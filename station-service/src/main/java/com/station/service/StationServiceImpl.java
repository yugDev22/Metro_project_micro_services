package com.station.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.station.bean.Station;
import com.station.persistence.StationDao;

@Service
public class StationServiceImpl implements StationService {

	@Autowired
	private StationDao stationDao;
	
	@Override
	public Station searchMetroStationById(int stationId) {
		return stationDao.findById(stationId).orElse(null);
	}

	@Override
	public int addMetroStation(Station station) {
		Station addedStation = stationDao.save(station);
		if(addedStation!=null) {
			return 1;
		}
		return 0;
	}

	@Override
	public List<Station> getAllStations() {
		return stationDao.findAll();
	}

}
