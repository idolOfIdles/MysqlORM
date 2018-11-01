/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.5.52-0ubuntu0.14.04.1-log : Database - alhelal
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`alhelal` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `alhelal`;

/*Table structure for table `rel_user_person` */

DROP TABLE IF EXISTS `rel_user_person`;

CREATE TABLE `rel_user_person` (
  `user_id` int(11) NOT NULL,
  `person_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `rel_user_person` */

insert  into `rel_user_person`(`user_id`,`person_id`) values (1,1),(1,2),(1,3),(2,1),(2,2),(3,1),(3,2),(4,1),(4,2);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
