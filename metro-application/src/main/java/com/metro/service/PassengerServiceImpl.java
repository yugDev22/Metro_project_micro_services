package com.metro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.metro.bean.Passenger;

@Service
public class PassengerServiceImpl implements PassengerService {
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Passenger searchPassengerById(Integer id) {
		ResponseEntity<Passenger> responsePassenger = restTemplate.getForEntity("http://passenger-service/passengers/"+id, Passenger.class);
		HttpStatus status = responsePassenger.getStatusCode();
		if(!status.equals(HttpStatus.FOUND)) {
			return null;
		}
		return responsePassenger.getBody();
	}

	@Override
	public Passenger addNewPassenger(Passenger passenger) {
		ResponseEntity<Passenger> responsePassenger = restTemplate.postForEntity("http://passenger-service/passengers", passenger,Passenger.class);
		HttpStatus status = responsePassenger.getStatusCode();
		if(!status.equals(HttpStatus.CREATED)) {
			return null;
		}
		
//		if(passengerDao.searchPassenger(passenger.getPassengerId())==null) {
//			if(passengerDao.addPassenger(passenger)>0) {
//				return passenger;
//			}
//		}
		return null;
	}

	@Override
	public Passenger updatingPassengerDetails(Passenger passenger) {
		
		return null;
	}


}
