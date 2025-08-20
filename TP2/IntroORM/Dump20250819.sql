-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: localhost    Database: persistence-db
-- ------------------------------------------------------
-- Server version	8.0.43-0ubuntu0.24.04.1

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
-- Table structure for table `REVISION_INFO`
--

DROP TABLE IF EXISTS `REVISION_INFO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REVISION_INFO` (
  `id` int NOT NULL,
  `REVISION_DATE` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REVISION_INFO`
--

LOCK TABLES `REVISION_INFO` WRITE;
/*!40000 ALTER TABLE `REVISION_INFO` DISABLE KEYS */;
INSERT INTO `REVISION_INFO` VALUES (1,'2025-08-19 00:58:23.635000');
/*!40000 ALTER TABLE `REVISION_INFO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `articulo`
--

DROP TABLE IF EXISTS `articulo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `articulo` (
  `borrado` bit(1) NOT NULL,
  `cantidad` int NOT NULL,
  `precio` int NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `denominacion` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `articulo`
--

LOCK TABLES `articulo` WRITE;
/*!40000 ALTER TABLE `articulo` DISABLE KEYS */;
INSERT INTO `articulo` VALUES (_binary '\0',400,2000,1,'Leche entera'),(_binary '\0',300,1500,2,'Brownie');
/*!40000 ALTER TABLE `articulo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `articulo_AUD`
--

DROP TABLE IF EXISTS `articulo_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `articulo_AUD` (
  `REV` int NOT NULL,
  `REVTYPE` tinyint DEFAULT NULL,
  `borrado` bit(1) DEFAULT NULL,
  `cantidad` int DEFAULT NULL,
  `precio` int DEFAULT NULL,
  `id` bigint NOT NULL,
  `denominacion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`REV`,`id`),
  CONSTRAINT `FKblxtmuw6tj9ci8q4re1oso1as` FOREIGN KEY (`REV`) REFERENCES `REVISION_INFO` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `articulo_AUD`
--

