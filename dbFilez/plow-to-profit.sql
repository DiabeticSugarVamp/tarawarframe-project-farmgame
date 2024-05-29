-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 16, 2024 at 11:44 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `plow-to-profit`
--

-- --------------------------------------------------------

--
-- Table structure for table `inventory`
--

CREATE TABLE `inventory` (
  `saveID` varchar(10) NOT NULL,
  `itemID` varchar(5) NOT NULL,
  `itemAmount` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `inventory`
--

INSERT INTO `inventory` (`saveID`, `itemID`, `itemAmount`) VALUES
('abcde12345', 'bCrop', 3),
('abcde12345', 'bSeed', 7),
('abcde12345', 'fLand', 20),
('abcde12345', 'gCrop', 1),
('abcde12345', 'gSeed', 4),
('abcde12345', 'sSeed', 5),
('vwxyz98765', 'bSeed', 7),
('vwxyz98765', 'fLand', 60),
('vwxyz98765', 'gCrop', 10),
('vwxyz98765', 'gGlov', 1),
('vwxyz98765', 'gSeed', 5),
('vwxyz98765', 'sCrop', 3),
('vwxyz98765', 'truck', 1);

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
  `itemID` varchar(5) NOT NULL,
  `itemName` varchar(50) DEFAULT NULL,
  `itemPrice` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`itemID`, `itemName`, `itemPrice`) VALUES
('bCrop', 'Bronze Crop', 5),
('bSeed', 'Bronze Seed', 5),
('fLand', 'Farmland++', 200),
('gCrop', 'Gold Crop', 20),
('gGlov', 'Midas Golden Farm Gloves', 500),
('gSeed', 'Gold Seed', 20),
('sCrop', 'Silver Crop', 10),
('sSeed', 'Silver Seed', 10),
('swCan', 'Super Watering Can', 100),
('trctr', 'Tractor', 255),
('truck', 'Trusty Truck', 255),
('win', 'Mansion', 25000);

-- --------------------------------------------------------

--
-- Table structure for table `profile`
--

CREATE TABLE `profile` (
  `saveID` varchar(10) NOT NULL,
  `saveDoC` date DEFAULT NULL,
  `userName` varchar(20) DEFAULT NULL,
  `userMoney` double DEFAULT NULL,
  `userActions` int(11) DEFAULT NULL,
  `gameDay` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `profile`
--

INSERT INTO `profile` (`saveID`, `saveDoC`, `userName`, `userMoney`, `userActions`, `gameDay`) VALUES
('abcde12345', '2001-09-11', 'I-Dont-Have-A-Dad', 20, 7, 75),
('vwxyz98765', '2001-09-11', 'BigDaddy', 420, 12, 69);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `inventory`
--
ALTER TABLE `inventory`
  ADD PRIMARY KEY (`saveID`,`itemID`),
  ADD KEY `itemID` (`itemID`);

--
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`itemID`);

--
-- Indexes for table `profile`
--
ALTER TABLE `profile`
  ADD PRIMARY KEY (`saveID`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `inventory`
--
ALTER TABLE `inventory`
  ADD CONSTRAINT `inventory_ibfk_1` FOREIGN KEY (`saveID`) REFERENCES `profile` (`saveID`),
  ADD CONSTRAINT `inventory_ibfk_2` FOREIGN KEY (`itemID`) REFERENCES `items` (`itemID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
