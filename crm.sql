/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.5.52-0ubuntu0.14.04.1-log : Database - crm
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`crm` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `crm`;

/*Table structure for table `Bank` */

DROP TABLE IF EXISTS `Bank`;

CREATE TABLE `Bank` (
  `bank_id` varchar(128) NOT NULL,
  `bank_name` varchar(128) NOT NULL,
  PRIMARY KEY (`bank_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `Bank` */

insert  into `Bank`(`bank_id`,`bank_name`) values ('10010010','Postbank'),('10030700','Eurocity'),('10040000','Commerzbank'),('22163114','Raiffeisenbank');

/*Table structure for table `accountInfo` */

DROP TABLE IF EXISTS `accountInfo`;

CREATE TABLE `accountInfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountNumber` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `clientId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK339B847BCF77AD3C` (`clientId`),
  CONSTRAINT `FK339B847BCF77AD3C` FOREIGN KEY (`clientId`) REFERENCES `client` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `accountInfo` */

insert  into `accountInfo`(`id`,`accountNumber`,`address`,`clientId`) values (1,'client239239283','sdsd',20),(2,'client239239283sds','sdsds',20),(3,'client239239283sdssdsqwqe','hhhhhhhhhhhhhhhh',20);

/*Table structure for table `billCollectionReport` */

DROP TABLE IF EXISTS `billCollectionReport`;

CREATE TABLE `billCollectionReport` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bank` varchar(255) DEFAULT NULL,
  `bankBrunch` varchar(255) DEFAULT NULL,
  `cheque` varchar(255) DEFAULT NULL,
  `chequeDate` datetime DEFAULT NULL,
  `clientId` int(11) DEFAULT NULL,
  `customerFollowUpId` bigint(20) DEFAULT NULL,
  `deposite` double DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `mrNo` varchar(255) DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `statement` varchar(255) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  `accountNo` varchar(255) DEFAULT NULL,
  `amount` varchar(255) DEFAULT NULL,
  `arrear` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `service` varchar(255) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `billCollectionReport` */

insert  into `billCollectionReport`(`id`,`bank`,`bankBrunch`,`cheque`,`chequeDate`,`clientId`,`customerFollowUpId`,`deposite`,`description`,`mrNo`,`source`,`statement`,`userId`,`accountNo`,`amount`,`arrear`,`date`,`service`,`userName`) values (1,NULL,NULL,NULL,NULL,1,1,NULL,NULL,'111','source',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `client` */

DROP TABLE IF EXISTS `client`;

CREATE TABLE `client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `accountNumber` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `assignedTo` bigint(20) DEFAULT NULL,
  `contactPerson` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `emailClientId` int(11) DEFAULT NULL,
  `internetClientId` int(11) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `officeTel` varchar(255) DEFAULT NULL,
  `organization` varchar(255) DEFAULT NULL,
  `profession` varchar(255) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `webClientId` int(11) DEFAULT NULL,
  `primaryService` varchar(255) DEFAULT NULL,
  `service` varchar(255) DEFAULT NULL,
  `serviceIdList` varchar(255) DEFAULT NULL,
  `netAmount` varchar(255) DEFAULT NULL,
  `totalAmount` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

/*Data for the table `client` */

insert  into `client`(`id`,`creationDate`,`updateDate`,`accountNumber`,`address`,`area`,`assignedTo`,`contactPerson`,`description`,`email`,`emailClientId`,`internetClientId`,`mobile`,`officeTel`,`organization`,`profession`,`userId`,`userName`,`webClientId`,`primaryService`,`service`,`serviceIdList`,`netAmount`,`totalAmount`) values (1,NULL,NULL,'crm1','Dhanmondi, Dhaka','Dhaka',9,'Tahnan',NULL,NULL,1,NULL,'01821716057','01821716057','GCL','MM',12,NULL,NULL,NULL,NULL,' 2 4',NULL,NULL),(2,NULL,NULL,'acc_gcl_company','Mohakahli','NAbisco',NULL,'Razib','Njhsdsdhsd',NULL,NULL,NULL,'1122321212','091232328821','org','SE',13,NULL,1,NULL,NULL,NULL,NULL,NULL),(3,NULL,NULL,'razib_company','Oslo','Norway',1,'Bony','dfrwesds',NULL,NULL,1,'018112232233','23324545433','razib_company_org','MM',14,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,NULL,NULL,'acc_bony','dhaka','Oslo',NULL,'tahnan','dder',NULL,2,NULL,'018626235623','2912392832','Bikroy','MM',15,NULL,2,NULL,NULL,NULL,NULL,NULL),(5,NULL,NULL,'safKing','Santa fe','sdsdsd',NULL,'Devil','Beggar  Description',NULL,3,NULL,'0182736273','2738283','Hell','sucking life',16,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6,NULL,NULL,'eresdfswwewe',NULL,NULL,NULL,NULL,NULL,NULL,4,NULL,NULL,NULL,NULL,NULL,17,NULL,NULL,NULL,NULL,' 2',NULL,NULL),(7,NULL,NULL,'acc23232434',NULL,NULL,NULL,NULL,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,19,NULL,NULL,NULL,NULL,'',NULL,NULL),(8,NULL,NULL,'acc2fdfdfdf',NULL,NULL,NULL,NULL,NULL,NULL,6,NULL,NULL,NULL,NULL,NULL,20,NULL,NULL,NULL,NULL,'',NULL,NULL),(9,NULL,NULL,'swedwdcswew',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,21,NULL,NULL,NULL,NULL,'',NULL,NULL),(10,NULL,NULL,'swedwdcswew',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,22,NULL,NULL,NULL,NULL,'',NULL,NULL),(11,NULL,NULL,'fderfdedfdf',NULL,NULL,NULL,NULL,NULL,NULL,7,NULL,NULL,NULL,NULL,NULL,24,NULL,NULL,NULL,NULL,'',NULL,NULL),(12,NULL,NULL,'crm_new',NULL,NULL,NULL,NULL,NULL,NULL,8,NULL,NULL,NULL,'fswe',NULL,25,NULL,NULL,NULL,NULL,' 1 2',NULL,NULL),(13,NULL,NULL,'accsdwew',NULL,NULL,NULL,NULL,NULL,NULL,9,NULL,NULL,NULL,NULL,NULL,26,NULL,NULL,NULL,NULL,' 2',NULL,NULL),(14,NULL,NULL,'derdsfwsrwsd',NULL,NULL,22,NULL,NULL,NULL,10,NULL,NULL,NULL,NULL,NULL,27,NULL,NULL,NULL,NULL,' 2',NULL,NULL),(15,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,11,NULL,NULL,NULL,'sdsdsd',NULL,28,NULL,NULL,NULL,NULL,'',NULL,NULL),(16,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,12,NULL,NULL,NULL,'xsd',NULL,29,NULL,NULL,NULL,NULL,'',NULL,NULL),(20,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,16,NULL,NULL,NULL,'w3e',NULL,33,NULL,NULL,NULL,NULL,'',NULL,NULL);

/*Table structure for table `customerFollowUp` */

DROP TABLE IF EXISTS `customerFollowUp`;

CREATE TABLE `customerFollowUp` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `arrear` double DEFAULT NULL,
  `bill` double DEFAULT NULL,
  `clientId` int(11) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `paid` double DEFAULT NULL,
  `payable` double DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `followUpDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

/*Data for the table `customerFollowUp` */

insert  into `customerFollowUp`(`id`,`arrear`,`bill`,`clientId`,`date`,`description`,`paid`,`payable`,`creationDate`,`updateDate`,`followUpDate`) values (2,150,150,1,'2016-06-16 01:58:16',NULL,0,150,NULL,'2016-06-16 01:58:16','2016-06-16 01:58:16'),(3,0,0,2,'2016-06-16 01:58:17',NULL,0,0,NULL,'2016-06-16 01:58:17','2016-06-16 01:58:17'),(4,0,0,3,'2016-06-16 01:58:19',NULL,0,0,NULL,'2016-06-16 01:58:19','2016-06-16 01:58:19'),(5,0,0,4,'2016-06-16 01:58:21',NULL,0,0,NULL,'2016-06-16 01:58:21','2016-06-16 01:58:21'),(6,0,0,5,'2016-06-16 01:58:22',NULL,0,0,NULL,'2016-06-16 01:58:22','2016-06-16 01:58:22'),(7,50,50,6,'2016-06-16 01:58:23',NULL,0,50,NULL,'2016-06-16 01:58:23','2016-06-16 01:58:23'),(8,0,0,7,'2016-06-16 01:58:25',NULL,0,0,NULL,'2016-06-16 01:58:25','2016-06-16 01:58:25'),(9,0,0,8,'2016-06-16 01:58:27',NULL,0,0,NULL,'2016-06-16 01:58:27','2016-06-16 01:58:27'),(10,0,0,9,'2016-06-16 01:58:29',NULL,0,0,NULL,'2016-06-16 01:58:29','2016-06-16 01:58:29'),(11,0,0,10,'2016-06-16 01:58:31',NULL,0,0,NULL,'2016-06-16 01:58:31','2016-06-16 01:58:31'),(12,0,0,11,'2016-06-16 01:58:32',NULL,0,0,NULL,'2016-06-16 01:58:32','2016-06-16 01:58:32'),(13,70,70,12,'2016-06-16 01:58:34',NULL,0,70,NULL,'2016-06-16 01:58:34','2016-06-16 01:58:34'),(14,0,NULL,13,NULL,NULL,0,0,'2016-08-31 00:18:25',NULL,NULL),(15,0,NULL,1,NULL,NULL,0,0,'2016-08-31 22:24:36',NULL,NULL),(16,0,NULL,14,NULL,NULL,0,0,'2016-08-31 22:25:14',NULL,NULL),(17,0,NULL,15,NULL,NULL,0,0,'2016-09-26 23:29:06',NULL,NULL),(18,0,NULL,16,NULL,NULL,0,0,'2016-09-26 23:55:03',NULL,NULL),(19,0,NULL,20,NULL,NULL,0,0,'2016-09-27 00:54:31',NULL,NULL),(20,0,NULL,20,NULL,NULL,0,0,'2016-09-27 21:32:09',NULL,NULL);

/*Table structure for table `email_client` */

DROP TABLE IF EXISTS `email_client`;

CREATE TABLE `email_client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `clientId` int(11) DEFAULT NULL,
  `emailDomainName` varchar(255) DEFAULT NULL,
  `paid` tinyint(1) NOT NULL,
  `registrationDate` datetime DEFAULT NULL,
  `registrationMonth` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;

/*Data for the table `email_client` */

insert  into `email_client`(`id`,`creationDate`,`updateDate`,`clientId`,`emailDomainName`,`paid`,`registrationDate`,`registrationMonth`) values (1,NULL,NULL,1,'crmrrt',1,'2016-04-11 06:00:00','April'),(2,NULL,NULL,4,'emailDomain',1,'2016-04-20 06:00:00','Decmber'),(3,NULL,NULL,5,'hell\'s Kitchen',1,'2016-04-11 06:00:00','January'),(4,NULL,NULL,6,'eressdwe',1,NULL,NULL),(5,NULL,NULL,7,'sdwewesdsd',1,NULL,NULL),(6,NULL,NULL,8,NULL,1,NULL,NULL),(7,NULL,NULL,11,'dferdferer',0,NULL,NULL),(8,NULL,NULL,12,NULL,1,NULL,NULL),(9,NULL,NULL,13,NULL,1,NULL,NULL),(10,NULL,NULL,14,NULL,1,NULL,NULL),(11,NULL,NULL,15,NULL,1,NULL,NULL),(12,NULL,NULL,16,NULL,1,NULL,NULL),(16,NULL,NULL,20,NULL,1,NULL,NULL);

/*Table structure for table `internet_client` */

DROP TABLE IF EXISTS `internet_client`;

CREATE TABLE `internet_client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `accountManager` varchar(255) DEFAULT NULL,
  `bandwidth` varchar(255) DEFAULT NULL,
  `clientId` int(11) DEFAULT NULL,
  `connectionMedia` varchar(255) DEFAULT NULL,
  `gateway` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `mcNo` varchar(255) DEFAULT NULL,
  `mcRackNo` varchar(255) DEFAULT NULL,
  `mcWebLength` varchar(255) DEFAULT NULL,
  `popName` varchar(255) DEFAULT NULL,
  `provider` varchar(255) DEFAULT NULL,
  `subnet` varchar(255) DEFAULT NULL,
  `switchName` varchar(255) DEFAULT NULL,
  `switchPortNo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

/*Data for the table `internet_client` */

insert  into `internet_client`(`id`,`creationDate`,`updateDate`,`accountManager`,`bandwidth`,`clientId`,`connectionMedia`,`gateway`,`ip`,`location`,`mcNo`,`mcRackNo`,`mcWebLength`,`popName`,`provider`,`subnet`,`switchName`,`switchPortNo`) values (1,NULL,NULL,'Akash','asqwqw',3,'qwssd','222.2.22.2','1.1.1.1','aqsasqw','swsd','daw','sdsgfg','asasqw','szz','8.8.8.8','vcxvxcs','1234');

/*Table structure for table `ipBlockHistory` */

DROP TABLE IF EXISTS `ipBlockHistory`;

CREATE TABLE `ipBlockHistory` (
  `ip` varchar(32) NOT NULL,
  `comment` text,
  `blockdate` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `ipBlockHistory` */

/*Table structure for table `primaryService` */

DROP TABLE IF EXISTS `primaryService`;

CREATE TABLE `primaryService` (
  `primaryServiceName` varchar(255) NOT NULL,
  PRIMARY KEY (`primaryServiceName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `primaryService` */

insert  into `primaryService`(`primaryServiceName`) values ('Broadband'),('Miscellaneous'),('Network'),('Web');

/*Table structure for table `requestLog` */

DROP TABLE IF EXISTS `requestLog`;

CREATE TABLE `requestLog` (
  `ip` varchar(32) NOT NULL,
  `date` datetime NOT NULL,
  `status` int(11) NOT NULL,
  `request` text,
  `userAgent` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `requestLog` */

/*Table structure for table `service` */

DROP TABLE IF EXISTS `service`;

CREATE TABLE `service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `cable` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `domain` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fixed` varchar(255) DEFAULT NULL,
  `network` varchar(255) DEFAULT NULL,
  `primaryService` varchar(255) DEFAULT NULL,
  `space` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `service` */

insert  into `service`(`id`,`amount`,`cable`,`description`,`domain`,`email`,`fixed`,`network`,`primaryService`,`space`) values (1,20,'test cable','desc','bangladesh','f@gcl.com','yes','Ifo','Web','no space'),(2,50,'test cable','Broad desc','bangladesh','f@gcl.com','yes','Ifo','Broadband','no space'),(3,90,'test cable','network desc','bangladesh','f@gcl.com','yes','Ifo','Network','no space'),(4,100,'test cable','miscellaneous desc','bangladesh','f@gcl.com','yes','Ifo','Miscellaneous','no space'),(5,45,'sdsd','ererererssd','asasa','s@cefalo.com','sdsdsd','sdewe','Broadband','sdsd');

/*Table structure for table `ticket` */

DROP TABLE IF EXISTS `ticket`;

CREATE TABLE `ticket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `append` tinyint(1) DEFAULT NULL,
  `assignTo` varchar(255) DEFAULT NULL,
  `cannedResponse` varchar(255) DEFAULT NULL,
  `cannedResponseDetails` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `dueDate` datetime DEFAULT NULL,
  `dueTime` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fullName` varchar(255) DEFAULT NULL,
  `helpTopic` varchar(255) DEFAULT NULL,
  `internalNote` varchar(255) DEFAULT NULL,
  `issueDetails` varchar(255) DEFAULT NULL,
  `issueSummary` varchar(255) DEFAULT NULL,
  `priorityLevel` varchar(255) DEFAULT NULL,
  `signature` varchar(255) DEFAULT NULL,
  `slaPlan` varchar(255) DEFAULT NULL,
  `ticketNotice` tinyint(1) DEFAULT NULL,
  `ticketSource` varchar(255) DEFAULT NULL,
  `ticketStatus` varchar(255) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `ticket` */

insert  into `ticket`(`id`,`creationDate`,`updateDate`,`append`,`assignTo`,`cannedResponse`,`cannedResponseDetails`,`department`,`dueDate`,`dueTime`,`email`,`fullName`,`helpTopic`,`internalNote`,`issueDetails`,`issueSummary`,`priorityLevel`,`signature`,`slaPlan`,`ticketNotice`,`ticketSource`,`ticketStatus`,`userId`,`userName`) values (1,'2016-10-15 15:16:50',NULL,NULL,NULL,NULL,NULL,'sales',NULL,NULL,'bony@gcl.com','bony_tasnim ',NULL,NULL,NULL,'sasasasa','medium',NULL,NULL,NULL,NULL,'Open',15,'bony_tasnim'),(2,NULL,NULL,NULL,NULL,NULL,NULL,'asas',NULL,NULL,NULL,'ticket 100',NULL,NULL,NULL,'asasqwwq','medium',NULL,NULL,NULL,NULL,'completed',NULL,NULL),(3,NULL,NULL,NULL,'15',NULL,NULL,'dwe',NULL,NULL,NULL,'sdsdwe',NULL,NULL,NULL,'sdsdwewewe','medium',NULL,NULL,NULL,NULL,'inProgress',NULL,NULL);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `dateOfBirth` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `middleName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `userImageUrl` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `selectedClientFields` varchar(255) DEFAULT NULL,
  `selectedTicketFields` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;

/*Data for the table `user` */

insert  into `user`(`id`,`creationDate`,`updateDate`,`dateOfBirth`,`email`,`firstName`,`lastName`,`middleName`,`password`,`userImageUrl`,`username`,`status`,`selectedClientFields`,`selectedTicketFields`) values (1,'2015-12-19 01:30:20','2015-12-19 01:30:20',NULL,'safayat@cefalo.com','safayat','rahaman',NULL,'$2a$10$b4I0sIIgb4Mkwo3JQQ0m5OULspi8T6wpd/GT2drPV/3T0mI2KKLPC',NULL,'safayat',NULL,'organization contactPerson profession OfficeTel mobile email area internetClient.bandwidth emailClient.emailDomainName emailClient.paid webClient.domainName webClient.cciClient','fullName email ticketSource helpTopic department slaPlan'),(5,NULL,NULL,NULL,'crmClient1@crm.com','crmClient',NULL,NULL,'cefalo',NULL,'crmClient',NULL,NULL,NULL),(8,NULL,NULL,NULL,'crmClient2@crm.com','ecl123456',NULL,NULL,'$2a$10$wOWTAigBzBFEVR9y4grXWu8jT3wCY5sw0k1fT/TwuhFFnl0zBQb6C',NULL,'ecl123456',NULL,NULL,NULL),(9,NULL,NULL,NULL,'crmClient3@crm.com','ecl1dfdfdfdf',NULL,NULL,'$2a$10$/PnJlWFU2pfm5IvCnMCVX.ECsId95gHmeSJhO3lj6rAMLpymKhzcW',NULL,'ecl1dfdfdfdf',NULL,NULL,NULL),(10,NULL,NULL,NULL,'crmClient4@crm.com','33434ererer',NULL,NULL,'$2a$10$zrejFx3lnqFitya9mbj3Y.s.Dj5rKn7K8PTMq9JYiiiH5ZMe7eVwS',NULL,'33434ererer',NULL,NULL,NULL),(11,NULL,NULL,NULL,'crmClient5@crm.com','sdsdsdsdsds',NULL,NULL,'$2a$10$jCGj48YhQut9cNg8.oorW.yXqsxum9Gz/jL03EEcw7.8sCpIN7iFu',NULL,'sdsdsdsdsds',NULL,NULL,NULL),(12,'2016-04-23 00:21:56','2016-04-23 00:21:56',NULL,'crmClient6@crm.com','crm1234561',NULL,NULL,'$2a$10$S7ou22oEJ3OoU2OTZJIoCeHt8nat4NT9Wn1e97eS2U77LUR4VLKhO',NULL,'crm1234561',NULL,NULL,NULL),(13,'2016-04-24 03:04:28','2016-04-24 03:04:28',NULL,'gcl@company.com','gcl_company',NULL,NULL,'$2a$10$v629RNjhRljiLPHQikcWuehV6.uNB5f1KzIgAuJJrHXKU4y/uQ84m',NULL,'gcl_company',NULL,NULL,NULL),(14,'2016-04-25 22:25:26','2016-04-25 22:25:26',NULL,'razib@company.com','razib_company',NULL,NULL,'$2a$10$pgcm8Jd9VSQ7JoTnEB4A/.ebbu/eYs.B2J7M8FP8vgklDrkN95WRK',NULL,'razib_company',NULL,NULL,NULL),(15,'2016-04-25 22:47:01','2016-04-25 22:47:01',NULL,'bony@gcl.com','bony_tasnim',NULL,NULL,'$2a$10$Nx1JXqxrDGX0uqj4OqfF.eX3oFMtSB285dwB3Smvw2oLsdBDvvPPi',NULL,'bony_tasnim',NULL,NULL,NULL),(16,'2016-04-25 23:01:50','2016-04-25 23:01:50',NULL,'safKing@cefalo.com','safKing',NULL,NULL,'$2a$10$q.MjSGaACaIk3.nSMWK0Z.XXgpxqryROSzfC7p0PnbiY9VE8birry',NULL,'safKing',NULL,NULL,NULL),(17,'2016-05-22 16:03:59','2016-05-22 16:03:59',NULL,'safayat.stellar@gmail.com','sfayat34fedre',NULL,NULL,'$2a$10$yueA7fSWtVm5lzgc7DXKVuYt7GndlalQ50Cr.hXu03i6Q3AzaXKxy',NULL,'sfayat34fedre',NULL,NULL,NULL),(19,'2016-05-24 00:41:56','2016-05-24 00:41:56',NULL,'safayat.stell.ar@gmail.com','43erererer',NULL,NULL,'$2a$10$M1SimgS1dEIH3M51ROI7mOl.HiK.251cae9hJdwgTIjIttj4/fIL2',NULL,'43erererer',NULL,NULL,NULL),(20,'2016-05-24 00:53:17','2016-05-24 00:53:17',NULL,'safayat_rahafman@yahoo.com','ererererererere',NULL,NULL,'$2a$10$Fce1R7NcA9qCXfU.43c5D.V83EipuuIXJzTROPHvWniK2Ds/JVPYO',NULL,'ererererererere',NULL,NULL,NULL),(21,'2016-05-24 01:08:16','2016-05-24 01:08:16',NULL,'safaycat.stellar@gmail.com','se43rererer3434',NULL,NULL,'$2a$10$iigjmFatfqbRtmQwe8CE3eLWH4aymmc5T4L.g8tcTwhp6hmg/aCLi',NULL,'se43rererer3434',NULL,NULL,NULL),(22,'2016-05-24 01:08:43','2016-05-24 01:08:43',NULL,'safaycat.stellar@gmail.com','se43rererer3434',NULL,NULL,'$2a$10$.oL7Fh0fW/pYp8Jlf5BayOUL9Fvtj2fDG28V2IRByAm5SHgLcRW3y',NULL,'se43rererer3434',NULL,NULL,NULL),(24,'2016-05-24 01:13:22','2016-05-24 01:13:22',NULL,'safayat_rahfderaman@yahoo.com','dfererefdsfcdeer',NULL,NULL,'$2a$10$42blGDm8Y.XKLd/fYnnn/.GEMq3nTBHQXgHwiRBDHV8xSGRgKo8Wi',NULL,'dfererefdsfcdeer',NULL,NULL,NULL),(25,'2016-06-08 00:15:47','2016-06-08 00:15:47',NULL,'safayat_rahaman@yahoo.com','crm_new',NULL,NULL,'$2a$10$farXG4f0kj6Q.3agG2xZpuL9Txu56oT0N9wsC95Fqo4c8DXoeEDQe',NULL,'crm_new',NULL,NULL,NULL),(26,'2016-08-31 00:18:25','2016-08-31 00:18:25',NULL,'safayat_raha333man@yahoo.com','sdsdwewewe',NULL,NULL,'$2a$10$mSv00maf0ZKSPaFo2h185.dCsdzjUVGj9nghBfTSh7yq1GCWl9buu',NULL,'sdsdwewewe',NULL,NULL,NULL),(27,'2016-08-31 22:25:14','2016-08-31 22:25:14',NULL,'safayat_rahamassssn@yahoo.com','ssssssssssssssssssssssss',NULL,NULL,'$2a$10$F549Jj5Xo6HJe5z2TEKubuASjoym1igkelmSWg8xlNGQo7J2CdEoa',NULL,'ssssssssssssssssssssssss',NULL,NULL,NULL),(28,'2016-09-26 23:29:06','2016-09-26 23:29:06',NULL,'safayat_rahasman@yahoo.com','client_123456',NULL,NULL,'$2a$10$vkxd8bA/r1KPGxYT/4bdVejF1JIcPP0H20yFGLc4mVgeDt2Vh8wmG',NULL,'client_123456',NULL,NULL,NULL),(29,'2016-09-26 23:55:03','2016-09-26 23:55:03',NULL,'safayat_rahaaaman@yahoo.com','client_98765',NULL,NULL,'$2a$10$ydZtu/1/e83rmIBgaJF9iuzWT2/9nHkli7jQX14itXvBMceUN0e52',NULL,'client_98765',NULL,NULL,NULL),(33,'2016-09-27 00:54:31','2016-09-27 00:54:31',NULL,'safadddyat_rahaman@yahoo.com','client239239283dfdf',NULL,NULL,'$2a$10$TAYAVb7XxERLBVFKcfX/D.nBvFLEhmNC1n3zW3DFtLR9.3NOvlNw6',NULL,'client239239283dfdf',NULL,NULL,NULL);

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `creationDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `roleName` varchar(255) NOT NULL,
  `userId` int(11) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;

/*Data for the table `user_role` */

insert  into `user_role`(`creationDate`,`updateDate`,`roleName`,`userId`,`id`) values (NULL,NULL,'1',1,1),(NULL,NULL,'2',5,4),(NULL,NULL,'2',8,7),(NULL,NULL,'2',9,8),(NULL,NULL,'2',10,9),(NULL,NULL,'2',11,10),(NULL,NULL,'2',12,11),(NULL,NULL,'2',13,12),(NULL,NULL,'2',14,13),(NULL,NULL,'2',15,14),(NULL,NULL,'2',16,15),(NULL,NULL,'2',17,16),(NULL,NULL,'2',19,18),(NULL,NULL,'2',20,19),(NULL,NULL,'2',21,20),(NULL,NULL,'2',22,21),(NULL,NULL,'2',24,23),(NULL,NULL,'2',25,24),(NULL,NULL,'2',26,25),(NULL,NULL,'2',27,26),(NULL,NULL,'2',28,27),(NULL,NULL,'2',29,28),(NULL,NULL,'2',33,32);

/*Table structure for table `web_client` */

DROP TABLE IF EXISTS `web_client`;

CREATE TABLE `web_client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creationDate` datetime DEFAULT NULL,
  `updateDate` datetime DEFAULT NULL,
  `cciClient` varchar(255) DEFAULT NULL,
  `clientId` int(11) DEFAULT NULL,
  `domainName` varchar(255) DEFAULT NULL,
  `domainRegister` varchar(255) DEFAULT NULL,
  `expireDate` datetime DEFAULT NULL,
  `expiresOn` datetime DEFAULT NULL,
  `hostIp` varchar(255) DEFAULT NULL,
  `hostName` varchar(255) DEFAULT NULL,
  `hostingPackage` varchar(255) DEFAULT NULL,
  `hostingServer` varchar(255) DEFAULT NULL,
  `ns1` varchar(255) DEFAULT NULL,
  `ns2` varchar(255) DEFAULT NULL,
  `registeredOn` datetime DEFAULT NULL,
  `registrationMonth` varchar(255) DEFAULT NULL,
  `signUpDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `web_client` */

insert  into `web_client`(`id`,`creationDate`,`updateDate`,`cciClient`,`clientId`,`domainName`,`domainRegister`,`expireDate`,`expiresOn`,`hostIp`,`hostName`,`hostingPackage`,`hostingServer`,`ns1`,`ns2`,`registeredOn`,`registrationMonth`,`signUpDate`) values (1,NULL,NULL,'yes',2,'Dhaka_GCL','Dont','2016-04-29 06:00:00','2016-04-30 06:00:00',NULL,NULL,'Premium',NULL,NULL,NULL,'2016-04-21 06:00:00','January','2016-04-01 06:00:00'),(2,NULL,NULL,'fdere',4,'edfder',NULL,'2016-04-11 06:00:00','2016-04-19 06:00:00','12.2.2.2','sdsdwedsf','swedf','swdfdf','dfdfdf','f','2016-04-13 06:00:00','Decmber','2016-04-19 06:00:00');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
