/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50714
Source Host           : localhost:3306
Source Database       : tradedb

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2016-11-01 11:20:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `accountid` int(11) NOT NULL,
  `profile_userid` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `balance` decimal(14,2) DEFAULT NULL,
  `openbalance` decimal(14,2) DEFAULT NULL,
  `creationdate` datetime DEFAULT NULL,
  `lastlogin` datetime DEFAULT NULL,
  `logincount` int(11) NOT NULL,
  `logoutcount` int(11) NOT NULL,
  PRIMARY KEY (`accountid`),
  KEY `ACCOUNT_USERID` (`profile_userid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('1', null, '109200.00', '109200.00', '2016-10-28 17:08:29', '2016-10-28 17:08:29', '1', '1');
INSERT INTO `account` VALUES ('1000', null, '109200.00', '109200.00', '2016-10-28 17:10:46', '2016-10-28 17:10:46', '1', '1');
INSERT INTO `account` VALUES ('1001', null, '109200.00', '109200.00', '2016-10-28 17:10:46', '2016-10-28 17:10:46', '1', '1');
INSERT INTO `account` VALUES ('1002', null, '109200.00', '109200.00', '2016-10-28 17:10:46', '2016-10-28 17:10:46', '1', '1');
INSERT INTO `account` VALUES ('1003', null, '109200.00', '109200.00', '2016-10-28 17:10:46', '2016-10-28 17:10:46', '1', '1');
INSERT INTO `account` VALUES ('1004', null, '109200.00', '109200.00', '2016-10-28 17:10:46', '2016-10-28 17:10:46', '1', '1');
INSERT INTO `account` VALUES ('1005', null, '109200.00', '109200.00', '2016-10-28 17:10:46', '2016-10-28 17:10:46', '1', '1');
INSERT INTO `account` VALUES ('1006', null, '109200.00', '109200.00', '2016-10-28 17:10:46', '2016-10-28 17:10:46', '1', '1');
INSERT INTO `account` VALUES ('1007', null, '109200.00', '109200.00', '2016-10-28 17:10:46', '2016-10-28 17:10:46', '1', '1');
INSERT INTO `account` VALUES ('1008', null, '109200.00', '109200.00', '2016-10-28 17:10:46', '2016-10-28 17:10:46', '1', '1');
INSERT INTO `account` VALUES ('1009', null, '109200.00', '109200.00', '2016-10-28 17:10:46', '2016-10-28 17:10:46', '1', '1');

-- ----------------------------
-- Table structure for accountprofile
-- ----------------------------
DROP TABLE IF EXISTS `accountprofile`;
CREATE TABLE `accountprofile` (
  `userid` varchar(255) CHARACTER SET utf8 NOT NULL,
  `fullname` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `passwd` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `creditcard` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `opt_lock` int(11) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of accountprofile
-- ----------------------------
INSERT INTO `accountprofile` VALUES ('10001', 'edward john', '10001', '9238200990011', 'jose@facebook.com', 'Jose Angoles, Washington DC', '0');
INSERT INTO `accountprofile` VALUES ('10002', 'howard john', '10002', '9238200990012', 'jose@facebook.com', 'Jose Angoles, Washington DC', '0');
INSERT INTO `accountprofile` VALUES ('10003', 'lucy john', '10003', '9238200990013', 'jose@facebook.com', 'Jose Angoles, Washington DC', '0');
INSERT INTO `accountprofile` VALUES ('10004', 'cocuci john', '10004', '9238200990014', 'jose@facebook.com', 'Jose Angoles, Washington DC', '0');
INSERT INTO `accountprofile` VALUES ('10005', 'judy john', '10005', '9238200990015', 'jose@facebook.com', 'Jose Angoles, Washington DC', '0');
INSERT INTO `accountprofile` VALUES ('10006', 'spoke john', '10006', '9238200990016', 'jose@facebook.com', 'Jose Angoles, Washington DC', '0');
INSERT INTO `accountprofile` VALUES ('10007', 'tincy john', '10007', '9238200990017', 'jose@facebook.com', 'Jose Angoles, Washington DC', '0');
INSERT INTO `accountprofile` VALUES ('10008', 'student john', '10008', '9238200990018', 'jose@facebook.com', 'Jose Angoles, Washington DC', '0');
INSERT INTO `accountprofile` VALUES ('10009', 'hulu john', '10009', '9238200990019', 'jose@facebook.com', 'Jose Angoles, Washington DC', '0');
INSERT INTO `accountprofile` VALUES ('10010', 'tools john', '10010', '9238200990020', 'jose@facebook.com', 'Jose Angoles, Washington DC', '0');

-- ----------------------------
-- Table structure for holding
-- ----------------------------
DROP TABLE IF EXISTS `holding`;
CREATE TABLE `holding` (
  `holdingid` int(11) NOT NULL,
  `quantity` decimal(10,0) NOT NULL,
  `purchaseprice` decimal(14,2) DEFAULT NULL,
  `purchasedate` datetime DEFAULT NULL,
  `quote_symbol` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `account_accountid` int(11) DEFAULT NULL,
  PRIMARY KEY (`holdingid`),
  KEY `HOLDING_ACCOUNTID` (`account_accountid`),
  KEY `FK_9lxkvmrnuc4rpvp5t5qj7what` (`quote_symbol`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of holding
-- ----------------------------

-- ----------------------------
-- Table structure for keygen
-- ----------------------------
DROP TABLE IF EXISTS `keygen`;
CREATE TABLE `keygen` (
  `keyname` varchar(255) CHARACTER SET utf8 NOT NULL,
  `keyval` int(11) NOT NULL,
  PRIMARY KEY (`keyname`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of keygen
-- ----------------------------
INSERT INTO `keygen` VALUES ('account', '2');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `orderid` int(11) NOT NULL,
  `ordertype` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `orderstatus` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `opendate` datetime DEFAULT NULL,
  `completiondate` datetime DEFAULT NULL,
  `quantity` decimal(10,0) NOT NULL,
  `price` decimal(14,2) DEFAULT NULL,
  `orderfee` decimal(14,2) DEFAULT NULL,
  `quote_symbol` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `account_accountid` int(11) DEFAULT NULL,
  `holding_holdingid` int(11) DEFAULT NULL,
  PRIMARY KEY (`orderid`),
  KEY `ORDER_ACCOUNTID` (`account_accountid`),
  KEY `ORDER_HOLDINGID` (`holding_holdingid`),
  KEY `CLOSED_ORDERS` (`account_accountid`,`orderstatus`),
  KEY `FK_qpat1nqm8g9mx4rh2bewqg7h6` (`quote_symbol`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for quote
-- ----------------------------
DROP TABLE IF EXISTS `quote`;
CREATE TABLE `quote` (
  `symbol` varchar(255) CHARACTER SET utf8 NOT NULL,
  `company_name` varchar(255) CHARACTER SET utf8 NOT NULL,
  `price` decimal(14,2) DEFAULT NULL,
  `low` decimal(14,2) DEFAULT NULL,
  `high` decimal(14,2) DEFAULT NULL,
  `open1` decimal(14,2) DEFAULT NULL,
  `volume` decimal(10,0) NOT NULL,
  `change1` decimal(10,0) NOT NULL,
  PRIMARY KEY (`symbol`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of quote
-- ----------------------------
INSERT INTO `quote` VALUES ('s:02', '中国移动集团', '3.21', '2996.00', '3300.00', '3200.00', '1200000', '204');
INSERT INTO `quote` VALUES ('s:01', '中国集美集团', '3.21', '2996.00', '3300.00', '3200.00', '1200000', '204');
INSERT INTO `quote` VALUES ('s:03', '中国联通集团', '3.21', '2996.00', '3300.00', '3200.00', '1200000', '204');
INSERT INTO `quote` VALUES ('s:04', '中国大唐集团', '3.21', '2996.00', '3300.00', '3200.00', '1200000', '204');
INSERT INTO `quote` VALUES ('s:05', '中国伊利集团', '3.21', '2996.00', '3300.00', '3200.00', '1200000', '204');
INSERT INTO `quote` VALUES ('s:06', '中国蒙牛集团', '3.21', '2996.00', '3300.00', '3200.00', '1200000', '204');
INSERT INTO `quote` VALUES ('s:07', '中国铁通集团', '3.21', '2996.00', '3300.00', '3200.00', '1200000', '204');
INSERT INTO `quote` VALUES ('s:08', '中国电信集团', '3.21', '2996.00', '3300.00', '3200.00', '1200000', '204');
INSERT INTO `quote` VALUES ('s:09', '中国东方集团', '3.21', '2996.00', '3300.00', '3200.00', '1200000', '204');
INSERT INTO `quote` VALUES ('s:10', '中国海运集团', '3.21', '2996.00', '3300.00', '3200.00', '1200000', '204');
INSERT INTO `quote` VALUES ('s:11', '中国航运集团', '3.21', '2996.00', '3300.00', '3200.00', '1200000', '204');
