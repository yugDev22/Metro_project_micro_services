package com.metro.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.metro.bean.MetroCard;
import com.metro.bean.SwipeTransaction;
import com.metro.bean.TransactionsList;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private MetroCardService metroCardService;
	
	
	@Override
	@CircuitBreaker(name="AllTransactionsCB",fallbackMethod = "getAllTransactionsByCardIdFallback")
	public List<SwipeTransaction> getAllTransactionsByCardId(int cardId) {
		ResponseEntity<TransactionsList> response = restTemplate.getForEntity("http://swipe-service/transactions/"+cardId, TransactionsList.class);
		HttpStatus httpStatus = response.getStatusCode();
		if(!httpStatus.equals(HttpStatus.FOUND)) {
			List<SwipeTransaction> list = new ArrayList<SwipeTransaction>();
			//list.add(new SwipeTransaction());
			return list;
		}
		
		return new ArrayList<SwipeTransaction>(response.getBody().getTransactionList());
	}
	
	public List<SwipeTransaction> getAllTransactionsByCardIdFallback(Exception e){
		return new ArrayList<SwipeTransaction>();
	}

	@Override
	@CircuitBreaker(name="swipeInCB",fallbackMethod = "addNewTransactionFallback")
	public int addNewTransaction(SwipeTransaction transaction) {
		if(transaction.getDestinationStationId()==null) {
			transaction.setDestinationStationId(0);
		}
		
		ResponseEntity<SwipeTransaction> response = restTemplate.postForEntity("http://swipe-service/swipesin", transaction,SwipeTransaction.class);
		HttpStatus httpStatus = response.getStatusCode();
		if(!httpStatus.equals(HttpStatus.CREATED)) {
			return 0;
		}
		return 1;
	}
	
	public int addNewTransactionFallback(Exception e){
		return 0;
	}

	@Override
	@CircuitBreaker(name="alreadySwipedCB",fallbackMethod = "alreadySwipedInFallback")
	public SwipeTransaction alreadySwipedIn(Integer cardId) {
		ResponseEntity<SwipeTransaction> response = restTemplate.getForEntity("http://swipe-service/swiped/"+cardId,SwipeTransaction.class);
		HttpStatus httpStatus = response.getStatusCode();
		if(!httpStatus.equals(HttpStatus.FOUND)) {
			return null;
		}
		return response.getBody();
	}

	public SwipeTransaction alreadySwipedInFallback(Exception e) {
		return null;
	}
	
	@Override
	@CircuitBreaker(name="swipeOutCB",fallbackMethod = "updateTransactionFallback")
	public SwipeTransaction updateTransaction(SwipeTransaction transaction) {
		Double balance = metroCardService.checkCardBalance(transaction.getCardId());
		if(balance==null) {
			return null;
		}
		
//		ResponseEntity<Double> balanceResponse = restTemplate.getForEntity("http://metrocard-service/balances/"+transaction.getCardId(), Double.class);
//		HttpStatus balanceHttpStatus = balanceResponse.getStatusCode();
//		if(!balanceHttpStatus.equals(HttpStatus.FOUND)) {
//			return null;
//		}
		//double balance = balanceResponse.getBody();
		int sourceStation = transaction.getBoardingStationId();
		int destStation = transaction.getDestinationStationId();
		double fare = Math.abs(destStation-sourceStation)*5;
		if(fare>balance) {
			return null;
		}
		ResponseEntity<SwipeTransaction> response = restTemplate.postForEntity("http://swipe-service/swipesout",transaction,SwipeTransaction.class);
		HttpStatus httpStatus = response.getStatusCode();
		if(!httpStatus.equals(HttpStatus.CREATED)) {
			return null;
		}
		if(metroCardService.deductBalance(transaction.getCardId(), fare)>0) {
			return response.getBody();
		}
		else {
			return null;
			
		}
	}
	public SwipeTransaction updateTransactionFallback(Exception e) {
		return null;
	}
	

}
