-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Oct 13, 2026 at 01:07 PM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kebun_hidroponik`
--

-- --------------------------------------------------------

--
-- Table structure for table `tanaman`
--

CREATE TABLE `tanaman` (
  `id_tanaman` int NOT NULL,
  `kategori` varchar(10) NOT NULL,
  `nama_tanaman` varchar(100) NOT NULL,
  `grade_tanaman` varchar(50) NOT NULL,
  `sistem_irigasi` varchar(50) NOT NULL,
  `tingkat_kerenyahan` varchar(50) DEFAULT NULL,
  `masa_simpan` int DEFAULT NULL,
  `skala_brix` double DEFAULT NULL,
  `berbiji` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `tanaman`
--

INSERT INTO `tanaman` (`id_tanaman`, `kategori`, `nama_tanaman`, `grade_tanaman`, `sistem_irigasi`, `tingkat_kerenyahan`, `masa_simpan`, `skala_brix`, `berbiji`) VALUES
(1, 'Sayur', 'Selada Hijau', 'Grade A', 'NFT', 'Sangat Renyah', 7, NULL, NULL),
(2, 'Buah', 'Tomat Cherry', 'Grade B', 'Dutch Bucket', NULL, NULL, 10.5, 1),
(3, 'Buah', 'Melon', 'B', 'Tetes', NULL, NULL, 11, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tanaman`
--
ALTER TABLE `tanaman`
  ADD PRIMARY KEY (`id_tanaman`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
