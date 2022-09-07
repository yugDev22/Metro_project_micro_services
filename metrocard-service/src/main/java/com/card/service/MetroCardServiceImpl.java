package com.card.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.card.bean.MetroCard;
import com.card.persistence.MetroCardDao;

@Service
public class MetroCardServiceImpl implements MetroCardService {

	@Autowired
	private MetroCardDao metroCardDao;
	
	@Override
	public MetroCard searchMetroCardById(Integer cardId) {
		return metroCardDao.findById(cardId).orElse(null);
	}

	@Override
	public MetroCard issueNewMetroCard(MetroCard card) {
		try {
			card.setCardId(getCardId());
			return metroCardDao.save(card);
		}catch(Exception e) {
			return null;
		}
	}

	@Override
	public Double checkCardBalance(Integer id) {
		MetroCard metroCard = searchMetroCardById(id);
		if(metroCard!=null) {
			return metroCard.getBalance();
		}
		return null;
	}

	@Override
	public Integer addCardBalance(Integer id, Double balance) {
		MetroCard metroCard = searchMetroCardById(id);
		if(metroCard!=null && balance>0) {
			return metroCardDao.updateMetroCard(balance, id);
		}
		return 0;
	}

	@Override
	public MetroCard refundMetroCard(Integer cardId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getCardId() {
		MetroCard lastCard = metroCardDao.getLastCard();
		int id=101;
		if(lastCard!=null) {
			id = lastCard.getCardId()+1;
		}
		return id;
	}

	@Override
	public List<MetroCard> getAllCards(Integer passengerId) {
		return metroCardDao.findByPassengerId(passengerId);
	}

	@Override
	public int deductBalance(Integer cardId, double balance) {
		MetroCard metroCard = searchMetroCardById(cardId);
		if(metroCard!=null) {
			return metroCardDao.updateMetroCard(balance,cardId);
		}
		return 0;
	}

}
