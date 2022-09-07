package com.passenger.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.passenger.bean.Passenger;
import com.passenger.service.PassengerService;

@RestController
public class PassengerResource {

	@Autowired
	private PassengerService passengerService;
	
	@GetMapping(path = "passengers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Passenger> searchPassengerByIdResource(@PathVariable("id") Integer pId){
		Passenger passenger = passengerService.searchPassengerById(pId);
		if(passenger!=null) {
			return new ResponseEntity<Passenger>(passenger,HttpStatus.FOUND);
		}
		return new ResponseEntity<Passenger>(new Passenger(),HttpStatus.NO_CONTENT);
	}
	
	@PostMapping(path = "passengers", produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Passenger> addNewPassengerResource(@RequestBody Passenger passenger){
		Passenger addedPassenger = passengerService.addNewPassenger(passenger);
		if(addedPassenger!=null) {
			return new ResponseEntity<Passenger>(passenger,HttpStatus.CREATED);
		}
		return new ResponseEntity<Passenger>(new Passenger(),HttpStatus.NOT_MODIFIED);
	}
	
	@GetMapping(path = "passengers/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Passenger> searchPassengerByEmailIdResource(@PathVariable("email") String email){
		Passenger passenger = passengerService.searchPassengerByEmailId(email);
		if(passenger!=null) {
			return new ResponseEntity<Passenger>(passenger,HttpStatus.FOUND);
		}
		return new ResponseEntity<Passenger>(new Passenger(),HttpStatus.NO_CONTENT);
	}
}
