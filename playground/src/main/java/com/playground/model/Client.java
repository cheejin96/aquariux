package com.playground.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.context.annotation.Lazy;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Clients")
public class Client {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="wallet")
	private BigDecimal wallet;
	
	@OneToMany(targetEntity = Transaction.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL) 
	private transient List<Transaction> transaction;
	
	@OneToMany(targetEntity = ClientStock.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private transient List<ClientStock> clientStocks;
	
	public Client() {
		
	}
	
	public Client(String name) {
		this.name = name;
		wallet = new BigDecimal("50000");
		clientStocks = new ArrayList<>();
		transaction = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getWallet() {
		return wallet;
	}

	public void setWallet(BigDecimal wallet) {
		this.wallet = wallet;
	}

	public List<Transaction> getTransaction() {
		return transaction;
	}
	
	public void setTransaction(List<Transaction> transaction) {
		this.transaction = transaction;
	}	

	public List<ClientStock> getClientStocks() {
		return clientStocks;
	}

	public void setClientStocks(List<ClientStock> clientStocks) {
		this.clientStocks = clientStocks;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", name=" + name + ", wallet=" + wallet +"]";
	}
	
}
