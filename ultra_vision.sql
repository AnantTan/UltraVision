-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 07, 2019 at 07:11 PM
-- Server version: 10.1.35-MariaDB
-- PHP Version: 7.2.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ultra_vision`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `unique_num` int(11) NOT NULL,
  `name` text,
  `subscription` enum('music_lover','video_lover','box_set','','premium') DEFAULT NULL,
  `points` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`unique_num`, `name`, `subscription`, `points`) VALUES
(39, 'Messi', 'music_lover', 0),
(40, 'Ronaldo', 'video_lover', 0),
(41, 'Beckham', 'box_set', 10),
(42, 'Pique', 'premium', 10);

-- --------------------------------------------------------

--
-- Table structure for table `inventory`
--

CREATE TABLE `inventory` (
  `id` int(11) NOT NULL,
  `title_type` enum('music','video','concert','box_set') DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `artist` varchar(50) DEFAULT NULL,
  `year_of_release` int(6) DEFAULT NULL,
  `media_format` enum('cd','dvd','blu_ray') DEFAULT NULL,
  `available` enum('yes','no') DEFAULT 'yes'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `inventory`
--

INSERT INTO `inventory` (`id`, `title_type`, `title`, `artist`, `year_of_release`, `media_format`, `available`) VALUES
(20, 'music', 'Love story', 'taylor swift', 2019, 'cd', 'no'),
(21, 'video', 'avengers', 'myself', 2017, 'blu_ray', 'yes'),
(22, 'concert', 'coldplay', 'Dublin', 2016, 'dvd', 'yes'),
(23, 'box_set', 'mr bean', 'disney', 2016, 'cd', 'yes');

-- --------------------------------------------------------

--
-- Table structure for table `rent`
--

CREATE TABLE `rent` (
  `rented_by` int(11) DEFAULT NULL,
  `rented` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `free` enum('yes','no') DEFAULT 'no'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `rent`
--

INSERT INTO `rent` (`rented_by`, `rented`, `date`, `free`) VALUES
(42, 20, '2019-05-07', 'no');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`unique_num`);

--
-- Indexes for table `inventory`
--
ALTER TABLE `inventory`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rent`
--
ALTER TABLE `rent`
  ADD UNIQUE KEY `rented` (`rented`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `unique_num` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT for table `inventory`
--
ALTER TABLE `inventory`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `rent`
--
ALTER TABLE `rent`
  ADD CONSTRAINT `rented` FOREIGN KEY (`rented`) REFERENCES `inventory` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
