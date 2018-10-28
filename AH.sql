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

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `categoryCode` varchar(255) DEFAULT NULL,
  `categoryName` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

/*Data for the table `category` */

insert  into `category`(`id`,`creationDate`,`updateDate`,`categoryCode`,`categoryName`,`description`,`status`) values (6,NULL,NULL,'001','Category1','d','on'),(7,NULL,NULL,'cvcv','c','cvcv','cvcv'),(9,NULL,NULL,'dfd','dfd','fdfdf','dfdf'),(12,'2016-11-05 15:20:55','2016-11-05 15:20:55','sds','sssd','sdsdsd','sdsdsd'),(13,'2016-11-05 23:35:12','2016-11-05 23:35:12','f','f','ff','f'),(14,'2017-08-15 00:51:23','2017-08-15 00:51:23','wsedsdsdw','ererwewe','dsd','dsdwew'),(15,'2017-08-15 00:54:08','2017-08-15 00:54:08','dcfdfwer','dferr','sdsdsd','wewe'),(16,'2017-08-15 00:55:12','2017-08-15 00:55:12','rfwsdwe','efrerfrded','wes','dwewe');

/*Table structure for table `person` */

DROP TABLE IF EXISTS `person`;

CREATE TABLE `person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `person` */

/*Table structure for table `product` */

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `productCode` varchar(255) DEFAULT NULL,
  `productName` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `subCategoryId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `product` */

insert  into `product`(`id`,`creationDate`,`updateDate`,`description`,`productCode`,`productName`,`status`,`subCategoryId`) values (1,'2016-11-07 01:12:05','2016-11-07 01:12:16','sdsd','sdsd','sd','wesd',6),(2,'2017-08-15 00:56:36','2017-08-15 00:56:36','swe','wed','dfer','dsd',9),(3,'2018-10-28 14:57:07','2018-10-23 14:57:14','product 3','pd3','product 3','ready',9),(4,'2018-10-24 14:57:55','2018-10-17 14:57:59','product 4','pd4`','(NULL)product 4','ready',9);

/*Table structure for table `subCategory` */

DROP TABLE IF EXISTS `subCategory`;

CREATE TABLE `subCategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `subCategoryCode` varchar(255) DEFAULT NULL,
  `subCategoryName` varchar(255) DEFAULT NULL,
  `categoryId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK515C9F5EA8A71822` (`categoryId`),
  CONSTRAINT `FK515C9F5EA8A71822` FOREIGN KEY (`categoryId`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

/*Data for the table `subCategory` */

insert  into `subCategory`(`id`,`creationDate`,`updateDate`,`description`,`status`,`subCategoryCode`,`subCategoryName`,`categoryId`) values (1,'2016-11-05 23:35:37','2016-11-07 00:10:26','dsdsd','sdsdsdsdsdsdsdsd','dsds','ssds',9),(2,'2016-11-05 23:36:08','2016-11-05 23:36:08','dsdsd','sdsdsdsdsdsdsdsd','dsds','ssds',NULL),(3,'2016-11-05 23:45:00','2016-11-05 23:45:00','dsd','sdsd','dsds','sds',NULL),(4,'2016-11-05 23:46:57','2016-11-06 01:26:33','erer','erer','rerer','ere',7),(5,'2016-11-05 23:48:44','2016-11-05 23:48:44','fdff','dfdf','fdf','dfd',NULL),(6,'2016-11-06 00:24:59','2016-11-06 00:24:59','sdsd','sdsd','sdsd','sdsd',9),(7,'2016-11-06 00:41:06','2016-11-06 00:41:06','sdsd','sdsd','sd','sds',NULL),(8,'2016-11-06 00:54:57','2016-11-06 00:54:57','fdf','df','dfd','fdf',6),(9,'2016-11-06 01:33:37','2016-11-06 02:09:30','dfdf','dfdf','fdf','dd',9),(10,NULL,NULL,NULL,NULL,NULL,NULL,7);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `dateOfBirth` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `middleName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `userImageUrl` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `user` */

insert  into `user`(`id`,`creationDate`,`updateDate`,`dateOfBirth`,`email`,`firstName`,`lastName`,`middleName`,`password`,`status`,`userImageUrl`,`username`) values (1,NULL,NULL,NULL,'admin@template.com',NULL,NULL,NULL,'$2a$10$b4I0sIIgb4Mkwo3JQQ0m5OULspi8T6wpd/GT2drPV/3T0mI2KKLPC',NULL,NULL,'safayat');

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `roleName` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `user_role` */

insert  into `user_role`(`id`,`creationDate`,`updateDate`,`roleName`,`userId`,`version`) values (1,NULL,NULL,1,1,0);

/*Table structure for table `versionInfo` */

DROP TABLE IF EXISTS `versionInfo`;

CREATE TABLE `versionInfo` (
  `tableName` varchar(255) NOT NULL,
  `updateDate` datetime DEFAULT NULL,
  PRIMARY KEY (`tableName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `versionInfo` */

insert  into `versionInfo`(`tableName`,`updateDate`) values ('category','2017-08-15 00:55:12'),('product','2017-08-15 00:56:36'),('subCategory','2016-11-07 00:10:26');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
