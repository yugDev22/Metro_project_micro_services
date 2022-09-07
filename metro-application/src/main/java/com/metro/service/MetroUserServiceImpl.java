package com.metro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.metro.bean.Passenger;

@Service
public class MetroUserServiceImpl implements MetroUserService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public boolean validateUser(String userID, String password) {
		ResponseEntity<Passenger> responsePassenger = restTemplate.getForEntity("http://passenger-service/passengers/email/"+userID, Passenger.class);
		HttpStatus status = responsePassenger.getStatusCode();
		if(!status.equals(HttpStatus.FOUND)) {
			return false;
		}
		Passenger passenger = responsePassenger.getBody();
		if(passenger.getPassword().equals(password)) {
				return true;
			}
		return false;
	}

	@Override
	public Passenger getPassenger(String userId) {
		ResponseEntity<Passenger> responsePassenger = restTemplate.getForEntity("http://passenger-service/passengers/email/"+userId, Passenger.class);
		HttpStatus status = responsePassenger.getStatusCode();
		if(!status.equals(HttpStatus.FOUND)) {
			return null;
		}
		Passenger passenger = responsePassenger.getBody();
		if(passenger!=null) {
			return passenger;
		}
		return null;
	}

	@Override
	public boolean registerUser(Passenger passenger) {
		ResponseEntity<Passenger> responsePassenger = restTemplate.postForEntity("http://passenger-service/passengers", passenger, Passenger.class);
		HttpStatus status = responsePassenger.getStatusCode();
		if(status.equals(HttpStatus.CREATED)) {
			return true;
		}
		return false;
	}

	
}
