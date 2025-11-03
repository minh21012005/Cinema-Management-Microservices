-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: auth_db
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
-- Table structure for table `auth_users`
--

DROP TABLE IF EXISTS `auth_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_users` (
  `enabled` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `refresh_token` mediumtext,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6jqfsuvys3lan090p4mk16a5t` (`email`),
  KEY `FKki5jdswhp3qm76fks5fkflsk4` (`role_id`),
  CONSTRAINT `FKki5jdswhp3qm76fks5fkflsk4` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4161 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_users`
--

LOCK TABLES `auth_users` WRITE;
/*!40000 ALTER TABLE `auth_users` DISABLE KEYS */;
INSERT INTO `auth_users` VALUES (_binary '','2025-09-11 09:10:40.234069',1,1,'2025-10-06 10:59:47.366439','test@gmail.com','$2a$10$prok5SHYmIbYDmvSFITR2OBrdvFGv9.7Sa/dWEQNiMifJiRptY762','$2a$10$vg.gOVYHF6clTcRczStz0O4z1UbrYsI5DaTJbNSedWjBWhE7btgn.',NULL,NULL),(_binary '\0','2025-09-11 15:26:37.213324',2,4,'2025-09-18 14:27:19.828587','test1@example.com','$2a$10$mnVAY8OKshnvxi59iI4rEusf05VWu0ic861.DlvDRNPdipB1L4zJW','$2a$10$Lqq5luLAdsl1Dg7BnszuB.zCkJdXgSB0qWEGaxx1tE/AtUF8KueGC',NULL,NULL),(_binary '','2025-09-12 16:38:33.467485',3,3,'2025-09-17 10:33:51.020651','test2@gmail.com','$2a$10$t1LlQaOENyLd41ys6lDXSO46Ru0mKH.YDmoXL8SUEeSat8Gtgdi0C',NULL,NULL,NULL),(_binary '\0','2025-09-13 17:54:22.705457',4,4,'2025-09-17 16:26:10.781433','test3@gmail.com','$2a$10$LhBy6Mzw4acIoqawqfoHUuQL1Jte7FSX.80qhUxY9wU5dhH.8XBci',NULL,NULL,NULL),(_binary '','2025-09-14 10:00:29.813318',5,2,'2025-09-17 16:26:06.308065','nguyenvana@example.com','$2a$10$j9UnPMaBxUnXGIeafMuUo.1UmrwNFLNFQ4tTfwykcKsgEN8uYnbK6','$2a$10$fAaVA7Lk2DdZkY8etYCpMeAWAJu7u0qte9wyuVTEairOCg4JE6VqO',NULL,NULL),(_binary '\0','2025-09-16 09:04:10.048075',6,3,'2025-09-17 16:26:08.542964','test5@gmail.com','$2a$10$DtIE2x/V4DjwJHK2HDR1EOJHCAuNOJJzgxI6Em9CarKd/bDL5RXi.',NULL,NULL,NULL),(_binary '','2025-09-16 09:40:38.049901',7,3,NULL,'test6@gmail.com','$2a$10$QawFzcADaWm/N7stI.BzFOr0Wh5V8SkrIrmJNkjpYGNSIIvUjLqjq',NULL,NULL,NULL),(_binary '','2025-09-16 15:08:17.345845',8,3,NULL,'test7@gmail.com','$2a$10$3EWHSPtphb0SsAEM6tPaVerwGmjnYgxk6sgxbHytdmqArWDJ3lhDa',NULL,NULL,NULL),(_binary '','2025-09-16 15:11:58.286067',9,3,NULL,'test8@gmail.com','$2a$10$SyMrzckXdxB45HbmqOmcTOn58Jtt3cUnAh8AI88/fKG1rLE4dTFYO',NULL,NULL,NULL),(_binary '','2025-09-16 15:33:54.911017',10,3,NULL,'test10@gmail.com','$2a$10$oHYTlm.fPyzoWIXN3JCBJ.88Q5TG0/t8x6iomf9BrFSnwRVKAeiTW',NULL,NULL,NULL),(_binary '','2025-09-16 15:35:13.284989',11,3,NULL,'test11@gmail.com','$2a$10$ODEQLd1zGK.iyKpVkhAR6OOwT.gssTxhhrWhcKZT9PN0WfWLD5evC',NULL,NULL,NULL),(_binary '','2025-09-16 15:35:21.332913',12,3,NULL,'test12@gmail.com','$2a$10$4llVvXDXXLVQS3L28itteOf0JMfkPVHnsAEdWNFxACPe8nknR5B2C',NULL,NULL,NULL),(_binary '','2025-09-16 15:38:51.180704',13,3,NULL,'test13@gmail.com','$2a$10$a0AvT6zoIOmwBLBDpCB87OgGNxhs2wx3tzXP.bCdwvEpSQSqsvx7e',NULL,NULL,NULL),(_binary '','2025-09-16 15:42:25.479544',14,3,NULL,'test14@gmail.com','$2a$10$g41PbNNpsDyjBXhKADIUwu3DQlaM4YLTljR0b5nK4D5t/qBu7OOBe',NULL,NULL,NULL),(_binary '','2025-09-16 16:17:10.097037',15,3,NULL,'test15@gmail.com','$2a$10$yplUs/0BOnJiloXvUFtUP.o0d/OarQpWxAuWjy6XJzAZg3qF11yNS',NULL,NULL,NULL),(_binary '','2025-09-16 16:17:55.002788',16,3,NULL,'test16@gmail.com','$2a$10$FBuowLuxU4OlAZOrJH1rNukXiHPVDZXvevipC/CgVtr3AUqnPRgAy',NULL,NULL,NULL),(_binary '','2025-09-16 16:23:37.937418',17,3,NULL,'test17@gmail.com','$2a$10$TIRbVwb/UqVbAkg76FiEHuZYX6WwTeaqwCVrgW0XPFJt0D/MfqK3O',NULL,NULL,NULL),(_binary '','2025-09-16 16:26:19.186676',18,3,NULL,'test18@gmail.com','$2a$10$rb/Esagx5XM0QyosEgRpc.12Nfz6EvNqCN66aczFmUWzQ9e4fw9hi',NULL,NULL,NULL),(_binary '','2025-09-16 16:35:48.104752',19,3,NULL,'test19@gmail.com','$2a$10$Sr3M2GGJRZV272X9bF4f4Ojk1KLTfFlHURkn/1Y70Bky6rM2onHx6',NULL,NULL,NULL),(_binary '','2025-09-16 16:38:54.397696',20,3,NULL,'test20@gmail.com','$2a$10$Eh3jSCxwciAAMEF4rCmXve3ttjxHUM4a2oBsRq51Mi1JeBiW0ZIB6',NULL,NULL,NULL),(_binary '','2025-09-16 16:43:16.650328',21,1,NULL,'test21@gmail.com','$2a$10$gbPA8toYvx.RGwfyhzIzNOU7k1hfR95YzY9XFBWssxmOXTaTJk772',NULL,NULL,NULL),(_binary '','2025-09-16 17:00:30.793135',22,2,NULL,'test22@gmail.com','$2a$10$JKQif1ZKo/dJXhxqNFbQae2MzTEliz88bH52KpETKlpMeI8AhvNA6',NULL,NULL,NULL),(_binary '','2025-09-16 17:04:20.967720',23,2,NULL,'test23@gmail.com','$2a$10$jwgrIzshRBnIuO8buVo/X.T4iSfeO./zyWuHHTJ5QaOyfFb8lhDf.',NULL,NULL,NULL),(_binary '','2025-09-16 17:07:50.048352',24,2,NULL,'test24@gmail.com','$2a$10$nc/Xll8ig1/EHdzU4PS6Y.B1yklViAaTnywlXpHH7i9fU4cFBxIN6',NULL,NULL,NULL),(_binary '','2025-09-16 20:17:46.219715',25,2,NULL,'test25@gmail.com','$2a$10$YjxpkWxVTyTRbOOPDU4DNeSImhOyWj37OH4ugHejX7PIhDuB9V.Oy',NULL,NULL,NULL),(_binary '','2025-09-16 20:24:16.916711',26,2,NULL,'test26@gmail.com','$2a$10$N9u/53NNo1doiuBj5akVo.wWM5cGhlr/p8Bo2uxN2YuAHFbtwEsdy',NULL,NULL,NULL),(_binary '','2025-09-16 20:24:41.525399',27,4,NULL,'test27@gmail.com','$2a$10$hUM3fhit5tuHxL/2CBmxz.FfFoqfl1aqV/.Ntci/IJV2Fsw.dBni.',NULL,NULL,NULL),(_binary '','2025-09-16 20:50:46.032732',28,1,NULL,'test28@gmail.com','$2a$10$MoJ1mWQFz.hAsa3HhQrAV.8oT/x5rzZNr3cTJriZnyQ9LZX/DEE5K',NULL,NULL,NULL),(_binary '','2025-09-16 20:51:29.176592',29,3,NULL,'test29@gmail.com','$2a$10$jKZaA5gehxVo5WVZRwpcWueQcg2hDWMC8yY9.3qjZS7ZLfB6PRASm',NULL,NULL,NULL),(_binary '','2025-09-16 20:52:39.831107',31,3,NULL,'test31@gmail.com','$2a$10$VWs8EKchpB5OGm2slFTecOLI5jOS.c5qNMmWDINwoW.lq6LS/Hr5W',NULL,NULL,NULL),(_binary '','2025-09-16 21:08:48.878144',4133,1,NULL,'test32@gmail.com','$2a$10$MweYhoKkSk/LKiy/zEXsne8HHIviDX4duJfwqodCN8Ui6ZGLE7oj6',NULL,NULL,NULL),(_binary '','2025-09-18 11:23:14.896902',4135,3,'2025-10-31 14:01:56.600093','customer@gmail.com','$2a$10$8W2gzxinGtiEgspS.zOYV./FxhLBk/iw1xfD17vo62N.82OFJXB5S','$2a$10$byb3ia8Lyp0h3Ac8emx4VeubUeT.wWn395YhF0cunneDa8HZwWJAa',NULL,'anonymousUser'),(_binary '','2025-09-18 14:07:01.984168',4136,1,'2025-10-23 11:15:33.440874','admin@gmail.com','$2a$10$A34YnAiiIv2wsvrx.HYuleejOEQoNx6xWeAtkQ.qO2fgsQNIi0X3C',NULL,NULL,NULL),(_binary '','2025-09-19 10:04:54.724682',4137,4,'2025-10-31 10:37:33.208023','manager@gmail.com','$2a$10$8FdErUAP0c6EzG.1bFkovOl5hTPQybRYaIzwbhXxOJ5Sbk/aYWD4S',NULL,NULL,'manager@gmail.com'),(_binary '','2025-09-27 10:53:09.521876',4138,2,'2025-10-29 09:57:00.726942','staff@gmail.com','$2a$10$4.vooTYKbExzTGuRfYGmAepWO7Ol3t7zjXVyu.s/JQ00o.I/dZEAm',NULL,NULL,NULL),(_binary '','2025-09-29 12:29:06.031863',4139,2,'2025-10-10 17:13:41.067763','staff1@gmail.com','$2a$10$4.vooTYKbExzTGuRfYGmAepWO7Ol3t7zjXVyu.s/JQ00o.I/dZEAm','$2a$10$sKiyj5BLvuozYlVBLUYNl.4EpEQwsgw4psKmka/.mSz8vbB0YVUU2',NULL,NULL),(_binary '','2025-10-06 10:49:11.434735',4140,2,'2025-10-06 10:52:19.768499','staff2@gmail.com','$2a$10$Sj1Zu6ReYgnC8Kams3C0Aezt/APDegx9Xg8THe9uA7aAgvVJdJDCi','$2a$10$5b8qM/YrRvJrvBH5GXMDeudvbXZOm0MBz28nBhtqc6wpjV0AAHHcq',NULL,NULL),(_binary '','2025-10-06 10:51:46.272218',4141,2,'2025-10-06 10:52:10.609603','staff3@gmail.com','$2a$10$aZIhHu.qasiJk25lOJ7gRetZ5GoTBbFgMYxqzkr/3.4c5AdAzlFWC',NULL,NULL,NULL),(_binary '','2025-10-14 09:01:30.266584',4153,3,'2025-10-27 16:49:38.743549','baminhss@gmail.com','$2a$10$frltOdIImy3ooMnw.f7Ma.ZeAlHCg7owFuKDnqkyA7ZWa/yPEtFqW','$2a$10$bfqbmlTkXQ5C20EQRBAPR.sjh94fMB2RTTrdFdlFeCrYzFy3lFGhu',NULL,NULL),(_binary '','2025-10-15 16:00:24.171206',4154,3,'2025-10-27 16:12:21.462968','minhhmoii@gmail.com','$2a$10$L6z3z/6cXdBTOmwDNdr2wOTrRaZj6mjvdeMTgVhNUcid5Lk5t7OIS',NULL,NULL,NULL),(_binary '','2025-10-15 16:37:34.172000',4157,2,NULL,'abc@gmail.com','$2a$10$JAkv7wXhvFMdH3tAerBau.X68KyR6YSO.jUl8K99DRUVjAGiFwhu6',NULL,NULL,NULL),(_binary '','2025-10-15 16:42:12.726151',4158,3,'2025-10-27 16:13:01.386718','vohanhtrang2019@gmail.com','$2a$10$Q80fG2U8cI2Z435ivLE4JOauoBMK2C21Kn3FqCvUwbT.Lm0dsUyBG',NULL,NULL,NULL),(_binary '','2025-10-21 08:43:15.624635',4159,37,'2025-10-23 14:08:31.529117','support@gmail.com','$2a$10$WtFip8Uv39chUTgQg/1xNu.Igc/UTySlz7WQSRQyr8Ejh1PNJdrFS',NULL,NULL,NULL),(_binary '','2025-10-21 21:33:22.356987',4160,37,'2025-10-22 14:29:35.237135','support1@gmail.com','$2a$10$wLpwCkX8mrsS8YvKFkAOuu5IPZiTHDO24jBLxCVpxbnMt46uiFJle','$2a$10$/0idvb5HkXjz.MoTDCnlVeisbMB88Dmhb1RjwCXG6YsLrvxKepcNm',NULL,NULL);
/*!40000 ALTER TABLE `auth_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_otp_verification`
--

DROP TABLE IF EXISTS `email_otp_verification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email_otp_verification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `email` varchar(150) NOT NULL,
  `expires_at` datetime(6) DEFAULT NULL,
  `otp` varchar(10) NOT NULL,
  `verified` bit(1) NOT NULL,
  `expired_at` datetime(6) NOT NULL,
  `raw_data` text,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_otp_verification`
--

LOCK TABLES `email_otp_verification` WRITE;
/*!40000 ALTER TABLE `email_otp_verification` DISABLE KEYS */;
/*!40000 ALTER TABLE `email_otp_verification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `api_path` varchar(255) NOT NULL,
  `code` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `method` varchar(255) NOT NULL,
  `module` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `active` bit(1) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7lcb6glmvwlro3p2w2cewxtvd` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` VALUES (NULL,1,NULL,'/api/movies','MOVIE_CREATE','Permission to create a movie','POST','MOVIE','Create new movie',_binary '',NULL,NULL),(NULL,3,NULL,'/api/movies/{id}','MOVIE_UPDATE','Permission to update movie','PUT','MOVIE','Update movie',_binary '',NULL,NULL),(NULL,5,'2025-09-30 15:22:00.044621','/api/bookings','BOOKING_MANAGE','','POST','BOOKING','Manage bookings',_binary '',NULL,NULL),(NULL,6,NULL,'/api/bookings','BOOKING_VIEW','Permission to view bookings','GET','BOOKING','View bookings',_binary '',NULL,NULL),(NULL,7,NULL,'/api/v1/roles','ROLE_CREATE','Permission to create role','POST','ROLE','Create new role',_binary '',NULL,NULL),(NULL,8,NULL,'/api/v1/roles','ROLE_VIEW',NULL,'GET','ROLE','View role',_binary '',NULL,NULL),(NULL,9,NULL,'/api/v1/roles','ROLE_VIEW_ALL',NULL,'GET','ROLE','View all role',_binary '',NULL,NULL),(NULL,10,NULL,'/api/v1/roles','ROLE_UPDATE',NULL,'PUT','ROLE','Update role',_binary '',NULL,NULL),(NULL,11,NULL,'/api/v1/users/fetch-all','USER_VIEW_ALL',NULL,'GET','USER','View all user',_binary '',NULL,NULL),(NULL,12,NULL,'/api/v1/users','USER_VIEW',NULL,'GET','USER','View user by id',_binary '',NULL,NULL),(NULL,13,NULL,'/api/v1/users','USER_CREATE',NULL,'POST','USER','Create user',_binary '',NULL,NULL),(NULL,14,NULL,'/api/v1/users/{id}','USER_UPDATE','Permission to update user profile','PUT','USER','Update user profile',_binary '',NULL,NULL),(NULL,15,NULL,'/api/v1/users/change-password','USER_CHANGE_PASSWORD',NULL,'POST','USER','Change pass word',_binary '',NULL,NULL),(NULL,16,NULL,'/api/v1/users/{id}','USER_ADMIN_UPDATE','Permission to update user','PUT','USER','Update user',_binary '',NULL,NULL),(NULL,17,NULL,'/api/v1/cinemas/fetch-all','CINEMA_VIEW_ALL',NULL,'GET','CINEMA','View all cinema',_binary '',NULL,NULL),(NULL,18,'2025-09-30 21:14:05.905904','/api/v1/cinemas/{id}','CINEMA_VIEW',NULL,'GET','CINEMA','View cinema',_binary '',NULL,NULL),(NULL,19,NULL,'/api/v1/cinemas','CINEMA_CREATE',NULL,'POST','CINEMA','Create Cinema',_binary '',NULL,NULL),(NULL,20,NULL,'/api/v1/cinemas/{id}','CINEMA_UPDATE',NULL,'PUT','CINEMA','Update Cinema',_binary '',NULL,NULL),(NULL,21,NULL,'/api/v1/rooms','ROOM_VIEW',NULL,'GET','ROOM','View room',_binary '',NULL,NULL),(NULL,22,NULL,'/api/v1/rooms','ROOM_CREATE',NULL,'POST','ROOM','Create room',_binary '',NULL,NULL),(NULL,23,NULL,'/api/v1/rooms','ROOM_UPDATE',NULL,'PUT','ROOM','Update room',_binary '',NULL,NULL),(NULL,24,NULL,'/api/v1/seats','SEAT_VIEW',NULL,'GET','SEAT','View seat',_binary '',NULL,NULL),(NULL,25,NULL,'/api/v1/seats','SEAT_CREATE',NULL,'POST','SEAT','Create seat',_binary '',NULL,NULL),(NULL,26,NULL,'/api/v1/seats','SEAT_UPDATE',NULL,'PUT','SEAT','Update seat',_binary '',NULL,NULL),(NULL,27,NULL,'/api/v1/movies/fetch/{id}','MOVIE_VIEW',NULL,'GET','MOVIE','Fetch movie',_binary '',NULL,NULL),(NULL,28,NULL,'/api/v1/showtime','SHOWTIME_CREATE',NULL,'POST','SHOWTIME','Create showtime',_binary '',NULL,NULL),(NULL,29,NULL,'/api/v1/showtime','SHOWTIME_VIEW',NULL,'GET','SHOWTIME','View showtime',_binary '',NULL,NULL),(NULL,30,NULL,'/api/v1/showtime','SHOWTIME_UPDATE',NULL,'PUT','SHOWTIME','Update showtime',_binary '',NULL,NULL),(NULL,31,NULL,'/api/v1/categories','CATEGORY_VIEW',NULL,'GET','CATEGORY','View category',_binary '',NULL,NULL),(NULL,32,NULL,'/api/v1/media','FILE_UPLOAD',NULL,'POST','FILE','Upload file',_binary '',NULL,NULL),(NULL,33,NULL,'/api/v1/media','FILE_VIEW',NULL,'GET','FILE','View file',_binary '',NULL,NULL),(NULL,34,NULL,'/api/v1/media','FILE_DELETE',NULL,'DELETE','FILE','Delete file',_binary '',NULL,NULL),(NULL,35,NULL,'/api/v1/foods','FOOD_VIEW',NULL,'GET','FOOD','View food',_binary '',NULL,NULL),(NULL,36,NULL,'/api/v1/foods','FOOD_CREATE',NULL,'POST','FOOD','Create food',_binary '',NULL,NULL),(NULL,37,NULL,'/api/v1/foods','FOOD_UPDATE',NULL,'PUT','FOOD','Update food',_binary '',NULL,NULL),(NULL,38,NULL,'/api/v1/foodtypes','FOODTYPE_VIEW',NULL,'GET','FOODTYPE','View food type',_binary '',NULL,NULL),(NULL,39,NULL,'/api/v1/combos','COMBO_CREATE',NULL,'POST','COMBO','Create combo',_binary '',NULL,NULL),(NULL,40,NULL,'/api/v1/combos','COMBO_VIEW',NULL,'GET','COMBO','View combo',_binary '',NULL,NULL),(NULL,41,NULL,'/api/v1/combos','COMBO_UPDATE',NULL,'PUT','COMBO','Update combo',_binary '',NULL,NULL),(NULL,42,NULL,'/api/v1/permissions','PERMISSION_VIEW',NULL,'GET','PERMISSION','View Permission',_binary '',NULL,NULL),(NULL,45,NULL,'/api/v1/permissions','PERMISSION_CREATE',NULL,'POST','PERMISSION','Create permission',_binary '',NULL,NULL),(NULL,46,NULL,'/api/v1/permissions','PERMISSION_UPDATE',NULL,'PUT','PERMISSION','Update permission',_binary '',NULL,NULL),('2025-10-06 09:52:02.107233',48,NULL,'/api/v1/orders','ORDER_CREATE',NULL,'POST','ORDER','Create order',_binary '',NULL,NULL),('2025-10-07 20:24:15.872990',49,NULL,'/api/v1/banners','BANNER_VIEW',NULL,'GET','BANNER','View banner',_binary '',NULL,NULL),('2025-10-07 20:24:41.159527',50,NULL,'/api/v1/banners','BANNER_CREATE',NULL,'POST','BANNER','Create banner',_binary '',NULL,NULL),('2025-10-07 22:16:53.102161',51,NULL,'/api/v1/banners/{id}','BANNER_UPDATE',NULL,'PUT','BANNER','Update banner',_binary '',NULL,NULL),('2025-10-11 22:36:59.262290',52,NULL,'/api/v1/orders/booking','BOOKING_CREATED',NULL,'POST','BOOKING','Book ticket',_binary '',NULL,NULL),('2025-10-12 10:27:31.828894',53,NULL,'/api/v1/orders','BOOKING_CANCEL',NULL,'DELETE','BOOKING','Cancel booking',_binary '',NULL,NULL),('2025-10-15 11:32:27.818877',54,NULL,'/api/v1/ratings','RATING_CREATE',NULL,'POST','RATING','Rate movie',_binary '',NULL,NULL),('2025-10-15 14:57:13.566372',55,NULL,'/api/v1/ratings/movies/{id}','RATING_VIEW',NULL,'GET','RATING','View ratings',_binary '',NULL,NULL);
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permissions`
--

DROP TABLE IF EXISTS `role_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_permissions` (
  `permission_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  UNIQUE KEY `UKt43p6aampim70fxxnkid1mibj` (`role_id`,`permission_id`),
  KEY `FKegdk29eiy7mdtefy5c7eirr6e` (`permission_id`),
  CONSTRAINT `FKegdk29eiy7mdtefy5c7eirr6e` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`),
  CONSTRAINT `FKn5fotdgk8d1xvo8nav9uv3muc` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permissions`
--

LOCK TABLES `role_permissions` WRITE;
/*!40000 ALTER TABLE `role_permissions` DISABLE KEYS */;
INSERT INTO `role_permissions` VALUES (1,4),(3,4),(7,1),(8,1),(9,1),(10,1),(11,1),(12,1),(13,1),(14,1),(14,3),(14,4),(14,37),(15,1),(15,3),(15,4),(15,37),(16,1),(17,4),(18,1),(18,2),(18,4),(18,37),(19,4),(20,4),(21,2),(21,4),(21,37),(22,4),(23,4),(24,2),(24,3),(24,4),(24,37),(25,4),(26,4),(27,2),(27,4),(27,37),(28,4),(29,2),(29,3),(29,4),(29,37),(30,4),(31,4),(32,4),(33,2),(33,4),(34,4),(35,2),(35,3),(35,4),(35,37),(36,4),(37,4),(38,4),(38,37),(39,4),(40,2),(40,3),(40,4),(40,37),(41,4),(42,1),(45,1),(46,1),(48,2),(49,4),(49,37),(50,4),(51,4),(52,3),(53,2),(53,3),(54,3),(55,3);
/*!40000 ALTER TABLE `role_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `code` varchar(255) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (_binary '',NULL,1,'2025-10-06 10:03:55.383049','Administrator role','Quản trị viên','ADMIN',NULL,NULL),(_binary '',NULL,2,'2025-10-12 17:34:05.043209','Staff role with limited permissions','Nhân viên','STAFF',NULL,NULL),(_binary '',NULL,3,'2025-10-15 14:57:21.109276','Default user role','Khách hàng','CUSTOMER',NULL,NULL),(_binary '','2025-09-15 11:34:11.556765',4,'2025-10-07 22:16:59.331146','Manager with limited permissions','Quản lí','MANAGER',NULL,NULL),(_binary '','2025-10-21 08:41:38.545323',37,'2025-10-21 08:45:29.610028',NULL,'Nhân viên CSKH','SUPPORT',NULL,NULL);
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-03 14:44:28
