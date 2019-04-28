DROP DATABASE IF EXISTS messenger;

DROP TABLE IF EXISTS clients;

DROP TABLE IF EXISTS messages;

DROP TABLE IF EXISTS friends;

CREATE DATABASE messenger;

CREATE TABLE clients(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    password VARCHAR(40) NOT NULL
);

CREATE TABLE messages(
    id_sender INT NOT NULL,
    id_receiver INT NOT NULL,
    message VARCHAR(4000)
);

CREATE TABLE friends(
    id_client1 INT NOT NULL,
    id_client2 INT NOT NULL
);