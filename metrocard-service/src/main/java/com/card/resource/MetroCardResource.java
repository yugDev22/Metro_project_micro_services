package com.card.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.card.bean.CardList;
import com.card.bean.MetroCard;
import com.card.service.MetroCardService;

@RestController
public class MetroCardResource {
	
	@Autowired
	private MetroCardService metroCardService;
	
	@GetMapping(path = "/cards/{cId}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MetroCard> getMetroCardResource(@PathVariable("cId") Integer cardId){
		MetroCard metroCard = metroCardService.searchMetroCardById(cardId);
		if(metroCard!=null) {
			return new ResponseEntity<MetroCard>(metroCard,HttpStatus.FOUND);
			
		}
		return new ResponseEntity<MetroCard>(new MetroCard(),HttpStatus.NO_CONTENT);
	}
	
	@PostMapping(path = "/cards", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MetroCard> addNewCardResource(@RequestBody MetroCard card){
		MetroCard issuedCard = metroCardService.issueNewMetroCard(card);
		if(issuedCard!=null) {
			return new ResponseEntity<MetroCard>(issuedCard,HttpStatus.CREATED);
		}
		return new ResponseEntity<MetroCard>(new MetroCard(),HttpStatus.NOT_MODIFIED);
	}
	
	@GetMapping(path = "/allcards/{pId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CardList> getAllCardsOfPassengerResource(@PathVariable("pId") Integer passengerId){
		List<MetroCard> list = metroCardService.getAllCards(passengerId);
		if(list.size()>0) {
			return new ResponseEntity<CardList>(new CardList(list), HttpStatus.FOUND);
		}
		return new ResponseEntity<CardList>(new CardList(new ArrayList<MetroCard>()),HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(path = "/adds/{cardId}/{bal}", produces = MediaType.APPLICATION_JSON_VALUE)
	@CacheEvict
	public ResponseEntity<MetroCard> addBalanceResource(@PathVariable("cardId") Integer cardId, @PathVariable("bal") Double balance){
		if(metroCardService.addCardBalance(cardId, balance)>0) {
			MetroCard metroCard = metroCardService.searchMetroCardById(cardId);
			return new ResponseEntity<MetroCard>(metroCard,HttpStatus.CREATED);
		}
		return new ResponseEntity<MetroCard>(new MetroCard(), HttpStatus.NOT_MODIFIED);
	}
	
	@GetMapping(path = "/deducts/{cardId}/{fare}", produces = MediaType.APPLICATION_JSON_VALUE)
	@CacheEvict
	public ResponseEntity<MetroCard> deductBalanceResource(@PathVariable("cardId") Integer cardId, @PathVariable("fare") Double fare){
		if(metroCardService.deductBalance(cardId, fare)>0) {
			return new ResponseEntity<MetroCard>(metroCardService.searchMetroCardById(cardId),HttpStatus.CREATED);
		}
		return new ResponseEntity<MetroCard>(new MetroCard(), HttpStatus.NOT_MODIFIED);
	}
	
	@GetMapping(path = "/balances/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Double> checkBalanceResource(@PathVariable("cardId") Integer cardId){
		Double balance = metroCardService.checkCardBalance(cardId);
		if(balance!=null) {
			return new ResponseEntity<Double>(balance,HttpStatus.FOUND);
		}
		return new ResponseEntity<Double>(0.0, HttpStatus.NO_CONTENT);
	}
}
