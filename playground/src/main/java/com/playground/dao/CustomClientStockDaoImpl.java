package com.playground.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.playground.model.ClientStock;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CustomClientStockDaoImpl implements CustomClientStockDao {

	@PersistenceContext
    private EntityManager entityManager;
	
	public List<ClientStock> getClientStocks(int clientId) {
		
		String sql = "SELECT cs FROM ClientStock cs INNER JOIN Client c WHERE cs.client_id = c.id";
		List<ClientStock> clientStocks = entityManager.createQuery(sql).getResultList();
		return clientStocks;
	}

}
