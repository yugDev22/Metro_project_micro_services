package com.swipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.swipe.bean.MetroCard;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class MetroCardService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@CircuitBreaker(name="MetroCardCB",fallbackMethod = "getMetroCardByIdFallback")
	public MetroCard getMetroCardById(Integer cardId) {
		ResponseEntity<MetroCard> responseEntity = restTemplate.getForEntity("http://metrocard-service/cards/"+cardId, MetroCard.class);
		HttpStatus httpStatus = responseEntity.getStatusCode();
		if(!httpStatus.equals(HttpStatus.FOUND)) {
			return null;
		}
		MetroCard metroCard = responseEntity.getBody();
		return metroCard;
	}
	
	public MetroCard getMetroCardByIdFallback(Exception e) {
		return new MetroCard();
	}

}