LOCK TABLES `articulo_AUD` WRITE;
/*!40000 ALTER TABLE `articulo_AUD` DISABLE KEYS */;
INSERT INTO `articulo_AUD` VALUES (1,0,_binary '\0',400,2000,1,'Leche entera'),(1,0,_binary '\0',300,1500,2,'Brownie');
/*!40000 ALTER TABLE `articulo_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `articulo_categoria`
--

DROP TABLE IF EXISTS `articulo_categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `articulo_categoria` (
  `id_articulo` bigint NOT NULL,
  `id_categoria` bigint NOT NULL,
  KEY `FK8d0o0iapiq1cffdfbrn4wf3jo` (`id_categoria`),
  KEY `FKnqx33luhfwxmo93wi43xy8nb4` (`id_articulo`),
  CONSTRAINT `FK8d0o0iapiq1cffdfbrn4wf3jo` FOREIGN KEY (`id_categoria`) REFERENCES `categoria` (`id`),
  CONSTRAINT `FKnqx33luhfwxmo93wi43xy8nb4` FOREIGN KEY (`id_articulo`) REFERENCES `articulo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `articulo_categoria`
--

LOCK TABLES `articulo_categoria` WRITE;
/*!40000 ALTER TABLE `articulo_categoria` DISABLE KEYS */;
INSERT INTO `articulo_categoria` VALUES (1,1),(1,2),(2,1);
/*!40000 ALTER TABLE `articulo_categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `articulo_categoria_AUD`
--

DROP TABLE IF EXISTS `articulo_categoria_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `articulo_categoria_AUD` (
  `REV` int NOT NULL,
  `REVTYPE` tinyint DEFAULT NULL,
  `id_articulo` bigint NOT NULL,
  `id_categoria` bigint NOT NULL,
  PRIMARY KEY (`REV`,`id_articulo`,`id_categoria`),
  CONSTRAINT `FKbn6mo1sov9gdririi1bwfo8qd` FOREIGN KEY (`REV`) REFERENCES `REVISION_INFO` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `articulo_categoria_AUD`
--

LOCK TABLES `articulo_categoria_AUD` WRITE;
/*!40000 ALTER TABLE `articulo_categoria_AUD` DISABLE KEYS */;
INSERT INTO `articulo_categoria_AUD` VALUES (1,0,1,1),(1,0,1,2),(1,0,2,1);
/*!40000 ALTER TABLE `articulo_categoria_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `borrado` bit(1) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `denominacion` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (_binary '\0',1,'Perecederos'),(_binary '\0',2,'Lacteos');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoria_AUD`
--

DROP TABLE IF EXISTS `categoria_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria_AUD` (
  `REV` int NOT NULL,
  `REVTYPE` tinyint DEFAULT NULL,
  `borrado` bit(1) DEFAULT NULL,
  `id` bigint NOT NULL,
  `denominacion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`REV`,`id`),
  CONSTRAINT `FK72dg61ru1mr4mnremtvvahey8` FOREIGN KEY (`REV`) REFERENCES `REVISION_INFO` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria_AUD`
--

LOCK TABLES `categoria_AUD` WRITE;
/*!40000 ALTER TABLE `categoria_AUD` DISABLE KEYS */;
INSERT INTO `categoria_AUD` VALUES (1,0,_binary '\0',1,'Perecederos'),(1,0,_binary '\0',2,'Lacteos');
/*!40000 ALTER TABLE `categoria_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `borrado` bit(1) NOT NULL,
  `dni` int NOT NULL,
  `fk_domicilio` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apellido` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jlcg5nhnauli1hu4ojldsedaw` (`dni`),
  UNIQUE KEY `UK_3w1bt7t8t2r2urdyb36tjhd98` (`fk_domicilio`),
  CONSTRAINT `FKraj5dyt7g3b7b9dj5o6aha8j8` FOREIGN KEY (`fk_domicilio`) REFERENCES `domicilio` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (_binary '\0',4534765,1,1,'Lopez','Lucas');
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente_AUD`
--

DROP TABLE IF EXISTS `cliente_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente_AUD` (
  `REV` int NOT NULL,
  `REVTYPE` tinyint DEFAULT NULL,
  `borrado` bit(1) DEFAULT NULL,
  `dni` int DEFAULT NULL,
  `fk_domicilio` bigint DEFAULT NULL,
  `id` bigint NOT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`REV`,`id`),
  CONSTRAINT `FKjrpsxmtyjsp10qmheque06qwy` FOREIGN KEY (`REV`) REFERENCES `REVISION_INFO` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente_AUD`
--

LOCK TABLES `cliente_AUD` WRITE;
/*!40000 ALTER TABLE `cliente_AUD` DISABLE KEYS */;
INSERT INTO `cliente_AUD` VALUES (1,0,_binary '\0',4534765,1,1,'Lopez','Lucas');
/*!40000 ALTER TABLE `cliente_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_factura`
--

DROP TABLE IF EXISTS `detalle_factura`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_factura` (
  `borrado` bit(1) NOT NULL,
  `cantidad` int NOT NULL,
  `sub_total` int NOT NULL,
  `fk_articulo` bigint DEFAULT NULL,
  `fk_factura` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `FKbba6viaqy8ch5ue25bne0jtws` (`fk_articulo`),
  KEY `FKpivyynb1ekrqvoej3r9tsy2ms` (`fk_factura`),
  CONSTRAINT `FKbba6viaqy8ch5ue25bne0jtws` FOREIGN KEY (`fk_articulo`) REFERENCES `articulo` (`id`),
  CONSTRAINT `FKpivyynb1ekrqvoej3r9tsy2ms` FOREIGN KEY (`fk_factura`) REFERENCES `factura` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_factura`
--

LOCK TABLES `detalle_factura` WRITE;
/*!40000 ALTER TABLE `detalle_factura` DISABLE KEYS */;
INSERT INTO `detalle_factura` VALUES (_binary '\0',2,4000,1,1,1),(_binary '\0',3,4500,2,1,2);
/*!40000 ALTER TABLE `detalle_factura` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_factura_AUD`
--

DROP TABLE IF EXISTS `detalle_factura_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_factura_AUD` (
  `REV` int NOT NULL,
  `REVTYPE` tinyint DEFAULT NULL,
  `borrado` bit(1) DEFAULT NULL,
  `cantidad` int DEFAULT NULL,
  `sub_total` int DEFAULT NULL,
  `fk_articulo` bigint DEFAULT NULL,
  `fk_factura` bigint DEFAULT NULL,
  `id` bigint NOT NULL,
  PRIMARY KEY (`REV`,`id`),
  CONSTRAINT `FKos0r11vcnob9ug2uudv3dmrb5` FOREIGN KEY (`REV`) REFERENCES `REVISION_INFO` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_factura_AUD`
--

LOCK TABLES `detalle_factura_AUD` WRITE;
/*!40000 ALTER TABLE `detalle_factura_AUD` DISABLE KEYS */;
INSERT INTO `detalle_factura_AUD` VALUES (1,0,_binary '\0',2,4000,1,1,1),(1,0,_binary '\0',3,4500,2,1,2);
/*!40000 ALTER TABLE `detalle_factura_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `domicilio`
--

DROP TABLE IF EXISTS `domicilio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `domicilio` (
  `borrado` bit(1) NOT NULL,
  `numero` int DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre_calle` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `domicilio`
--

LOCK TABLES `domicilio` WRITE;
/*!40000 ALTER TABLE `domicilio` DISABLE KEYS */;
INSERT INTO `domicilio` VALUES (_binary '\0',4,1,'Av. San Martin');
/*!40000 ALTER TABLE `domicilio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `domicilio_AUD`
--

DROP TABLE IF EXISTS `domicilio_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `domicilio_AUD` (
  `REV` int NOT NULL,
  `REVTYPE` tinyint DEFAULT NULL,
  `borrado` bit(1) DEFAULT NULL,
  `numero` int DEFAULT NULL,
  `id` bigint NOT NULL,
  `nombre_calle` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`REV`,`id`),
  CONSTRAINT `FKfc58h1oov1ovonp3b7nea4l5n` FOREIGN KEY (`REV`) REFERENCES `REVISION_INFO` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `domicilio_AUD`
--

LOCK TABLES `domicilio_AUD` WRITE;
/*!40000 ALTER TABLE `domicilio_AUD` DISABLE KEYS */;
INSERT INTO `domicilio_AUD` VALUES (1,0,_binary '\0',4,1,'Av. San Martin');
/*!40000 ALTER TABLE `domicilio_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `factura`
--

DROP TABLE IF EXISTS `factura`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `factura` (
  `borrado` bit(1) NOT NULL,
  `numero` int NOT NULL,
  `total` int NOT NULL,
  `fk_cliente` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf33dllie6u5tuebm3xqtnw8rn` (`fk_cliente`),
  CONSTRAINT `FKf33dllie6u5tuebm3xqtnw8rn` FOREIGN KEY (`fk_cliente`) REFERENCES `cliente` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `factura`
--

LOCK TABLES `factura` WRITE;
/*!40000 ALTER TABLE `factura` DISABLE KEYS */;
INSERT INTO `factura` VALUES (_binary '\0',1,8500,1,1,'17/08/2009');
/*!40000 ALTER TABLE `factura` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `factura_AUD`
--

DROP TABLE IF EXISTS `factura_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `factura_AUD` (
  `REV` int NOT NULL,
  `REVTYPE` tinyint DEFAULT NULL,
  `borrado` bit(1) DEFAULT NULL,
  `numero` int DEFAULT NULL,
  `total` int DEFAULT NULL,
  `fk_cliente` bigint DEFAULT NULL,
  `id` bigint NOT NULL,
  `fecha` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`REV`,`id`),
  CONSTRAINT `FKcujb6wpfqa2rjgsl2vyd092jv` FOREIGN KEY (`REV`) REFERENCES `REVISION_INFO` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `factura_AUD`
--

LOCK TABLES `factura_AUD` WRITE;
/*!40000 ALTER TABLE `factura_AUD` DISABLE KEYS */;
INSERT INTO `factura_AUD` VALUES (1,0,_binary '\0',1,8500,1,1,'17/08/2009');
/*!40000 ALTER TABLE `factura_AUD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seq_revision_id`
--

DROP TABLE IF EXISTS `seq_revision_id`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seq_revision_id` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seq_revision_id`
--

LOCK TABLES `seq_revision_id` WRITE;
/*!40000 ALTER TABLE `seq_revision_id` DISABLE KEYS */;
INSERT INTO `seq_revision_id` VALUES (51);
/*!40000 ALTER TABLE `seq_revision_id` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-19 21:38:07
