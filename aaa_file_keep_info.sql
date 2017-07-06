/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.1.51-community : Database - 123
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`123` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `123`;

/*Table structure for table `aaa_file_keep_info` */

DROP TABLE IF EXISTS `aaa_file_keep_info`;

CREATE TABLE `aaa_file_keep_info` (
  `fki_nm_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `fki_tt_cd` date NOT NULL COMMENT '创建日期',
  `fki_tt_md` date NOT NULL COMMENT '修改日期',
  `fki_st_fn` varchar(200) NOT NULL COMMENT '原文件名称',
  `fki_st_fz` int(20) NOT NULL COMMENT '文件大小',
  `fki_st_fp` varchar(200) NOT NULL COMMENT '文件存放路径',
  `fki_st_ft` varchar(50) NOT NULL COMMENT '文件扩展名类型',
  `fki_st_ip` varchar(20) NOT NULL COMMENT '上传者IP地址',
  PRIMARY KEY (`fki_nm_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
