package com.metro.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.metro.bean.Station;
import com.metro.bean.StationsList;


@Service
public class StationServiceImpl implements StationService {
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Station searchMetroStationById(int stationId) {
		ResponseEntity<Station> response = restTemplate.getForEntity("http://station-service/stations/"+stationId, Station.class);
		HttpStatus status = response.getStatusCode();
		if(!status.equals(HttpStatus.FOUND)) {
			return null;
		}
		return response.getBody();
	}

	@Override
	public int addMetroStation(Station station) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<Station> getAllStations() {
		ResponseEntity<StationsList> response = restTemplate.getForEntity("http://station-service/allstations", StationsList.class);
		HttpStatus status = response.getStatusCode();
		if(!status.equals(HttpStatus.FOUND)) {
			return new ArrayList<Station>();
		}
		return new ArrayList<Station>(response.getBody().getStationList());
	}

}
