package com.station.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.station.bean.Station;
import com.station.bean.StationsList;
import com.station.persistence.StationDao;
import com.station.service.StationService;

@RestController
public class StationResource {

	@Autowired
	private StationService stationService;
	
	@GetMapping(path = "stations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Station> getStationByIdResource(@PathVariable("id") Integer stationId){
		Station station = stationService.searchMetroStationById(stationId);
		if(station!=null) {
			return new ResponseEntity<Station>(station,HttpStatus.FOUND);
		}
		return new ResponseEntity<Station>(new Station(),HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(path = "allstations", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StationsList> getAllStationsResource(){
		List<Station> list = stationService.getAllStations();
		if(!list.isEmpty()) {
			return new ResponseEntity<StationsList>(new StationsList(list),HttpStatus.FOUND);
		}
		return new ResponseEntity<StationsList>(new StationsList(new ArrayList<Station>()),HttpStatus.NO_CONTENT);
	}
	
	@PostMapping(path = "stations", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Station> addStationResource(@RequestBody Station station){
		int rows = stationService.addMetroStation(station);
		if(rows>0) {
			return new ResponseEntity<Station>(station,HttpStatus.CREATED);
		}
		return new ResponseEntity<Station>(new Station(),HttpStatus.NOT_MODIFIED);
	}
}
