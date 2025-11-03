-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: communication_db
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
-- Table structure for table `chat_messages`
--

DROP TABLE IF EXISTS `chat_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_messages` (
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `session_id` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `content` text NOT NULL,
  `sender` enum('BOT','USER') DEFAULT NULL,
  `type` enum('AI_GENERATED','FAQ_STATIC','INTERNAL_API') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3cpkdtwdxndrjhrx3gt9q5ux9` (`session_id`),
  CONSTRAINT `FK3cpkdtwdxndrjhrx3gt9q5ux9` FOREIGN KEY (`session_id`) REFERENCES `chat_sessions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_messages`
--

LOCK TABLES `chat_messages` WRITE;
/*!40000 ALTER TABLE `chat_messages` DISABLE KEYS */;
INSERT INTO `chat_messages` VALUES ('2025-10-18 07:48:13.099324',1,1,NULL,'alo','USER','AI_GENERATED'),('2025-10-18 07:48:15.050426',2,1,NULL,'Chào bạn! Có điều gì tôi có thể giúp bạn hôm nay không?','BOT','AI_GENERATED'),('2025-10-18 07:48:17.123876',3,1,NULL,'bạn là ai','USER','AI_GENERATED'),('2025-10-18 07:48:18.370649',4,1,NULL,'Tôi là trợ lý ảo của rạp chiếu phim CNM Cinemas. Tôi ở đây để giúp bạn với bất kỳ thông tin nào về phim, lịch chiếu, đặt vé hoặc các dịch vụ khác tại rạp. Bạn cần hỗ trợ gì hôm nay?','BOT','AI_GENERATED'),('2025-10-18 08:13:24.187026',5,2,NULL,'alo','USER','AI_GENERATED'),('2025-10-18 08:13:25.351699',6,2,NULL,'Chào bạn! Bạn cần tôi hỗ trợ gì hôm nay?','BOT','AI_GENERATED'),('2025-10-18 08:13:27.478039',7,2,NULL,'bạn là ai','USER','AI_GENERATED'),('2025-10-18 08:13:28.738724',8,2,NULL,'Tôi là trợ lý ảo của rạp chiếu phim CNM Cinemas, sẵn sàng hỗ trợ bạn về thông tin phim, đặt vé, và các dịch vụ khác. Bạn cần giúp gì hôm nay?','BOT','AI_GENERATED'),('2025-10-18 08:14:08.671365',9,1,NULL,'bạn biết hôm nay có phim gì ko','USER','AI_GENERATED'),('2025-10-18 08:14:11.706464',10,1,NULL,'Hôm nay, rạp CNM Cinemas có các phim đang chiếu như sau:\n\n1. Chú Thuật Hồi Chiến: Hoài Ngọc / Ngọc Chiết\n2. Inception\n3. Spider-Man: Into the Spider-Verse\n4. Avengers: Endgame\n5. The Silent Horizon\n\nBạn có muốn biết thêm thông tin về phim nào không?','BOT','AI_GENERATED'),('2025-10-18 08:14:35.053697',11,3,NULL,'alo','USER','AI_GENERATED'),('2025-10-18 08:14:35.865449',12,3,NULL,'Chào bạn! Có điều gì tôi có thể giúp bạn hôm nay không?','BOT','AI_GENERATED'),('2025-10-18 08:14:39.296810',13,3,NULL,'bạn biết gì','USER','AI_GENERATED'),('2025-10-18 08:14:40.642440',14,3,NULL,'Tôi có thể cung cấp cho bạn thông tin về các bộ phim đang chiếu, hướng dẫn đặt vé, menu đồ ăn tại rạp và nhiều điều khác liên quan đến trải nghiệm tại CNM Cinemas. Bạn cần biết thông tin gì cụ thể hơn?','BOT','AI_GENERATED'),('2025-10-18 08:15:00.293095',15,1,NULL,'phim thứ nhất hay ko','USER','AI_GENERATED'),('2025-10-18 08:15:23.307087',16,1,NULL,'\"Chú Thuật Hồi Chiến: Hoài Ngọc\" đang nhận được đánh giá khá cao, với điểm trung bình là 4.5/5 từ 2 người đã đánh giá. Phim được đạo diễn bởi Goshozono Shouta và có sự tham gia của các diễn viên như Sakurai Takahiro và Nakamura Yuichi. \n\nBạn có muốn biết thêm thông tin gì khác về phim này không?','BOT','AI_GENERATED'),('2025-10-18 08:17:40.894157',17,4,NULL,'alo','USER','AI_GENERATED'),('2025-10-18 08:17:41.653632',18,4,NULL,'Xin chào! Tôi có thể giúp gì cho bạn hôm nay?','BOT','AI_GENERATED'),('2025-10-18 08:30:11.739438',19,5,NULL,'alo','USER','AI_GENERATED'),('2025-10-18 08:30:13.226113',20,5,NULL,'Chào bạn! Bạn cần hỗ trợ gì hôm nay?','BOT','AI_GENERATED'),('2025-10-18 08:30:14.066023',21,5,NULL,'bạn là ai','USER','AI_GENERATED'),('2025-10-18 08:30:15.132110',22,5,NULL,'Mình là trợ lý ảo của rạp chiếu phim CNM Cinemas. Mình ở đây để hỗ trợ bạn với các thông tin về phim, đặt vé, và các dịch vụ khác của rạp. Bạn cần giúp gì hôm nay?','BOT','AI_GENERATED'),('2025-10-18 08:30:58.707530',23,6,NULL,'alo','USER','AI_GENERATED'),('2025-10-18 08:30:59.541537',24,6,NULL,'Chào bạn! Có điều gì tôi có thể giúp bạn hôm nay không?','BOT','AI_GENERATED'),('2025-10-18 08:33:33.680324',25,7,NULL,'alo','USER','AI_GENERATED'),('2025-10-18 08:33:34.679935',26,7,NULL,'Xin chào! Bạn cần hỗ trợ gì hôm nay?','BOT','AI_GENERATED'),('2025-10-27 09:38:22.651761',77,43,NULL,'b có đang onl ko','USER','AI_GENERATED'),('2025-10-27 09:38:26.562723',78,43,NULL,'Chào bạn! Mình luôn sẵn sàng hỗ trợ bạn. Bạn cần tìm thông tin gì từ rạp CNM Cinemas?','BOT','AI_GENERATED');
/*!40000 ALTER TABLE `chat_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_sessions`
--

DROP TABLE IF EXISTS `chat_sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_sessions` (
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `session_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKnbds8mvm10f5rs8rj2nlx98y9` (`session_id`),
  UNIQUE KEY `UKkku7yip2859n0dij6sh6bve6p` (`user_id`,`active`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_sessions`
--

LOCK TABLES `chat_sessions` WRITE;
/*!40000 ALTER TABLE `chat_sessions` DISABLE KEYS */;
INSERT INTO `chat_sessions` VALUES (_binary '','2025-10-18 07:48:13.023131',1,'2025-10-19 10:24:46.999374',4135,'113d90f6-c696-4f05-b68f-e2cc08fc9076'),(_binary '','2025-10-18 08:13:24.172342',2,NULL,NULL,'d88b17c1-d3b7-493f-8cd5-7c6e295017a3'),(_binary '','2025-10-18 08:14:35.051511',3,NULL,NULL,'bac96907-1324-41ec-b3d8-873657d17dec'),(_binary '','2025-10-18 08:17:40.891151',4,NULL,NULL,'feb1257d-e541-4ae8-b7c5-63e3a5f25177'),(_binary '','2025-10-18 08:30:11.736899',5,NULL,NULL,'93c8ecfd-ca72-47ab-9587-02f3fc6a92e0'),(_binary '','2025-10-18 08:30:58.705021',6,NULL,NULL,'6ea13dca-7b01-4059-bfb9-702a94b31239'),(_binary '','2025-10-18 08:33:33.660480',7,NULL,NULL,'a48344bd-b97d-4550-a213-15636dad2157'),(_binary '\0','2025-10-19 10:15:23.349939',40,'2025-10-19 10:24:11.696283',4158,'aff9e484-4d1f-4118-9dda-ee42245bb9d6'),(_binary '','2025-10-19 10:24:11.729119',43,NULL,4158,'bda1680c-f4c3-4812-9dfc-081d0adafdc5');
/*!40000 ALTER TABLE `chat_sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `faqs`
--

DROP TABLE IF EXISTS `faqs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `faqs` (
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `answer` text NOT NULL,
  `intent` varchar(255) DEFAULT NULL,
  `question` varchar(255) NOT NULL,
  `tags` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKiw3tu4k0ojcbvafcwsauf1c2` (`question`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faqs`
--

LOCK TABLES `faqs` WRITE;
/*!40000 ALTER TABLE `faqs` DISABLE KEYS */;
INSERT INTO `faqs` VALUES (_binary '','2025-10-19 08:29:52.000000',1,'2025-10-19 08:29:52.000000','Bạn có thể xem lịch chiếu chi tiết trên website hoặc ứng dụng CNM Cinemas tại mục “Lịch chiếu”. Chúng tôi cập nhật giờ chiếu theo từng rạp và từng ngày.','showtime','Giờ chiếu phim hôm nay là mấy giờ?','showtime,giờ chiếu,lịch chiếu,phim'),(_binary '','2025-10-19 08:29:52.000000',2,'2025-10-19 08:29:52.000000','Rạp CNM Cinemas mở cửa từ 8:00 sáng đến 11:30 tối hằng ngày, kể cả cuối tuần và ngày lễ.','showtime','Rạp CNM mở cửa lúc mấy giờ?','giờ mở cửa,hoạt động,giờ làm việc'),(_binary '','2025-10-19 08:29:52.000000',3,'2025-10-19 08:29:52.000000','Bạn có thể đặt vé trực tiếp tại quầy, hoặc đặt online qua website và ứng dụng CNM Cinemas. Sau khi chọn phim và suất chiếu, bạn chỉ cần thanh toán để xác nhận vé.','booking','Làm sao để đặt vé xem phim?','booking,đặt vé,vé phim,mua vé'),(_binary '','2025-10-19 08:29:52.000000',4,'2025-10-19 08:29:52.000000','Có, bạn hoàn toàn có thể chọn chỗ ngồi mong muốn khi đặt vé online hoặc tại quầy. Sơ đồ rạp sẽ hiển thị để bạn chọn chỗ trống.','booking','Tôi có thể chọn chỗ ngồi khi đặt vé không?','booking,seat,chỗ ngồi'),(_binary '','2025-10-19 08:29:52.000000',5,'2025-10-19 08:29:52.000000','CNM Cinemas hỗ trợ thanh toán bằng thẻ ATM nội địa, thẻ tín dụng, ví điện tử (Momo, ZaloPay, ShopeePay) và chuyển khoản ngân hàng.','payment','Rạp chấp nhận những phương thức thanh toán nào?','payment,thanh toán,bank,ví điện tử'),(_binary '','2025-10-19 08:29:52.000000',6,'2025-10-19 08:29:52.000000','Nếu thanh toán online không thành công, vui lòng kiểm tra lại kết nối Internet, số dư tài khoản, hoặc liên hệ hotline 1900-1234 để được hỗ trợ.','payment','Thanh toán online bị lỗi thì làm sao?','payment,lỗi thanh toán,thất bại'),(_binary '','2025-10-19 08:29:52.000000',7,'2025-10-19 08:29:52.000000','Theo chính sách của CNM Cinemas, vé đã thanh toán sẽ không được hoàn tiền. Tuy nhiên, bạn có thể đổi suất chiếu nếu liên hệ trước 2 giờ so với giờ chiếu.','refund','Tôi có thể hủy vé sau khi đã thanh toán không?','refund,hủy vé,hoàn tiền,đổi vé'),(_binary '','2025-10-19 08:29:52.000000',8,'2025-10-19 08:29:52.000000','Nếu giao dịch bị lỗi và được xác nhận hoàn tiền, tiền sẽ được hoàn về tài khoản của bạn trong vòng 3–5 ngày làm việc.','refund','Bao lâu tôi nhận được tiền hoàn lại?','refund,hoàn tiền,chuyển khoản'),(_binary '','2025-10-19 08:29:52.000000',9,'2025-10-19 08:29:52.000000','CNM Cinemas thường xuyên có các chương trình ưu đãi đặc biệt cho thành viên. Bạn có thể xem chi tiết tại mục “Khuyến mãi” trên website hoặc app.','promotion','CNM Cinemas hiện có khuyến mãi nào không?','promotion,khuyến mãi,ưu đãi,giảm giá'),(_binary '','2025-10-19 08:29:52.000000',10,'2025-10-19 08:29:52.000000','Bạn chỉ cần đăng ký tài khoản trên website hoặc ứng dụng CNM Cinemas. Thành viên sẽ nhận được ưu đãi giảm giá và tích điểm thưởng khi mua vé.','membership','Làm sao để trở thành thành viên CNM?','membership,thành viên,đăng ký'),(_binary '','2025-10-19 08:29:52.000000',11,'2025-10-19 08:29:52.000000','Hiện tại CNM Cinemas có hơn 10 chi nhánh trên toàn quốc, bao gồm Hà Nội, TP.HCM, Đà Nẵng, Cần Thơ và Hải Phòng.','location','Rạp CNM có bao nhiêu chi nhánh?','location,chi nhánh,địa chỉ'),(_binary '','2025-10-19 08:29:52.000000',12,'2025-10-19 08:29:52.000000','Bạn có thể liên hệ CNM Cinemas qua hotline 1900-1234 hoặc email: support@cnmcinemas.vn để được hỗ trợ nhanh nhất.','contact','Liên hệ rạp CNM bằng cách nào?','contact,hỗ trợ,hotline,email');
/*!40000 ALTER TABLE `faqs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `support_chat_sessions`
--

DROP TABLE IF EXISTS `support_chat_sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `support_chat_sessions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `agent_id` bigint DEFAULT NULL,
  `session_id` varchar(255) NOT NULL,
  `status` enum('ASSIGNED','CLOSED','OPEN') DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7cd099uyg3yu2rr7wbpoxhk86` (`session_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `support_chat_sessions`
--

LOCK TABLES `support_chat_sessions` WRITE;
/*!40000 ALTER TABLE `support_chat_sessions` DISABLE KEYS */;
INSERT INTO `support_chat_sessions` VALUES (13,'2025-10-23 10:25:39.177573','2025-10-23 10:25:46.256661',4159,'c73df939-b254-4c0e-aa93-037b1dcc52a3','CLOSED',4135),(14,'2025-10-23 10:26:22.286126','2025-10-23 10:26:36.734510',4159,'810d3f5e-29c4-4811-8cbf-a50bbdb07f56','CLOSED',4135),(15,'2025-10-23 10:33:07.660031','2025-10-23 11:25:21.354859',4159,'ef5e8aa3-09fa-44c1-887f-fc034418a6ba','CLOSED',4135),(16,'2025-10-23 11:25:32.023164','2025-10-23 11:27:18.012345',4159,'8f7f2413-4863-4c41-94a7-e4b02865e9cb','CLOSED',4135),(17,'2025-10-23 11:27:28.940755','2025-10-23 11:27:36.164955',4159,'953363f9-9d71-49d4-bc55-0321c6b4f70c','CLOSED',4135),(18,'2025-10-23 11:28:22.764340','2025-10-23 11:28:30.137010',4159,'0e169f62-2d98-424a-b5d4-fe8c652e3c2c','CLOSED',4135),(19,'2025-10-23 11:34:41.308933','2025-10-23 11:35:29.383356',4159,'2e39b9f9-497c-4028-bec5-2e0b8f20646c','CLOSED',4135),(20,'2025-10-23 11:35:30.909708','2025-10-23 11:36:26.302249',4159,'3c0d50c5-8e42-4e06-9b86-738fecdb833d','ASSIGNED',4135);
/*!40000 ALTER TABLE `support_chat_sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `support_messages`
--

DROP TABLE IF EXISTS `support_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `support_messages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `content` text NOT NULL,
  `read_at` datetime(6) DEFAULT NULL,
  `sender` enum('AGENT','USER') DEFAULT NULL,
  `session_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfqlx6hy9jsnq344bwrfl9h3hp` (`session_id`),
  CONSTRAINT `FKfqlx6hy9jsnq344bwrfl9h3hp` FOREIGN KEY (`session_id`) REFERENCES `support_chat_sessions` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `support_messages`
--

LOCK TABLES `support_messages` WRITE;
/*!40000 ALTER TABLE `support_messages` DISABLE KEYS */;
INSERT INTO `support_messages` VALUES (1,'2025-10-23 10:25:39.186588','2025-10-23 10:33:26.265286','alo','2025-10-23 10:33:26.261287','USER',13),(2,'2025-10-23 10:26:22.292659','2025-10-23 10:26:28.293618','alo','2025-10-23 10:26:28.292615','USER',14),(3,'2025-10-23 10:26:30.186831','2025-10-23 10:26:31.511240','gì','2025-10-23 10:26:31.511240','AGENT',14),(4,'2025-10-23 10:26:32.602867','2025-10-23 10:26:34.968150','nè','2025-10-23 10:26:34.967148','USER',14),(5,'2025-10-23 10:33:07.665981','2025-10-23 10:55:34.076328','alo','2025-10-23 10:55:34.075305','USER',15),(6,'2025-10-23 10:55:36.074358','2025-10-23 10:57:17.035969','gì','2025-10-23 10:57:17.034961','AGENT',15),(7,'2025-10-23 10:57:20.187328','2025-10-23 10:57:21.812055','alo','2025-10-23 10:57:21.810552','AGENT',15),(8,'2025-10-23 10:57:22.625383','2025-10-23 11:03:59.275681','abc','2025-10-23 11:03:59.274162','USER',15),(9,'2025-10-23 11:03:57.134069','2025-10-23 11:03:59.275681','nè','2025-10-23 11:03:59.274162','USER',15),(10,'2025-10-23 11:04:00.317030','2025-10-23 11:04:10.782061','ơi','2025-10-23 11:04:10.779547','AGENT',15),(11,'2025-10-23 11:04:13.805170','2025-10-23 11:04:28.740233','alo','2025-10-23 11:04:28.739708','AGENT',15),(12,'2025-10-23 11:04:17.033951','2025-10-23 11:04:28.741246','nè','2025-10-23 11:04:28.739708','AGENT',15),(13,'2025-10-23 11:04:22.179033','2025-10-23 11:04:28.741246','nè','2025-10-23 11:04:28.739708','AGENT',15),(14,'2025-10-23 11:04:25.157528','2025-10-23 11:04:28.741246','alo','2025-10-23 11:04:28.739708','AGENT',15),(15,'2025-10-23 11:04:27.283838','2025-10-23 11:04:28.741246','alo','2025-10-23 11:04:28.739708','AGENT',15),(16,'2025-10-23 11:04:30.296526','2025-10-23 11:04:33.436655','alo','2025-10-23 11:04:33.435646','AGENT',15),(17,'2025-10-23 11:04:36.436177','2025-10-23 11:05:01.932081','alo','2025-10-23 11:05:01.931082','AGENT',15),(18,'2025-10-23 11:04:51.035710','2025-10-23 11:05:01.932081','alo','2025-10-23 11:05:01.931082','AGENT',15),(19,'2025-10-23 11:04:57.123474','2025-10-23 11:05:01.932081','alo','2025-10-23 11:05:01.931082','AGENT',15),(20,'2025-10-23 11:05:03.680955','2025-10-23 11:05:05.543528','alo','2025-10-23 11:05:05.542528','AGENT',15),(21,'2025-10-23 11:05:09.871702','2025-10-23 11:05:12.543294','alo','2025-10-23 11:05:12.540133','AGENT',15),(22,'2025-10-23 11:25:32.034734','2025-10-23 11:27:16.194788','alo','2025-10-23 11:27:16.192787','USER',16),(23,'2025-10-23 11:27:28.945817','2025-10-23 11:27:43.891392','alo','2025-10-23 11:27:43.890391','USER',17),(24,'2025-10-23 11:28:22.769978','2025-10-23 11:33:22.294697','alo','2025-10-23 11:33:22.293697','USER',18),(25,'2025-10-23 11:34:41.315613','2025-10-23 13:57:08.536514','alo','2025-10-23 13:57:08.526280','USER',19),(26,'2025-10-23 11:34:44.312287','2025-10-23 13:57:08.540576','nè','2025-10-23 13:57:08.526280','USER',19),(27,'2025-10-23 11:34:50.004346','2025-10-23 13:57:08.540576','abc','2025-10-23 13:57:08.526280','USER',19),(28,'2025-10-23 11:35:11.598360','2025-10-23 13:57:08.540576','xyz','2025-10-23 13:57:08.526280','USER',19),(29,'2025-10-23 11:35:30.916821','2025-10-23 13:57:12.803680','alo','2025-10-23 13:57:12.800479','USER',20);
/*!40000 ALTER TABLE `support_messages` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-03 14:45:56
