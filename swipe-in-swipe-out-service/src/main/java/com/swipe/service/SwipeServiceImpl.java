package com.swipe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.swipe.bean.MetroCard;
import com.swipe.bean.SwipeTransaction;
import com.swipe.persistence.SwipeDao;

@Service
public class SwipeServiceImpl implements SwipeService {
	
	@Autowired
	private SwipeDao swipeDao;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<SwipeTransaction> getAllSwipeTransactionsByCardId(int cardId) {
		List<SwipeTransaction> allTransList = swipeDao.findByCardId(cardId);
		if(allTransList.isEmpty()){
			return new ArrayList<SwipeTransaction>();
		}
		return allTransList;
	}

	@Override
	public int swipeIn(SwipeTransaction transaction) {
		// add swipe in transaction
		String tid=getTransactionId();
		return swipeDao.addSwipeInTransaction(tid, transaction.getCardId(), transaction.getBoardingStationId(), transaction.getSwipeInTime());
	}

	@Override
	public SwipeTransaction alreadySwipedIn(Integer cardId) {
		// check if already swiped in
		SwipeTransaction swipeTransaction = swipeDao.getSwipedInTransaction(cardId);
		//System.out.println(swipeTransaction);
		return swipeTransaction;
	}

	@Override
	public SwipeTransaction swipeOut(SwipeTransaction transaction) {
		// update already existing swiped in transaction by
		// adding destination station, fare, & boarding time.
		ResponseEntity<MetroCard> responseEntity = restTemplate.getForEntity("http://metrocard-service/cards/"+transaction.getCardId(), MetroCard.class);
		HttpStatus httpStatus = responseEntity.getStatusCode();
		if(!httpStatus.equals(HttpStatus.FOUND)) {
			return null;
		}
		MetroCard metroCard = responseEntity.getBody();
		//------+++++++------//
		int sourceStation = transaction.getBoardingStationId();
		int destStation = transaction.getDestinationStationId();
		double fare = Math.abs(destStation-sourceStation)*5;
		transaction.setFare(fare);
	
		if(fare>metroCard.getBalance()){
			return null;
		}
		//metroCardService.deductBalance(card.getCardId(), fare);
		if(swipeDao.addSwipeOutTransaction(transaction.getDestinationStationId(), fare, transaction.getSwipeOutTime(),transaction.getTransactionId())>0) {
			return transaction;
		}
		else return null;
		//return swipeDao.addSwipeOutTransaction(transaction.getTransactionId(), transaction., null, null);
		
	}

	@Override
	public String getTransactionId() {
		// automatically generate transaction id
		SwipeTransaction lastTran = swipeDao.getLastTransaction();
		String tid = "t0";
		if(lastTran!=null) {
			String lastId=lastTran.getTransactionId().split("t")[1];
			tid = "t"+Integer.toString(Integer.parseInt(lastId)+1);
		}
		return tid;
	}

	@Override
	public MetroCard getMetroCardById(Integer cardId) {
		// TODO Auto-generated method stub
		return null;
	}

}
