package com.prawal.currency_exchange_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prawal.currency_exchange_service.DAO.currencyExchange;

public interface currency_exchange_repository extends JpaRepository<currencyExchange, Long> {
	
	currencyExchange findByFromAndTo(String from, String to);

}
