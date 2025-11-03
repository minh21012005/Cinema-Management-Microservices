-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: user_db
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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `date_of_birth` date DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `gender` enum('FEMALE','MALE') DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  `cinema_id` bigint DEFAULT NULL,
  `auth_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('2000-05-15','2025-09-11 09:10:40.394728',1,NULL,'ha noi','test@gmail.com','minh','0974798105','MALE',1,NULL,0),('2000-05-20','2025-09-11 15:26:37.591998',2,'2025-09-18 13:48:00.124809','123 Đường ABC, Quận 1, TP. Hồ Chí Minh','test1@example.com','Nguyen Van A','0912345578','MALE',4,NULL,0),('2005-01-21','2025-09-12 16:38:33.644161',3,'2025-09-17 09:34:17.647760','ha noi','test2@gmail.com','update','0987654320','FEMALE',3,NULL,0),('2000-05-15','2025-09-13 17:54:22.873693',4,NULL,'ha noi','test3@gmail.com','minh','0974798104','MALE',4,NULL,0),('2000-05-20','2025-09-14 10:00:30.016091',5,'2025-09-17 15:03:10.370442','123 Đường ABC, Quận 1, TP. Hồ Chí Minh','nguyenvana@example.com','Nguyen Van A','0912345678','MALE',2,NULL,0),('2000-05-15','2025-09-16 09:04:10.247100',6,NULL,'ha noi','test5@gmail.com','minh','0974798101','MALE',3,NULL,0),('2000-05-15','2025-09-16 09:40:38.190163',7,NULL,'ha noi','test6@gmail.com','minh','0974798100','MALE',3,NULL,0),('2000-05-15','2025-09-16 15:08:31.098728',8,NULL,'ha noi','test7@gmail.com','minh','0974798109','MALE',3,NULL,0),('2000-05-15','2025-09-16 15:12:03.777447',9,NULL,'ha noi','test8@gmail.com','minh','0974798107','MALE',3,NULL,0),('2000-05-15','2025-09-16 15:39:01.851340',10,NULL,'ha noi','test13@gmail.com','minh','0974798177','MALE',3,NULL,0),('2000-05-15','2025-09-16 15:42:25.728784',11,NULL,'ha noi','test14@gmail.com','minh','0974798147','MALE',3,NULL,0),('2000-05-15','2025-09-16 16:17:41.699227',12,NULL,'ha noi','test15@gmail.com','minh','0975798147','MALE',3,NULL,0),('2000-05-15','2025-09-16 16:18:27.844424',13,NULL,'ha noi','test16@gmail.com','minh','0975748147','MALE',3,NULL,0),('2000-05-15','2025-09-16 16:25:16.444819',14,NULL,'ha noi','test17@gmail.com','minh','0975744147','MALE',3,NULL,0),('2000-05-15','2025-09-16 16:28:41.244003',15,NULL,'ha noi','test18@gmail.com','minh','0945744147','MALE',3,NULL,0),('2000-05-15','2025-09-16 16:37:39.330416',16,NULL,'ha noi','test19@gmail.com','minh','0945754147','MALE',3,NULL,0),('2000-05-15','2025-09-16 16:39:21.345773',17,NULL,'ha noi','test20@gmail.com','minh','0945753147','MALE',3,NULL,0),('2000-05-15','2025-09-16 17:00:30.369207',18,NULL,'ha noi','test22@gmail.com','minh','0945753257','MALE',3,NULL,0),('2000-05-15','2025-09-16 17:04:20.309357',19,NULL,'ha noi','test23@gmail.com','minh','0945753957','MALE',3,NULL,0),('2000-05-15','2025-09-16 17:07:49.942822',20,NULL,'ha noi','test24@gmail.com','minh','0945653957','MALE',3,NULL,0),('2000-05-15','2025-09-16 20:17:45.977688',21,NULL,'ha noi','test25@gmail.com','minh','0945663957','MALE',3,NULL,0),('2000-05-15','2025-09-16 20:24:16.494907',22,NULL,'ha noi','test26@gmail.com','minh','0947663957','MALE',3,NULL,0),('2000-05-15','2025-09-16 20:24:41.435918',23,NULL,'ha noi','test27@gmail.com','minh','0947669957','MALE',3,NULL,0),('2000-05-15','2025-09-16 20:50:46.024986',24,NULL,'ha noi','test28@gmail.com','minh','0947662957','MALE',3,NULL,0),('2000-05-15','2025-09-16 20:52:08.646925',25,NULL,'ha noi','test30@gmail.com','minh','0917663457','MALE',3,NULL,0),('2005-01-21','2025-09-16 21:08:48.686473',26,'2025-09-17 09:25:05.470721','ha noi','test32@gmail.com','update','0987654321','FEMALE',3,NULL,0),('2000-05-15','2025-09-18 11:23:15.043703',27,NULL,'ha noi','customer@gmail.com','customer','0845753147','MALE',3,NULL,4135),('2025-09-01','2025-09-18 14:07:01.848438',28,NULL,'HN','admin@gmail.com','Nguyễn Bá Minh','0975684231','MALE',1,NULL,0),('2000-05-15','2025-09-19 10:04:54.613941',29,NULL,'ha noi','manager@gmail.com','minh','0917678457','MALE',4,NULL,0),('2025-09-01','2025-09-27 10:53:09.644876',30,NULL,'hcm','staff@gmail.com','staff','0987456321','MALE',2,6,4138),('2025-09-15','2025-09-29 12:29:05.827582',31,NULL,'a','staff1@gmail.com','staff','0975846257','MALE',2,8,0),('2025-10-01','2025-10-06 10:49:11.225890',32,NULL,'q','staff2@gmail.com','Minh Nguyễn Bá','0971798105','MALE',2,13,0),('2025-10-03','2025-10-06 10:51:46.187737',33,NULL,'abc','staff3@gmail.com','minh','0874210369','FEMALE',2,6,0),('2025-10-02','2025-10-14 09:01:30.502338',45,NULL,'q','baminhss@gmail.com','Minh Nguyễn Bá','0974798129','MALE',3,NULL,4153),('2025-10-01','2025-10-15 16:00:24.409536',46,NULL,'q','minhhmoii@gmail.com','minh moi','0974798113','MALE',3,NULL,4154),('2025-10-01','2025-10-15 16:37:34.206705',47,NULL,'abc','abc@gmail.com','abc','0587496398','FEMALE',2,6,4157),('2025-10-01','2025-10-15 16:42:13.068288',48,NULL,'q','vohanhtrang2019@gmail.com','Trang peos','0974798193','MALE',3,NULL,4158),('2025-10-10','2025-10-21 08:43:15.644481',49,NULL,'b','support@gmail.com','support','0587126329','FEMALE',37,NULL,4159),('2025-10-14','2025-10-21 21:33:22.394463',50,NULL,'b','support1@gmail.com','support1','0876935614','FEMALE',37,NULL,4160);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-03 14:47:27
