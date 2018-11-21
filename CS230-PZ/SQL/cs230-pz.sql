-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jun 13, 2018 at 10:04 PM
-- Server version: 10.1.30-MariaDB
-- PHP Version: 7.2.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cs230-pz`
--

-- --------------------------------------------------------

--
-- Table structure for table `aerodrom`
--

CREATE TABLE `aerodrom` (
  `id` int(11) NOT NULL,
  `grad` varchar(255) NOT NULL,
  `drzava` varchar(255) NOT NULL,
  `naziv` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `aerodrom`
--

INSERT INTO `aerodrom` (`id`, `grad`, `drzava`, `naziv`) VALUES
(1, 'Huangmei', 'China', 'Menomonie'),
(2, 'Khandagayty', 'Russia', 'High Crossing'),
(4, 'Beograd', 'Srbija', 'Nikola Tesla');

-- --------------------------------------------------------

--
-- Table structure for table `avion`
--

CREATE TABLE `avion` (
  `id` int(11) NOT NULL,
  `model` varchar(255) NOT NULL,
  `br_mesta` int(11) NOT NULL,
  `registarski_br` varchar(255) NOT NULL,
  `id_kompanije` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `avion`
--

INSERT INTO `avion` (`id`, `model`, `br_mesta`, `registarski_br`, `id_kompanije`) VALUES
(1, 'Aurora', 200, '284b1561-a001-4e9e-9391-6996cacfb135', 1);

-- --------------------------------------------------------

--
-- Table structure for table `groups`
--

CREATE TABLE `groups` (
  `username` varchar(255) NOT NULL,
  `groupname` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `groups`
--

INSERT INTO `groups` (`username`, `groupname`) VALUES
('admin', 'admin'),
('milos', 'user'),
('ana', 'user');

-- --------------------------------------------------------

--
-- Table structure for table `kompanija`
--

CREATE TABLE `kompanija` (
  `id` int(11) NOT NULL,
  `naziv_kompanije` varchar(255) NOT NULL,
  `drzava` varchar(255) NOT NULL,
  `sediste` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `kompanija`
--

INSERT INTO `kompanija` (`id`, `naziv_kompanije`, `drzava`, `sediste`) VALUES
(1, 'Avavee', 'China', 'Sunzhuang');

-- --------------------------------------------------------

--
-- Table structure for table `let`
--

CREATE TABLE `let` (
  `id` int(11) NOT NULL,
  `vreme_polaska` datetime NOT NULL,
  `vreme_dolaska` datetime NOT NULL,
  `aerodrom_id_polazak` int(11) NOT NULL,
  `aerodrom_id_dolazak` int(11) NOT NULL,
  `pilot` varchar(255) DEFAULT NULL,
  `avion_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `let`
--

INSERT INTO `let` (`id`, `vreme_polaska`, `vreme_dolaska`, `aerodrom_id_polazak`, `aerodrom_id_dolazak`, `pilot`, `avion_id`) VALUES
(1, '2018-06-08 07:00:00', '2018-06-08 10:00:00', 1, 2, 'milos', 1),
(2, '2018-07-12 10:00:00', '2018-07-12 13:00:00', 1, 2, 'milos', 1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`username`, `password`) VALUES
('admin', '8C6976E5B5410415BDE908BD4DEE15DFB167A9C873FC4BB8A81F6F2AB448A918'),
('ana', '24d4b96f58da6d4a8512313bbd02a28ebf0ca95dec6e4c86ef78ce7f01e788ac'),
('milos', '794B9FECF082D4273CA54046F7B8377BF523240B88566BDDBCFBF52194195123');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `aerodrom`
--
ALTER TABLE `aerodrom`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `avion`
--
ALTER TABLE `avion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `kompanija_avion` (`id_kompanije`);

--
-- Indexes for table `groups`
--
ALTER TABLE `groups`
  ADD KEY `username_group` (`username`);

--
-- Indexes for table `kompanija`
--
ALTER TABLE `kompanija`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `let`
--
ALTER TABLE `let`
  ADD PRIMARY KEY (`id`),
  ADD KEY `aerodrom_polazak` (`aerodrom_id_polazak`),
  ADD KEY `aerodrom_dolazak` (`aerodrom_id_dolazak`),
  ADD KEY `pilot` (`pilot`),
  ADD KEY `avion` (`avion_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `aerodrom`
--
ALTER TABLE `aerodrom`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `avion`
--
ALTER TABLE `avion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `kompanija`
--
ALTER TABLE `kompanija`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `let`
--
ALTER TABLE `let`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `avion`
--
ALTER TABLE `avion`
  ADD CONSTRAINT `kompanija_avion` FOREIGN KEY (`id_kompanije`) REFERENCES `kompanija` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `groups`
--
ALTER TABLE `groups`
  ADD CONSTRAINT `username_group` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `let`
--
ALTER TABLE `let`
  ADD CONSTRAINT `aerodrom_dolazak` FOREIGN KEY (`aerodrom_id_dolazak`) REFERENCES `aerodrom` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `aerodrom_polazak` FOREIGN KEY (`aerodrom_id_polazak`) REFERENCES `aerodrom` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `avion` FOREIGN KEY (`avion_id`) REFERENCES `avion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `pilot` FOREIGN KEY (`pilot`) REFERENCES `users` (`username`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
