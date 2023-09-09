package com.playground.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.playground.model.ClientStock;

public interface ClientStockDao extends JpaRepository<ClientStock, Integer>{

	List<ClientStock> findByClientId(int clientId);
}
