DROP TABLE IF EXISTS Clients;  
CREATE TABLE Clients (  
id INT AUTO_INCREMENT  PRIMARY KEY,  
name VARCHAR(50) NOT NULL,  
wallet DOUBLE(40,2) NOT NULL  
);  

DROP TABLE IF EXISTS Stocks;  
CREATE TABLE Stocks (  
id INT AUTO_INCREMENT  PRIMARY KEY,  
name VARCHAR(50) NOT NULL,  
valuePerStock DOUBLE(40,2) NOT NULL  
);  

DROP TABLE IF EXISTS Transactions;  
CREATE TABLE Transactions (  
id INT AUTO_INCREMENT  PRIMARY KEY,  
client_id INT,  
stock_id INT,
value DOUBLE(40,2) NOT NULL,
action  VARCHAR(50) NOT NULL,
foreign key (client_id) references Clients(id),
foreign key (stock_id) references Stocks(id)
);  

DROP TABLE IF EXISTS ClientStocks;  
CREATE TABLE ClientStocks (  
id INT AUTO_INCREMENT  PRIMARY KEY,  
client_id INT,  
stock_id INT,
quantity INT(5) NOT NULL,
foreign key (client_id) references Clients(id),
foreign key (stock_id) references Stocks(id)
);  