-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS donation_db;
USE donation_db;

-- User table (base for donors and associations)
CREATE TABLE IF NOT EXISTS User (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Donor table
CREATE TABLE IF NOT EXISTS Donor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    user_id INT UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

-- Association table
CREATE TABLE IF NOT EXISTS Association (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(200),
    user_id INT UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

-- Donation table
CREATE TABLE IF NOT EXISTS Donation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(100) NOT NULL,
    description TEXT,
    quantity INT NOT NULL DEFAULT 1,
    isAvailable BOOLEAN DEFAULT TRUE,
    donor_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (donor_id) REFERENCES Donor(id) ON DELETE CASCADE
);

-- DonationCollection table (donation collection history)
CREATE TABLE IF NOT EXISTS DonationCollection (
    id INT AUTO_INCREMENT PRIMARY KEY,
    donation_id INT NOT NULL,
    association_id INT NOT NULL,
    quantity INT NOT NULL,
    dateDonationCollected DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (donation_id) REFERENCES Donation(id) ON DELETE CASCADE,
    FOREIGN KEY (association_id) REFERENCES Association(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX idx_donation_status ON Donation(isAvailable);
CREATE INDEX idx_donation_donor ON Donation(donor_id);
CREATE INDEX idx_collection_association ON DonationCollection(association_id);
CREATE INDEX idx_collection_donation ON DonationCollection(donation_id);
