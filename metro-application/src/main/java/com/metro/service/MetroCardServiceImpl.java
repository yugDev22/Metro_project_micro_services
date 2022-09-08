package com.metro.service;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.metro.bean.CardList;
import com.metro.bean.MetroCard;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class MetroCardServiceImpl implements MetroCardService {

	@Autowired
	private RestTemplate restTemplate;

	@Override
	@CircuitBreaker(name="saerchCardCB", fallbackMethod="searchMetroCardByIdFallback")
	public MetroCard searchMetroCardById(Integer cardId) {
		ResponseEntity<MetroCard> responseCard = restTemplate.getForEntity("http://metrocard-service/cards/"+cardId, MetroCard.class);
		HttpStatus httpStatus = responseCard.getStatusCode();
		if(!httpStatus.equals(HttpStatus.FOUND)) {
			return null;
		}
		MetroCard card = responseCard.getBody();
		return card;

	}
	public MetroCard searchMetroCardByIdFallback(Exception e) {
		return null;
	}

	@Override
	@CircuitBreaker(name="issueCardCB", fallbackMethod="issueNewMetroCardFallback")
	public MetroCard issueNewMetroCard(MetroCard card) {
		ResponseEntity<MetroCard> response = restTemplate.postForEntity("http://metrocard-service/cards", card,MetroCard.class);
		HttpStatus httpStatus = response.getStatusCode();
		if(!httpStatus.equals(HttpStatus.CREATED)) {
			return null;
		}
		
		return response.getBody();
	}
	
	public MetroCard issueNewMetroCardFallback(Exception e) {
		return null;
	}

	@Override
	@CircuitBreaker(name="cardBalanceCB", fallbackMethod="checkCardBalanceFallback")
	public Double checkCardBalance(Integer id) {
		ResponseEntity<Double> response = restTemplate.getForEntity("http://metrocard-service/balances/"+id, Double.class);
		HttpStatus httpStatus = response.getStatusCode();
		if(!httpStatus.equals(HttpStatus.FOUND)) {
			return null;
		}
		return response.getBody();
	}
	
	public Double checkCardBalanceFallback(Exception e) {
		return null;
	}

	@Override
	@CircuitBreaker(name="addBalanceCB", fallbackMethod="addCardBalanceFallback")
	public Integer AddCardBalance(Integer id, Double balance) {
		double currBalance = checkCardBalance(id);
		currBalance = currBalance+balance;
		ResponseEntity<MetroCard> response = restTemplate.getForEntity("http://metrocard-service/adds/"+id+"/"+currBalance, MetroCard.class);
		HttpStatus httpStatus = response.getStatusCode();
		if(!httpStatus.equals(HttpStatus.CREATED)) {
			return 0;
		}
		
		return 1;

	}
	public Integer addCardBalanceFallback(Exception e) {
		return 0;	
	}
	
	@Override
	public MetroCard RefundMetroCard(Integer cardId) {
		
		return null;

	}



	@Override
	@CircuitBreaker(name="allCardsCB", fallbackMethod="getAllCardsFallback")
	public ArrayList<MetroCard> getAllCards(Integer passengerId) {
		ResponseEntity<CardList> response = restTemplate.getForEntity("http://metrocard-service/allcards/"+passengerId, CardList.class);
		HttpStatus httpStatus = response.getStatusCode();
		if(!httpStatus.equals(HttpStatus.FOUND)) {
			return null;
		}
		CardList list = response.getBody();
		return new ArrayList<MetroCard>(list.getCardsList());
	}
	
	public ArrayList<MetroCard> getAllCardsFallback(Exception e) {
		return new ArrayList<MetroCard>();
	}

	@Override
	@CircuitBreaker(name="deductFareCB", fallbackMethod="deductBalanceFallback")
	public int deductBalance(Integer cardId, double balance) {
		double currBalance = checkCardBalance(cardId);
		currBalance = currBalance-balance;
		ResponseEntity<MetroCard> response = restTemplate.getForEntity("http://metrocard-service/deducts/"+cardId+"/"+currBalance, MetroCard.class);
		HttpStatus httpStatus = response.getStatusCode();
		if(!httpStatus.equals(HttpStatus.CREATED)) {
			return 0;
		}
		
		return 1;
	}
	public int deductBalanceFallback(Exception e) {
		return 0;
	}

}
