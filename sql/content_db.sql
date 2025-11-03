-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: content_db
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `banners`
--

DROP TABLE IF EXISTS `banners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `banners` (
  `active` bit(1) NOT NULL,
  `display_order` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `start_date` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `title` varchar(150) NOT NULL,
  `subtitle` varchar(300) DEFAULT NULL,
  `image_key` varchar(255) NOT NULL,
  `redirect_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKh3uo8owpdprry5hiybetwbh37` (`display_order`,`active`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `banners`
--

LOCK TABLES `banners` WRITE;
/*!40000 ALTER TABLE `banners` DISABLE KEYS */;
INSERT INTO `banners` VALUES (_binary '',1,'2025-10-07 20:46:16.656065',NULL,1,NULL,'2025-10-11 21:27:19.652719','Avatar: The Way of Water update','Trải nghiệm hành tinh Pandora trong phần tiếp theo đầy kịch tính update','banners/11cb669f-e2a1-4015-aaf9-9318d99837aa-621bf177-db71-4819-b30a-4ba3342b0b1e-banner1.jpg',''),(_binary '',2,'2025-10-07 21:05:09.276087',NULL,2,NULL,NULL,'Cục vàng của ngoại','Hành trình kỳ thú của cục vàng mang đến niềm vui cho cả gia đình','banners/d9639a32-9d9f-4f27-a28f-6c34b1275a4c-11730b6e-f981-452d-890e-c5133a61f85e-banner3.jpg',NULL),(_binary '',3,'2025-10-07 21:08:31.315389',NULL,3,NULL,'2025-10-11 21:27:23.824496','Chú Thuật Hồi Chiến: Hoài Ngọc Ngọc Chiết','Phiêu lưu mới của các pháp sư trong thế giới đầy phép thuật','banners/00f7573b-835b-4975-b2e2-07b142317f82-84842738-f8d0-401f-b216-3916807bb603-banner2.jpg','/booking/10');
/*!40000 ALTER TABLE `banners` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-03 14:46:21
