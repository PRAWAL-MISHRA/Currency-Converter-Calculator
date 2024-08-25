package com.prawal.currency_exchange_service.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.prawal.currency_exchange_service.DAO.currencyExchange;
import com.prawal.currency_exchange_service.repository.currency_exchange_repository;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CurrencyExchangeController 
{
	@Autowired
	private currency_exchange_repository repository;
	
	@Autowired
	private Environment environment;
	
	private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);
	
	@GetMapping("currency-exchange/from/{from}/to/{to}")
	@Retry(name="default", fallbackMethod = "hardCodedResponse")
	@CircuitBreaker(name="default", fallbackMethod = "hardCodedResponse")
	@RateLimiter(name="default")
	@Bulkhead(name="default")
	public currencyExchange retrieveValue(@PathVariable String from, @PathVariable String to)
	{
		//currencyExchange currencyexchange = new currencyExchange(1000L, from, to, BigDecimal.valueOf(65));
		logger.info("Sample Api call received");
		currencyExchange currencyexchange = repository.findByFromAndTo(from, to);
		if(currencyexchange.equals(null))
			throw new RuntimeException("Unable to find data for"+ from +" and "+ to);
		String port = environment.getProperty("local.server.port");
		currencyexchange.setEnvironment(port);
		return currencyexchange;
	}
	
	public String hardCodedResponse(Exception ex)
	{
		return "hardCodedResponse";
	}
}
