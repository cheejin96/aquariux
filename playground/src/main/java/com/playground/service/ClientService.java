package com.playground.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playground.dao.ClientDao;
import com.playground.model.Client;
import com.playground.repo.ClientRepository;

import jakarta.annotation.PostConstruct;

@Service
public class ClientService {
	
	@Autowired
	ClientDao clientDao;
	
	@PostConstruct
	public void InitiateClient() {
		Client client = getClientById(1);
		if(client == null) {
			client = new Client("CJ");
			clientDao.save(client);
		}
	}
	
	public Client getClientById(int id) {
		Client client = null;
		if(clientDao.findById(id).isPresent()) {
			client = clientDao.findById(id).get();
		}
		return client;
	}
	
	public void saveClient(Client client) {
		clientDao.saveAndFlush(client);
	}
}
