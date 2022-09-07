package com.swipe.resource;

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

import com.swipe.bean.SwipeTransaction;
import com.swipe.bean.TransactionsList;
import com.swipe.service.SwipeService;

@RestController
public class SwipeResource {
	
	@Autowired
	private SwipeService swipeService;

	@GetMapping(path = "swiped/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SwipeTransaction> alreadySwipedInResource(@PathVariable("cardId") Integer cardId){
		SwipeTransaction transaction = swipeService.alreadySwipedIn(cardId);
		if(transaction!=null) {
			return new ResponseEntity<SwipeTransaction>(transaction,HttpStatus.FOUND);
		}
		return new ResponseEntity<SwipeTransaction>(new SwipeTransaction(),HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(path = "transactions/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionsList> getAllTransactionsResource(@PathVariable("cardId") Integer cardId){
		List<SwipeTransaction> swipeTransactions = swipeService.getAllSwipeTransactionsByCardId(cardId);
		if(swipeTransactions.isEmpty()) {
			return new ResponseEntity<TransactionsList>(new TransactionsList(new ArrayList<SwipeTransaction>()),HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<TransactionsList>(new TransactionsList(swipeTransactions),HttpStatus.FOUND);
	}
	
	@PostMapping(path = "swipesin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SwipeTransaction> swipeInResource(@RequestBody SwipeTransaction transaction){
		if(swipeService.swipeIn(transaction)>0) {
			return new ResponseEntity<SwipeTransaction>(transaction,HttpStatus.CREATED);
		}
		return new ResponseEntity<SwipeTransaction>(new SwipeTransaction(), HttpStatus.NOT_MODIFIED);
	}
	
	@PostMapping(path = "swipesout", produces = MediaType.APPLICATION_JSON_VALUE,  consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SwipeTransaction> swipeOutResource(@RequestBody SwipeTransaction transaction){
		SwipeTransaction swipedOut = swipeService.swipeOut(transaction);
		if(swipedOut!=null) {
			return new ResponseEntity<SwipeTransaction>(swipedOut,HttpStatus.CREATED);
		}
		return new ResponseEntity<SwipeTransaction>(new SwipeTransaction(),HttpStatus.NOT_MODIFIED);
	}
	

}
