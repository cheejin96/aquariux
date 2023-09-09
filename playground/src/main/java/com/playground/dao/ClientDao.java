package com.playground.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.playground.model.Client;

public interface ClientDao extends JpaRepository<Client, Integer> {
	
}
