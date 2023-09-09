package com.playground.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.playground.model.Client;

public interface ClientRepository {
	
	Client findById(int id);
	
	void addOrUpdateClient(Client client);
	
	void deleteClient(int id);
}
