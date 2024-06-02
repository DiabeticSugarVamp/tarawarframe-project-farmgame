-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 02, 2024 at 07:52 AM
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
-- Table structure for table `savedstats`
--

CREATE TABLE `savedstats` (
  `save_slots` int(1) NOT NULL,
  `user_id` int(6) NOT NULL,
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

INSERT INTO `savedstats` (`save_slots`, `user_id`, `username`, `cur_day`, `cur_actions`, `cur_money`, `cur_deadline`, `time_stamp`) VALUES
(1, 1, 'ambatunut', 1, 5, 100, 14, '2024-05-31 20:18:07'),
(2, 2, 'dorin', 25, 5, 50, 4, '2024-05-31 21:27:42'),
(3, 3, 'Ambatunut', 7, 5, 100, 8, '2024-05-31 20:55:13');

-- --------------------------------------------------------

--
-- Table structure for table `temporarystatsholder`
--

CREATE TABLE `temporarystatsholder` (
  `user_id` int(6) NOT NULL,
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

INSERT INTO `temporarystatsholder` (`user_id`, `username`, `cur_day`, `cur_actions`, `cur_money`, `cur_deadline`, `time_stamp`) VALUES
(1, 'ambatunut', 1, 5, 100, 14, '2024-06-01 09:20:08');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `savedstats`
--
ALTER TABLE `savedstats`
  ADD PRIMARY KEY (`save_slots`);

--
-- Indexes for table `temporarystatsholder`
--
ALTER TABLE `temporarystatsholder`
  ADD PRIMARY KEY (`user_id`);

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
