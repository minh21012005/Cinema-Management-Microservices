-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cinema_db
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
-- Table structure for table `cinemas`
--

DROP TABLE IF EXISTS `cinemas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cinemas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cinemas`
--

LOCK TABLES `cinemas` WRITE;
/*!40000 ALTER TABLE `cinemas` DISABLE KEYS */;
INSERT INTO `cinemas` VALUES (6,'CGV Hoàn Kiếm','Hoan kiem','Hn','0987456321',1,'2025-09-18 16:50:13.311575','2025-10-09 15:08:07.520714'),(7,'Galaxy Ba Đình','Ba Đình','Ha noi','0846442989',1,'2025-09-18 16:50:28.382628','2025-10-09 15:08:17.764833'),(8,'Lotte Cầu Giấy','Cau giay','Ha noi','0876452981',1,'2025-09-19 10:11:32.115473','2025-10-09 15:08:25.395063'),(9,'CGV update 2','Cau giay update 2','Ha noi','0876442989',0,'2025-09-19 10:34:11.634186','2025-10-09 15:07:43.261274'),(10,'CGV update 1','Cau giay update 1','Ha noi','0876442980',0,'2025-09-19 10:35:14.353925','2025-10-09 15:07:44.553407'),(11,'CGV update 4','Cau giay update 4','Ha noi','0846443989',0,'2025-09-19 10:41:20.860439','2025-10-09 15:07:46.163300'),(12,'CGV 5','hcm','Hà Nội','0974798105',0,'2025-09-19 10:55:29.029220','2025-10-09 15:07:47.857610'),(13,'CGV 6','test','Hà Nội','0974748105',0,'2025-09-23 08:16:16.077547','2025-10-28 09:21:57.844825');
/*!40000 ALTER TABLE `cinemas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `combo_food`
--

DROP TABLE IF EXISTS `combo_food`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `combo_food` (
  `combo_id` bigint NOT NULL,
  `food_id` bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `quantity` int NOT NULL,
  KEY `FK6kjqog43a6va7aygl55ac1o05` (`food_id`),
  KEY `FKqm0j7h9rombps574n647jaiwd` (`combo_id`),
  CONSTRAINT `FK6kjqog43a6va7aygl55ac1o05` FOREIGN KEY (`food_id`) REFERENCES `foods` (`id`),
  CONSTRAINT `FKqm0j7h9rombps574n647jaiwd` FOREIGN KEY (`combo_id`) REFERENCES `combos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `combo_food`
--

LOCK TABLES `combo_food` WRITE;
/*!40000 ALTER TABLE `combo_food` DISABLE KEYS */;
INSERT INTO `combo_food` VALUES (1,1,NULL,NULL,1),(1,2,NULL,NULL,2),(2,1,NULL,NULL,1),(2,2,NULL,NULL,1),(3,1,NULL,NULL,1),(3,2,NULL,NULL,1),(3,5,NULL,NULL,1);
/*!40000 ALTER TABLE `combo_food` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `combos`
--

DROP TABLE IF EXISTS `combos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `combos` (
  `available` bit(1) NOT NULL,
  `price` double NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `code` varchar(20) NOT NULL,
  `image_key` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7rlxg49bigur1bd3unh54mhjx` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `combos`
--

LOCK TABLES `combos` WRITE;
/*!40000 ALTER TABLE `combos` DISABLE KEYS */;
INSERT INTO `combos` VALUES (_binary '',120000,1,NULL,'Combo 2 nước một bắp (2 nước siêu lớn +1 bắp lớn)','2025-09-29 10:44:59.562809','2025-10-13 16:27:30.663494','CB001','combos/ddf61f83-6853-4e10-a8b1-7ee02812f577-530a9381-a5ff-4dfb-8ec0-53a726d01c89-gia-bap-nuoc-cgv-1.jpg'),(_binary '',105000,2,NULL,'My Combo(Combo 1 bắp lớn + 1 nước siêu lớn)','2025-09-29 10:50:41.742214','2025-10-13 16:27:12.107490','CB002','combos/54efa132-6ddd-48b2-a21a-cf78b8e20433-07e64dce-b571-425d-9fa3-598df2ec6173-MP2010230001_BASIC_origin.jpg'),(_binary '',110000,3,NULL,'Premium My Combo','2025-09-29 12:03:20.045506','2025-09-29 19:46:19.778605','CB003','combos/fbb4940f-2893-403c-bae7-f31bb435817e-1befa686-5655-4052-9236-feefe6337d4a-KICHI_VOUCHER_350x495.jpg');
/*!40000 ALTER TABLE `combos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `food_type`
--

DROP TABLE IF EXISTS `food_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `code` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK84l4wyt6ljt8bockly2pt1bg4` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food_type`
--

LOCK TABLES `food_type` WRITE;
/*!40000 ALTER TABLE `food_type` DISABLE KEYS */;
INSERT INTO `food_type` VALUES (1,NULL,NULL,'POPCORN','Bắp rang bơ truyền thống, vị mặn ngọt','Bắp rang bơ',_binary ''),(2,NULL,NULL,'DRINK','Các loại nước ngọt giải khát, có ga hoặc không có ga','Nước ngọt',_binary ''),(3,NULL,NULL,'SNACK','Các loại snack như khoai tây chiên, bánh quy, hạt dưa','Đồ ăn vặt',_binary ''),(4,NULL,NULL,'HOTDOG','Xúc xích và các món kèm theo','Xúc xích',_binary ''),(5,NULL,NULL,'ICECREAM','Các loại kem mát lạnh, nhiều vị khác nhau','Kem',_binary '');
/*!40000 ALTER TABLE `food_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `foods`
--

DROP TABLE IF EXISTS `foods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `foods` (
  `available` bit(1) NOT NULL,
  `price` double NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `code` varchar(20) NOT NULL,
  `image_key` varchar(255) NOT NULL,
  `food_type_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK95bmnt3ir39rhf36evcgwf1u9` (`code`),
  KEY `FKkk6rr7axld2grpib53q53spd1` (`food_type_id`),
  CONSTRAINT `FKkk6rr7axld2grpib53q53spd1` FOREIGN KEY (`food_type_id`) REFERENCES `food_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `foods`
--

LOCK TABLES `foods` WRITE;
/*!40000 ALTER TABLE `foods` DISABLE KEYS */;
INSERT INTO `foods` VALUES (_binary '',60000,1,NULL,'Bắp rang bơ','2025-09-28 11:09:26.960175',NULL,'F001','foods/97cde937-bac9-405f-aafe-15dee3f6d4b6-5291f087-e592-4a09-b079-52e2a4047b06-efbee8fcfdd416b066b4437a57b09f10.jpg',1),(_binary '',25000,2,NULL,'Cocacola','2025-09-28 11:23:07.057171','2025-10-13 16:27:02.078285','F002','foods/c3fc795c-ab17-47a1-b553-e1ae85da6641-bf0d7eb5-6d76-486a-b151-05ead7111b7d-master.png',2),(_binary '',35000,3,'Trà vải ngon nhất thế giới','Trà vải','2025-09-28 11:28:51.977579','2025-10-13 16:27:22.671504','F003','foods/6da3a0f4-ce18-4b1c-b5d3-8a2fd314849c-e22344a7-04bd-4b79-b854-e0108feabc89-515938402_10163755180305625_1909812582788978960_n.jpg',2),(_binary '',20000,4,NULL,'Xúc xích','2025-09-28 20:05:12.575115','2025-09-29 19:45:46.153201','F004','foods/3b628b92-cb21-4038-822a-d804fd388286-c13bfc90-d821-41b1-a185-c946db3083b4-ti_xung.jpg',4),(_binary '',45000,5,NULL,'Cơm cháy','2025-09-29 12:02:51.469556','2025-09-29 12:11:08.131641','F005','foods/c93d9b8c-6b25-4560-8816-69fe17f985df-ca2f7aca-01fa-40e8-8058-65e10a931012-487807919_1129616589205110_6220160359522369111_n.jpg',3),(_binary '',70000,6,NULL,'Mandu (nhân hải sản và nhân thịt heo)','2025-10-02 10:17:46.553892',NULL,'FD006','foods/7557c2ee-aedd-42c6-89e8-bb2340acbe7f-e549362a-ede4-491d-9a48-2b93c3e70dc0-2025_Mandu_Mi__N_O_350x495.png',3);
/*!40000 ALTER TABLE `foods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_types`
--

DROP TABLE IF EXISTS `room_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_types` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  UNIQUE KEY `UKpwg4oqr4ylc28g701vfys5hc4` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_types`
--

LOCK TABLES `room_types` WRITE;
/*!40000 ALTER TABLE `room_types` DISABLE KEYS */;
INSERT INTO `room_types` VALUES (1,'STANDARD','Phòng Standard',NULL,NULL,NULL),(2,'VIP','Phòng VIP',NULL,NULL,NULL),(3,'IMAX','Phòng IMAX',NULL,NULL,NULL),(4,'OTHER','Khác',NULL,NULL,NULL);
/*!40000 ALTER TABLE `room_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
  `active` bit(1) NOT NULL,
  `cinema_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `room_type_id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjp9bjtvlojbw581bpq23cpw4j` (`cinema_id`),
  KEY `FKh9m2n1paq5hmd3u0klfl7wsfv` (`room_type_id`),
  CONSTRAINT `FKh9m2n1paq5hmd3u0klfl7wsfv` FOREIGN KEY (`room_type_id`) REFERENCES `room_types` (`id`),
  CONSTRAINT `FKjp9bjtvlojbw581bpq23cpw4j` FOREIGN KEY (`cinema_id`) REFERENCES `cinemas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES (_binary '',6,1,1,'Room 1','2025-09-19 16:10:44.649850','2025-09-23 07:57:36.476054'),(_binary '',6,2,2,'Room 2','2025-09-19 20:21:02.920631','2025-10-01 10:35:07.413376'),(_binary '',6,3,3,'Room 3','2025-09-20 10:22:52.204726','2025-09-23 08:17:59.824430'),(_binary '',7,4,1,'Room 1','2025-09-22 15:26:07.227528','2025-10-09 11:20:30.442515'),(_binary '',8,5,2,'Room 1','2025-10-09 11:21:25.491552',NULL);
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seat_types`
--

DROP TABLE IF EXISTS `seat_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seat_types` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `base_price` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seat_types`
--

LOCK TABLES `seat_types` WRITE;
/*!40000 ALTER TABLE `seat_types` DISABLE KEYS */;
INSERT INTO `seat_types` VALUES (1,'Thường',NULL,NULL,80000),(2,'VIP',NULL,NULL,120000),(3,'Đôi',NULL,NULL,150000);
/*!40000 ALTER TABLE `seat_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seats`
--

DROP TABLE IF EXISTS `seats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seats` (
  `active` bit(1) NOT NULL,
  `col_index` int NOT NULL,
  `row_index` int NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `room_id` bigint NOT NULL,
  `seat_type_id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKemrcxvfyp670ks7utgrqru9n3` (`room_id`,`row_index`,`col_index`),
  KEY `FKcp43h11k99445lq681irnensp` (`seat_type_id`),
  CONSTRAINT `FKcp43h11k99445lq681irnensp` FOREIGN KEY (`seat_type_id`) REFERENCES `seat_types` (`id`),
  CONSTRAINT `FKg993pi7ucgy616icmddq8u335` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seats`
--

LOCK TABLES `seats` WRITE;
/*!40000 ALTER TABLE `seats` DISABLE KEYS */;
INSERT INTO `seats` VALUES (_binary '',0,0,1,1,1,'A1','2025-09-19 16:10:44.691588','2025-09-23 08:18:10.483854'),(_binary '',1,0,2,1,1,'A2','2025-09-19 16:10:44.696931','2025-09-23 08:18:13.308459'),(_binary '',2,0,3,1,1,'A3','2025-09-19 16:10:44.698950','2025-09-23 08:18:15.312283'),(_binary '',3,0,4,1,1,'A4','2025-09-19 16:10:44.701950','2025-09-23 08:18:17.564500'),(_binary '',0,1,5,1,2,'B1','2025-09-19 16:10:44.704556','2025-09-23 08:18:28.987870'),(_binary '',1,1,6,1,2,'B2','2025-09-19 16:10:44.707074','2025-09-23 08:18:30.865509'),(_binary '',2,1,7,1,3,'B3-4','2025-09-19 16:10:44.708517','2025-09-23 08:18:32.776280'),(_binary '',0,0,8,2,3,'A1-2','2025-09-19 20:21:02.966038','2025-10-09 11:21:42.705034'),(_binary '',0,1,9,2,3,'B1-2','2025-09-19 20:21:02.970331','2025-10-09 11:21:44.677879'),(_binary '',0,0,10,3,1,'A1','2025-09-20 10:22:52.224250','2025-09-30 21:31:50.632079'),(_binary '',1,0,11,3,1,'A2','2025-09-20 10:22:52.237115','2025-09-30 21:32:02.737482'),(_binary '',2,0,12,3,1,'A3','2025-09-20 10:22:52.238739','2025-09-30 21:32:04.926931'),(_binary '',3,0,13,3,1,'A4','2025-09-20 10:22:52.239754','2025-09-30 21:32:06.699922'),(_binary '',4,0,14,3,1,'A5','2025-09-20 10:22:52.240753','2025-09-30 21:32:08.648451'),(_binary '',5,0,15,3,1,'A6','2025-09-20 10:22:52.241754','2025-09-30 21:32:10.482473'),(_binary '',6,0,16,3,1,'A7','2025-09-20 10:22:52.242753','2025-09-30 21:32:12.539518'),(_binary '',7,0,17,3,1,'A8','2025-09-20 10:22:52.244256','2025-09-30 21:32:15.501239'),(_binary '',8,0,18,3,1,'A9','2025-09-20 10:22:52.245261','2025-09-30 21:32:17.708140'),(_binary '',9,0,19,3,1,'A10','2025-09-20 10:22:52.246268','2025-09-30 21:32:19.680788'),(_binary '',8,1,20,3,1,'B9','2025-09-20 10:22:52.247269','2025-09-30 21:32:38.366314'),(_binary '',7,1,21,3,1,'B8','2025-09-20 10:22:52.248266','2025-09-30 21:32:36.383238'),(_binary '',9,1,22,3,1,'B10','2025-09-20 10:22:52.249266','2025-09-30 21:32:40.833131'),(_binary '',6,1,23,3,1,'B7','2025-09-20 10:22:52.250266','2025-09-30 21:32:34.554574'),(_binary '',5,1,24,3,1,'B6','2025-09-20 10:22:52.251268','2025-09-30 21:32:32.356750'),(_binary '',3,1,25,3,1,'B4','2025-09-20 10:22:52.252266','2025-09-30 21:32:28.326750'),(_binary '',4,1,26,3,1,'B5','2025-09-20 10:22:52.252266','2025-09-30 21:32:30.349126'),(_binary '',2,1,27,3,1,'B3','2025-09-20 10:22:52.254401','2025-09-30 21:32:26.209396'),(_binary '',0,1,28,3,1,'B1','2025-09-20 10:22:52.255407','2025-09-30 21:32:21.745183'),(_binary '',1,1,29,3,1,'B2','2025-09-20 10:22:52.256492','2025-09-30 21:32:24.329199'),(_binary '',0,2,30,3,1,'C1','2025-09-20 10:22:52.257735','2025-09-30 21:32:42.722654'),(_binary '',1,2,31,3,1,'C2','2025-09-20 10:22:52.257735','2025-09-30 21:32:44.453192'),(_binary '',2,2,32,3,1,'C3','2025-09-20 10:22:52.259160','2025-09-30 21:32:46.288815'),(_binary '',7,2,33,3,1,'C8','2025-09-20 10:22:52.260167','2025-09-30 21:32:55.613775'),(_binary '',8,2,34,3,1,'C9','2025-09-20 10:22:52.261166','2025-09-30 21:32:57.608896'),(_binary '',9,2,35,3,1,'C10','2025-09-20 10:22:52.262172','2025-09-30 21:32:59.716300'),(_binary '',3,2,36,3,2,'C4','2025-09-20 10:22:52.263677','2025-09-30 21:32:48.090968'),(_binary '',4,2,37,3,2,'C5','2025-09-20 10:22:52.265889','2025-09-30 21:32:49.942037'),(_binary '',5,2,38,3,2,'C6','2025-09-20 10:22:52.266904','2025-09-30 21:32:52.006381'),(_binary '',6,2,39,3,2,'C7','2025-09-20 10:22:52.268073','2025-09-30 21:32:53.967999'),(_binary '',3,3,40,3,2,'D4','2025-09-20 10:22:52.269546','2025-09-30 21:33:09.734965'),(_binary '',4,3,41,3,2,'D5','2025-09-20 10:22:52.271230','2025-09-30 21:33:11.638338'),(_binary '',5,3,42,3,2,'D6','2025-09-20 10:22:52.272559','2025-09-30 21:33:15.844622'),(_binary '',6,3,43,3,2,'D7','2025-09-20 10:22:52.275028','2025-09-30 21:33:13.827442'),(_binary '',7,3,44,3,2,'D8','2025-09-20 10:22:52.277042','2025-09-30 21:33:17.766010'),(_binary '',8,3,45,3,2,'D9','2025-09-20 10:22:52.278272','2025-09-30 21:33:20.030896'),(_binary '',0,4,46,3,2,'E1','2025-09-20 10:22:52.281280','2025-09-30 21:33:24.369904'),(_binary '',1,4,47,3,2,'E2','2025-09-20 10:22:52.282682','2025-09-30 21:33:26.367602'),(_binary '',2,4,48,3,2,'E3','2025-09-20 10:22:52.284928','2025-09-30 21:33:28.852268'),(_binary '',3,4,49,3,2,'E4','2025-09-20 10:22:52.286592','2025-09-30 21:33:30.642597'),(_binary '',4,4,50,3,2,'E5','2025-09-20 10:22:52.287731','2025-09-30 21:33:32.636091'),(_binary '',7,4,51,3,2,'E8','2025-09-20 10:22:52.288730','2025-09-30 21:33:38.292376'),(_binary '',5,4,52,3,2,'E6','2025-09-20 10:22:52.290119','2025-09-30 21:33:34.342361'),(_binary '',6,4,53,3,2,'E7','2025-09-20 10:22:52.291344','2025-09-30 21:33:36.117966'),(_binary '',9,4,54,3,2,'E10','2025-09-20 10:22:52.292858','2025-09-30 21:33:42.191396'),(_binary '',8,4,55,3,2,'E9','2025-09-20 10:22:52.293859','2025-09-30 21:33:40.105176'),(_binary '',9,3,56,3,2,'D10','2025-09-20 10:22:52.295373','2025-09-30 21:33:22.104407'),(_binary '',1,3,57,3,2,'D2','2025-09-20 10:22:52.297886','2025-09-30 21:33:06.101165'),(_binary '',2,3,58,3,2,'D3','2025-09-20 10:22:52.298888','2025-09-30 21:33:07.867497'),(_binary '',0,3,59,3,2,'D1','2025-09-20 10:22:52.300979','2025-09-30 21:33:03.793459'),(_binary '',0,5,60,3,3,'F1-2','2025-09-20 10:22:52.302736','2025-09-30 21:33:44.655267'),(_binary '',2,5,61,3,3,'F3-4','2025-09-20 10:22:52.303743','2025-09-30 21:33:46.769606'),(_binary '',4,5,62,3,3,'F5-6','2025-09-20 10:22:52.304855','2025-09-30 21:33:48.989113'),(_binary '',6,5,63,3,3,'F7-8','2025-09-20 10:22:52.305361','2025-09-30 21:33:51.689563'),(_binary '',8,5,64,3,3,'F9-10','2025-09-20 10:22:52.306372','2025-09-30 21:33:53.418592'),(_binary '',4,0,65,1,1,'A5','2025-09-20 11:26:21.889377','2025-09-23 08:18:19.459187'),(_binary '',4,1,66,1,3,'B5-6','2025-09-20 11:26:37.466046','2025-09-23 08:18:34.813525'),(_binary '',6,0,67,1,2,'A7','2025-09-20 11:26:46.608901','2025-09-23 08:18:24.852618'),(_binary '',5,0,68,1,1,'A6','2025-09-20 11:27:10.987536','2025-09-23 08:18:21.975972'),(_binary '',6,1,69,1,3,'B7-8','2025-09-20 11:28:09.447787','2025-09-23 08:18:36.908783'),(_binary '',7,0,70,1,1,'A8','2025-09-20 11:51:54.353884','2025-09-23 08:18:26.822028'),(_binary '',0,0,71,4,1,'A1','2025-09-22 15:26:07.264193','2025-10-09 11:20:38.888792'),(_binary '',0,0,72,5,1,'A1','2025-10-09 11:21:25.491552',NULL),(_binary '',1,0,73,5,1,'A2','2025-10-09 11:21:25.499434',NULL),(_binary '',2,0,74,5,1,'A3','2025-10-09 11:21:25.500940',NULL),(_binary '',3,0,75,5,1,'A4','2025-10-09 11:21:25.501945',NULL),(_binary '',4,0,76,5,1,'A5','2025-10-09 11:21:25.503241',NULL),(_binary '',6,0,77,5,1,'A7','2025-10-09 11:21:25.504255',NULL),(_binary '',5,0,78,5,1,'A6','2025-10-09 11:21:25.505255',NULL),(_binary '',7,0,79,5,1,'A8','2025-10-09 11:21:25.506255',NULL),(_binary '',8,0,80,5,1,'A9','2025-10-09 11:21:25.506255',NULL),(_binary '',9,0,81,5,1,'A10','2025-10-09 11:21:25.507254',NULL),(_binary '',9,1,82,5,1,'B10','2025-10-09 11:21:25.508255',NULL),(_binary '',8,1,83,5,1,'B9','2025-10-09 11:21:25.508255',NULL),(_binary '',7,1,84,5,1,'B8','2025-10-09 11:21:25.509645',NULL),(_binary '',6,1,85,5,1,'B7','2025-10-09 11:21:25.510649',NULL),(_binary '',5,1,86,5,1,'B6','2025-10-09 11:21:25.510649',NULL),(_binary '',3,1,87,5,1,'B4','2025-10-09 11:21:25.511797',NULL),(_binary '',4,1,88,5,1,'B5','2025-10-09 11:21:25.512804',NULL),(_binary '',2,1,89,5,1,'B3','2025-10-09 11:21:25.513472',NULL),(_binary '',0,1,90,5,1,'B1','2025-10-09 11:21:25.514841',NULL),(_binary '',1,1,91,5,1,'B2','2025-10-09 11:21:25.515875',NULL),(_binary '',0,2,92,5,2,'C1','2025-10-09 11:21:25.516881',NULL),(_binary '',1,2,93,5,2,'C2','2025-10-09 11:21:25.516881',NULL),(_binary '',2,2,94,5,2,'C3','2025-10-09 11:21:25.516881',NULL),(_binary '',3,2,95,5,2,'C4','2025-10-09 11:21:25.519153',NULL),(_binary '',4,2,96,5,2,'C5','2025-10-09 11:21:25.520669',NULL),(_binary '',5,2,97,5,2,'C6','2025-10-09 11:21:25.521676',NULL),(_binary '',6,2,98,5,2,'C7','2025-10-09 11:21:25.522674',NULL),(_binary '',7,2,99,5,2,'C8','2025-10-09 11:21:25.523676',NULL),(_binary '',9,2,100,5,2,'C10','2025-10-09 11:21:25.523676',NULL),(_binary '',9,3,101,5,2,'D10','2025-10-09 11:21:25.524675',NULL),(_binary '',8,2,102,5,2,'C9','2025-10-09 11:21:25.525676',NULL),(_binary '',8,3,103,5,2,'D9','2025-10-09 11:21:25.525676',NULL),(_binary '',7,3,104,5,2,'D8','2025-10-09 11:21:25.526675',NULL),(_binary '',6,3,105,5,2,'D7','2025-10-09 11:21:25.527674',NULL),(_binary '',4,3,106,5,2,'D5','2025-10-09 11:21:25.528702',NULL),(_binary '',3,3,107,5,2,'D4','2025-10-09 11:21:25.528702',NULL),(_binary '',2,3,108,5,2,'D3','2025-10-09 11:21:25.529768',NULL),(_binary '',1,3,109,5,2,'D2','2025-10-09 11:21:25.530769',NULL),(_binary '',0,3,110,5,2,'D1','2025-10-09 11:21:25.531779',NULL),(_binary '',5,3,111,5,2,'D6','2025-10-09 11:21:25.532787',NULL),(_binary '',0,4,112,5,2,'E1','2025-10-09 11:21:25.532787',NULL),(_binary '',1,4,113,5,2,'E2','2025-10-09 11:21:25.534786',NULL),(_binary '',2,4,114,5,2,'E3','2025-10-09 11:21:25.536916',NULL),(_binary '',3,4,115,5,2,'E4','2025-10-09 11:21:25.537916',NULL),(_binary '',4,4,116,5,2,'E5','2025-10-09 11:21:25.540032',NULL),(_binary '',6,4,117,5,2,'E7','2025-10-09 11:21:25.540538',NULL),(_binary '',5,4,118,5,2,'E6','2025-10-09 11:21:25.541543',NULL),(_binary '',8,4,119,5,2,'E9','2025-10-09 11:21:25.542596',NULL),(_binary '',9,4,120,5,2,'E10','2025-10-09 11:21:25.543742',NULL),(_binary '',7,4,121,5,2,'E8','2025-10-09 11:21:25.543742',NULL),(_binary '',0,5,122,5,3,'F1-2','2025-10-09 11:21:25.545200',NULL),(_binary '',2,5,123,5,3,'F3-4','2025-10-09 11:21:25.546208',NULL),(_binary '',6,5,124,5,3,'F7-8','2025-10-09 11:21:25.547208',NULL),(_binary '',4,5,125,5,3,'F5-6','2025-10-09 11:21:25.548207',NULL),(_binary '',8,5,126,5,3,'F9-10','2025-10-09 11:21:25.549206',NULL);
/*!40000 ALTER TABLE `seats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `showtimes`
--

DROP TABLE IF EXISTS `showtimes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `showtimes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `end_time` datetime(6) NOT NULL,
  `movie_id` bigint DEFAULT NULL,
  `start_time` datetime(6) NOT NULL,
  `room_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrumrrbei9jppryk4teoyoetit` (`room_id`),
  CONSTRAINT `FKrumrrbei9jppryk4teoyoetit` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `showtimes`
--

LOCK TABLES `showtimes` WRITE;
/*!40000 ALTER TABLE `showtimes` DISABLE KEYS */;
INSERT INTO `showtimes` VALUES (1,'2025-09-25 09:37:09.089251','2025-09-25 09:51:35.384655',_binary '','2025-09-30 09:57:00.000000',1,'2025-09-30 08:00:00.000000',1),(2,'2025-10-01 10:33:45.353636',NULL,_binary '','2025-10-06 15:01:00.000000',2,'2025-10-06 12:00:00.000000',1),(3,'2025-10-01 10:34:52.434180','2025-10-01 12:12:25.576366',_binary '','2025-10-06 16:57:00.000000',1,'2025-10-06 15:00:00.000000',3),(4,'2025-10-01 10:35:26.010241',NULL,_binary '','2025-10-05 22:05:00.000000',6,'2025-10-05 20:00:00.000000',2),(5,'2025-10-01 10:38:42.484312',NULL,_binary '','2025-10-05 11:01:00.000000',2,'2025-10-05 08:00:00.000000',1),(6,'2025-10-01 12:09:12.272153',NULL,_binary '','2025-10-06 20:01:00.000000',2,'2025-10-06 17:00:00.000000',1),(7,'2025-10-01 12:10:18.679688','2025-10-01 12:43:52.259536',_binary '','2025-10-05 18:57:00.000000',1,'2025-10-05 17:00:00.000000',3),(8,'2025-10-09 11:17:45.985479',NULL,_binary '','2025-10-23 18:50:00.000000',10,'2025-10-23 17:00:00.000000',1),(9,'2025-10-09 11:18:01.944902',NULL,_binary '','2025-10-23 21:50:00.000000',10,'2025-10-23 20:00:00.000000',1),(10,'2025-10-09 11:18:31.889926',NULL,_binary '','2025-10-19 15:50:00.000000',10,'2025-10-19 14:00:00.000000',2),(11,'2025-10-09 11:18:57.483955',NULL,_binary '','2025-10-19 16:50:00.000000',10,'2025-10-19 15:00:00.000000',3),(12,'2025-10-09 11:22:14.714587',NULL,_binary '','2025-10-23 18:50:00.000000',10,'2025-10-23 17:00:00.000000',4),(13,'2025-10-09 11:22:34.005380',NULL,_binary '','2025-10-19 14:50:00.000000',10,'2025-10-19 13:00:00.000000',4),(14,'2025-10-09 11:22:51.347637',NULL,_binary '','2025-10-19 14:50:00.000000',10,'2025-10-19 13:00:00.000000',5),(15,'2025-10-09 11:23:04.178282',NULL,_binary '','2025-10-23 19:50:00.000000',10,'2025-10-23 18:00:00.000000',5),(16,'2025-10-09 14:23:42.295703',NULL,_binary '','2025-10-23 09:50:00.000000',10,'2025-10-23 08:00:00.000000',4),(17,'2025-10-09 14:24:03.176118',NULL,_binary '','2025-10-23 11:50:00.000000',10,'2025-10-23 10:00:00.000000',5),(18,'2025-10-09 14:25:04.535103',NULL,_binary '','2025-10-23 16:50:00.000000',10,'2025-10-23 15:00:00.000000',1),(19,'2025-10-13 22:02:49.562979',NULL,_binary '','2025-10-21 21:50:00.000000',10,'2025-10-21 20:00:00.000000',3),(20,'2025-10-28 09:22:26.647675',NULL,_binary '','2025-10-31 22:49:00.000000',22,'2025-10-31 20:00:00.000000',5),(21,'2025-10-28 14:49:47.993828',NULL,_binary '','2025-10-28 22:49:00.000000',22,'2025-10-28 20:00:00.000000',5),(22,'2025-10-28 14:50:13.609859',NULL,_binary '','2025-10-28 22:49:00.000000',22,'2025-10-28 20:00:00.000000',3),(23,'2025-10-29 09:54:34.205686',NULL,_binary '','2025-10-29 21:43:00.000000',16,'2025-10-29 20:00:00.000000',1),(24,'2025-10-29 09:54:58.716302',NULL,_binary '','2025-10-29 20:02:00.000000',18,'2025-10-29 18:00:00.000000',2),(25,'2025-10-29 09:55:31.267130',NULL,_binary '','2025-10-29 19:28:00.000000',3,'2025-10-29 17:00:00.000000',3),(26,'2025-10-29 09:55:58.454869',NULL,_binary '','2025-10-29 21:58:00.000000',19,'2025-10-29 20:00:00.000000',3),(33,'2025-10-29 17:28:35.784568',NULL,_binary '','2025-10-30 22:28:00.000000',3,'2025-10-30 20:00:00.000000',1),(34,'2025-10-29 17:28:35.811445',NULL,_binary '','2025-10-30 21:58:00.000000',19,'2025-10-30 20:00:00.000000',3);
/*!40000 ALTER TABLE `showtimes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-03 14:45:38
