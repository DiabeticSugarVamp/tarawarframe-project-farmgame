-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 06, 2024 at 11:56 PM
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
-- Database: `tarawarframedb`
--

-- --------------------------------------------------------

--
-- Table structure for table `savedgrowingbronze`
--

CREATE TABLE `savedgrowingbronze` (
  `save_slots` int(1) NOT NULL,
  `day_planted` int(100) NOT NULL,
  `grown_day` int(100) NOT NULL,
  `seed_planted_num` int(3) NOT NULL DEFAULT 0,
  `watered` int(1) NOT NULL DEFAULT 1,
  `day_watered` int(100) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `savedgrowinggold`
--

CREATE TABLE `savedgrowinggold` (
  `save_slots` int(1) NOT NULL,
  `day_planted` int(100) NOT NULL,
  `grown_day` int(100) NOT NULL,
  `seed_planted_num` int(3) NOT NULL DEFAULT 0,
  `watered` int(1) NOT NULL DEFAULT 1,
  `day_watered` int(100) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `savedgrowinggold`
--

INSERT INTO `savedgrowinggold` (`save_slots`, `day_planted`, `grown_day`, `seed_planted_num`, `watered`, `day_watered`) VALUES
(1, 1, 8, 1, 1, 3),
(3, 35, 42, 1, 1, 37);

-- --------------------------------------------------------

--
-- Table structure for table `savedgrowingsilver`
--

CREATE TABLE `savedgrowingsilver` (
  `save_slots` int(1) NOT NULL,
  `day_planted` int(100) NOT NULL,
  `grown_day` int(100) NOT NULL,
  `seed_planted_num` int(3) NOT NULL DEFAULT 0,
  `watered` int(1) NOT NULL DEFAULT 1,
  `day_watered` int(100) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `savedgrowingsilver`
--

INSERT INTO `savedgrowingsilver` (`save_slots`, `day_planted`, `grown_day`, `seed_planted_num`, `watered`, `day_watered`) VALUES
(1, 1, 5, 3, 1, 3),
(3, 35, 39, 1, 1, 37);

-- --------------------------------------------------------

--
-- Table structure for table `saveditems`
--

CREATE TABLE `saveditems` (
  `save_slots` int(1) NOT NULL DEFAULT 1,
  `seed_bronze` int(3) NOT NULL DEFAULT 0,
  `seed_silver` int(3) NOT NULL DEFAULT 0,
  `seed_gold` int(3) NOT NULL DEFAULT 0,
  `crop_bronze` int(3) NOT NULL DEFAULT 0,
  `crop_silver` int(3) NOT NULL DEFAULT 0,
  `crop_gold` int(3) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `saveditems`
--

INSERT INTO `saveditems` (`save_slots`, `seed_bronze`, `seed_silver`, `seed_gold`, `crop_bronze`, `crop_silver`, `crop_gold`) VALUES
(1, 0, 0, 0, 5, 0, 0),
(2, 5, 3, 1, 6, 2, 2),
(3, 41, 41, 40, 5, 2, 2);

-- --------------------------------------------------------

--
-- Table structure for table `savedreadytoharvest`
--

CREATE TABLE `savedreadytoharvest` (
  `save_slots` int(1) NOT NULL,
  `crop_bronze` int(3) NOT NULL,
  `crop_silver` int(3) NOT NULL,
  `crop_gold` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `savedreadytoharvest`
--

INSERT INTO `savedreadytoharvest` (`save_slots`, `crop_bronze`, `crop_silver`, `crop_gold`) VALUES
(1, 0, 0, 0),
(2, 0, 0, 0),
(3, 1, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `savedstats`
--

CREATE TABLE `savedstats` (
  `save_slots` int(1) NOT NULL,
  `user_id` int(6) NOT NULL,
  `avatar` text NOT NULL DEFAULT '/assets/imageDevAndre.png',
  `username` varchar(100) NOT NULL,
  `cur_day` int(100) NOT NULL DEFAULT 1,
  `cur_actions` int(2) NOT NULL DEFAULT 5,
  `cur_money` int(10) NOT NULL DEFAULT 100,
  `cur_deadline` int(2) NOT NULL DEFAULT 14,
  `time_stamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `savedstats`
--

INSERT INTO `savedstats` (`save_slots`, `user_id`, `avatar`, `username`, `cur_day`, `cur_actions`, `cur_money`, `cur_deadline`, `time_stamp`) VALUES
(1, 1, '/assets/imageDevAndre.png', 'CarlAngelo', 3, 2, 100, 12, '2024-06-06 12:08:47'),
(2, 2, '/assets/imageDevNat.png', 'Dreadlord', 12, 4, 100, 3, '2024-06-06 21:52:06'),
(3, 3, '/assets/imageDevAndre.png', 'Ahh yes', 37, 4, 953, 6, '2024-06-06 07:12:42');

-- --------------------------------------------------------

--
-- Table structure for table `tempgrowingbronze`
--

CREATE TABLE `tempgrowingbronze` (
  `day_planted` int(100) NOT NULL,
  `grown_day` int(100) NOT NULL,
  `seed_planted_num` int(3) NOT NULL DEFAULT 0,
  `watered` int(1) NOT NULL DEFAULT 1,
  `day_watered` int(100) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tempgrowinggold`
--

CREATE TABLE `tempgrowinggold` (
  `day_planted` int(100) NOT NULL,
  `grown_day` int(100) NOT NULL,
  `seed_planted_num` int(3) NOT NULL DEFAULT 0,
  `watered` int(1) NOT NULL DEFAULT 1,
  `day_watered` int(100) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tempgrowingsilver`
--

CREATE TABLE `tempgrowingsilver` (
  `day_planted` int(100) NOT NULL,
  `grown_day` int(100) NOT NULL,
  `seed_planted_num` int(3) NOT NULL DEFAULT 0,
  `watered` int(1) NOT NULL DEFAULT 1,
  `day_watered` int(100) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tempitems`
--

CREATE TABLE `tempitems` (
  `temp_id` int(2) NOT NULL DEFAULT 1,
  `seed_bronze` int(3) NOT NULL DEFAULT 5,
  `seed_silver` int(3) NOT NULL DEFAULT 3,
  `seed_gold` int(3) NOT NULL DEFAULT 1,
  `crop_bronze` int(3) NOT NULL DEFAULT 0,
  `crop_silver` int(3) NOT NULL DEFAULT 0,
  `crop_gold` int(3) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tempitems`
--

INSERT INTO `tempitems` (`temp_id`, `seed_bronze`, `seed_silver`, `seed_gold`, `crop_bronze`, `crop_silver`, `crop_gold`) VALUES
(1, 5, 3, 1, 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `temporarystatsholder`
--

CREATE TABLE `temporarystatsholder` (
  `user_id` int(6) NOT NULL,
  `avatar` text NOT NULL DEFAULT '/assets/imageDevAndre.png',
  `username` varchar(100) NOT NULL,
  `cur_day` int(100) NOT NULL DEFAULT 1,
  `cur_actions` int(2) NOT NULL DEFAULT 5,
  `cur_money` int(10) NOT NULL DEFAULT 100,
  `cur_deadline` int(2) NOT NULL DEFAULT 14,
  `time_stamp` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `temporarystatsholder`
--

INSERT INTO `temporarystatsholder` (`user_id`, `avatar`, `username`, `cur_day`, `cur_actions`, `cur_money`, `cur_deadline`, `time_stamp`) VALUES
(1, '/assets/imageDevIsrael.png', 'Test1', 1, 5, 100, 14, '2024-06-06 21:52:55');

-- --------------------------------------------------------

--
-- Table structure for table `tempreadytoharvest`
--

CREATE TABLE `tempreadytoharvest` (
  `temp_id` int(1) NOT NULL DEFAULT 1,
  `crop_bronze` int(3) NOT NULL,
  `crop_silver` int(3) NOT NULL,
  `crop_gold` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tempreadytoharvest`
--

INSERT INTO `tempreadytoharvest` (`temp_id`, `crop_bronze`, `crop_silver`, `crop_gold`) VALUES
(1, 0, 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `savedgrowingbronze`
--
ALTER TABLE `savedgrowingbronze`
  ADD PRIMARY KEY (`day_planted`);

--
-- Indexes for table `savedgrowinggold`
--
ALTER TABLE `savedgrowinggold`
  ADD PRIMARY KEY (`day_planted`);

--
-- Indexes for table `savedgrowingsilver`
--
ALTER TABLE `savedgrowingsilver`
  ADD PRIMARY KEY (`day_planted`);

--
-- Indexes for table `saveditems`
--
ALTER TABLE `saveditems`
  ADD PRIMARY KEY (`save_slots`);

--
-- Indexes for table `savedreadytoharvest`
--
ALTER TABLE `savedreadytoharvest`
  ADD PRIMARY KEY (`save_slots`);

--
-- Indexes for table `savedstats`
--
ALTER TABLE `savedstats`
  ADD PRIMARY KEY (`save_slots`);

--
-- Indexes for table `tempgrowingbronze`
--
ALTER TABLE `tempgrowingbronze`
  ADD PRIMARY KEY (`day_planted`);

--
-- Indexes for table `tempgrowinggold`
--
ALTER TABLE `tempgrowinggold`
  ADD PRIMARY KEY (`day_planted`);

--
-- Indexes for table `tempgrowingsilver`
--
ALTER TABLE `tempgrowingsilver`
  ADD PRIMARY KEY (`day_planted`);

--
-- Indexes for table `tempitems`
--
ALTER TABLE `tempitems`
  ADD PRIMARY KEY (`temp_id`);

--
-- Indexes for table `temporarystatsholder`
--
ALTER TABLE `temporarystatsholder`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `tempreadytoharvest`
--
ALTER TABLE `tempreadytoharvest`
  ADD PRIMARY KEY (`temp_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `temporarystatsholder`
--
ALTER TABLE `temporarystatsholder`
  MODIFY `user_id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
