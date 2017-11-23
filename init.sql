-- MySQL dump 10.11
--
-- Host: localhost    Database: vj
-- ------------------------------------------------------
-- Server version	5.0.51b-community-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ceinfo`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ceinfo` (
  `rid` int(11) NOT NULL,
  `info` mediumtext collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `contest`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `contest` (
  `id` int(11) NOT NULL,
  `name` varchar(100) collate utf8_unicode_ci NOT NULL,
  `beginTime` datetime NOT NULL,
  `endTime` datetime NOT NULL,
  `rankType` int(11) NOT NULL,
  `ctype` int(11) NOT NULL,
  `password` varchar(30) collate utf8_unicode_ci NOT NULL,
  `registerstarttime` datetime NOT NULL default '2015-01-01 00:00:00',
  `registerendtime` datetime NOT NULL,
  `info` text collate utf8_unicode_ci NOT NULL,
  `computerating` int(11) NOT NULL,
  `createuser` varchar(30) collate utf8_unicode_ci NOT NULL,
  `kind` int(11) NOT NULL default '0',
  `problemCanPutTag` bit(1) NOT NULL,
  `statusReadOut` bit(1) NOT NULL,
  `registerShowComplete` bit(1) NOT NULL,
  `hideRankMinute` int(11) NOT NULL,
  `isHideOthersStatus` bit(1) NOT NULL,
  `isHideOthersStatusInfo` bit(1) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `contestproblems`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `contestproblems` (
  `cid` int(11) NOT NULL,
  `pid` int(11) NOT NULL,
  `tpid` int(11) NOT NULL,
  PRIMARY KEY  (`cid`,`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `contestuser`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `contestuser` (
  `cid` int(11) NOT NULL,
  `username` varchar(30) collate utf8_unicode_ci NOT NULL,
  `statu` int(11) NOT NULL,
  `info` varchar(50) collate utf8_unicode_ci NOT NULL,
  `time` datetime default NULL,
  PRIMARY KEY  (`cid`,`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `contestusersolve_view`
--

DROP TABLE IF EXISTS `contestusersolve_view`;
/*!50001 DROP VIEW IF EXISTS `contestusersolve_view`*/;
/*!50001 CREATE TABLE `contestusersolve_view` (
  `cid` int(11),
  `pid` bigint(11),
  `username` varchar(20),
  `solved` bigint(20)
) */;

--
-- Table structure for table `permission`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `permission` (
  `id` int(11) NOT NULL,
  `name` varchar(20) collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (1,'新增题目'),(2,'查看代码'),(3,'重判'),(4,'添加比赛'),(5,'计算Rating'),(6,'添加讨论'),(7,'新增标签'),(8,'签到管理'),(9,'权限管理'),(10,'奖励ACB'),(11,'审核比赛报名'),(12,'新增本地题目'),(13,'挑战模式管理'),(14,'密码重置'),(15,'用户管理'),(16,'Log查看'),(17,'考试管理'),(18,'集训队获奖管理'),(19,'添加商品');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `problem`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `problem` (
  `pid` int(11) NOT NULL,
  `ptype` int(11) NOT NULL,
  `title` varchar(80) collate utf8_unicode_ci NOT NULL,
  `ojid` int(11) NOT NULL,
  `ojspid` varchar(10) collate utf8_unicode_ci NOT NULL,
  `visiable` int(11) NOT NULL default '0',
  `author` varchar(300) collate utf8_unicode_ci NOT NULL default '',
  `spj` tinyint(1) NOT NULL,
  `totalSubmit` int(11) NOT NULL default '0',
  `totalSubmitUser` int(11) NOT NULL default '0',
  `totalAc` int(11) NOT NULL default '0',
  `totalAcUser` int(11) NOT NULL default '0',
  `owner` varchar(30) collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `problem`
--

LOCK TABLES `problem` WRITE;
/*!40000 ALTER TABLE `problem` DISABLE KEYS */;
INSERT INTO `problem` VALUES (1000,1,'A + B Problem',0,'1000',1,'',0,0,0,0,0,'');
/*!40000 ALTER TABLE `problem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statu`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `statu` (
  `id` int(11) NOT NULL,
  `ruser` varchar(20) collate utf8_unicode_ci NOT NULL,
  `pid` int(11) NOT NULL,
  `cid` int(11) NOT NULL,
  `lang` int(11) NOT NULL,
  `submitTime` datetime NOT NULL,
  `result` int(11) NOT NULL,
  `score` int(11) NOT NULL default '-1',
  `timeUsed` varchar(10) collate utf8_unicode_ci NOT NULL,
  `memoryUsed` varchar(10) collate utf8_unicode_ci NOT NULL,
  `code` text collate utf8_unicode_ci NOT NULL,
  `codelen` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `suoying` (`ruser`),
  KEY `suoying2` (`pid`),
  KEY `cidsuoyint` (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_acborder`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_acborder` (
  `id` int(11) NOT NULL auto_increment,
  `username` varchar(30) collate utf8_unicode_ci NOT NULL,
  `acbchange` int(11) NOT NULL,
  `reason` int(11) NOT NULL,
  `mark` varchar(200) collate utf8_unicode_ci NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_ai_info`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_ai_info` (
  `id` int(11) NOT NULL auto_increment,
  `username` varchar(30) collate utf8_unicode_ci NOT NULL,
  `game_id` int(11) NOT NULL,
  `ai_name` varchar(30) collate utf8_unicode_ci NOT NULL,
  `ai_code` text collate utf8_unicode_ci NOT NULL,
  `introduce` varchar(300) collate utf8_unicode_ci NOT NULL,
  `isHide` int(11) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_challenge_block`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_challenge_block` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(30) collate utf8_unicode_ci NOT NULL,
  `gro` int(11) NOT NULL,
  `text` text collate utf8_unicode_ci NOT NULL,
  `isEditing` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=610 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_challenge_condition`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_challenge_condition` (
  `belongBlockId` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `par` int(11) NOT NULL,
  `num` int(11) NOT NULL,
  `id` int(11) NOT NULL auto_increment,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_challenge_openblock`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_challenge_openblock` (
  `username` varchar(20) collate utf8_unicode_ci NOT NULL,
  `block` int(11) NOT NULL,
  PRIMARY KEY  (`username`,`block`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_challenge_problem`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_challenge_problem` (
  `id` int(11) NOT NULL,
  `pid` int(11) NOT NULL,
  `tpid` int(11) NOT NULL,
  `score` int(11) NOT NULL,
  PRIMARY KEY  (`id`,`pid`),
  UNIQUE KEY `tpid` (`tpid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_clock_in`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_clock_in` (
  `id` int(11) NOT NULL auto_increment,
  `username` varchar(30) collate utf8_unicode_ci NOT NULL,
  `time` datetime NOT NULL,
  `sign` varchar(200) collate utf8_unicode_ci NOT NULL,
  `ip` varchar(20) collate utf8_unicode_ci NOT NULL,
  `todytimes` int(11) NOT NULL COMMENT '当天第几次',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=459 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_discuss`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_discuss` (
  `id` int(11) NOT NULL,
  `cid` int(11) NOT NULL default '-1',
  `title` varchar(50) collate utf8_unicode_ci NOT NULL,
  `panelclass` int(11) NOT NULL,
  `username` varchar(20) collate utf8_unicode_ci NOT NULL,
  `time` datetime NOT NULL,
  `text` longtext collate utf8_unicode_ci NOT NULL,
  `priority` double NOT NULL,
  `top` tinyint(1) NOT NULL default '0',
  `visiable` tinyint(1) NOT NULL default '0',
  `reply` tinyint(1) NOT NULL default '1',
  `shownum` int(11) NOT NULL default '-1' COMMENT '-1 å…¨éƒ¨æ˜¾ç¤º',
  `panelnobody` tinyint(1) NOT NULL default '0',
  `showauthor` tinyint(1) NOT NULL default '1',
  `showtime` tinyint(1) NOT NULL default '1',
  `replyhidden` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_discussreply`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_discussreply` (
  `rid` int(11) NOT NULL,
  `did` int(11) NOT NULL,
  `username` varchar(20) collate utf8_unicode_ci NOT NULL,
  `time` datetime NOT NULL,
  `text` text collate utf8_unicode_ci NOT NULL,
  `visiable` tinyint(1) NOT NULL,
  `panelclass` int(11) NOT NULL,
  `adminreplay` text collate utf8_unicode_ci,
  PRIMARY KEY  (`rid`,`did`),
  KEY `did` (`did`),
  CONSTRAINT `t_discussreply_ibfk_1` FOREIGN KEY (`did`) REFERENCES `t_discuss` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_game_repetition`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_game_repetition` (
  `id` int(11) NOT NULL auto_increment,
  `blackId` int(11) NOT NULL,
  `blackAuthor` varchar(30) collate utf8_unicode_ci NOT NULL,
  `whiteId` int(11) NOT NULL,
  `whiteAuthor` varchar(30) collate utf8_unicode_ci NOT NULL,
  `processes` text collate utf8_unicode_ci NOT NULL,
  `win` varchar(30) collate utf8_unicode_ci NOT NULL,
  `time` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_group`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_group` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(30) collate utf8_unicode_ci NOT NULL,
  `type` int(11) NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_group_member`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_group_member` (
  `group_id` int(11) NOT NULL,
  `username` varchar(30) collate utf8_unicode_ci NOT NULL,
  `status` int(11) NOT NULL,
  `join_time` datetime NOT NULL,
  PRIMARY KEY  (`group_id`,`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_gv`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_gv` (
  `key` varchar(50) collate utf8_unicode_ci NOT NULL,
  `value` text collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_mall`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_mall` (
  `id` int(11) NOT NULL auto_increment,
  `title` varchar(30) collate utf8_unicode_ci NOT NULL,
  `acb` int(11) NOT NULL,
  `stock` int(11) NOT NULL,
  `des` text collate utf8_unicode_ci NOT NULL,
  `isHidden` tinyint(4) NOT NULL,
  `user` varchar(30) collate utf8_unicode_ci NOT NULL,
  `time` time NOT NULL,
  `buyLimit` int(11) NOT NULL,
  `buyVerifyLimit` int(11) NOT NULL default '1',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_message`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_message` (
  `mid` int(11) NOT NULL auto_increment,
  `user` varchar(20) collate utf8_unicode_ci NOT NULL,
  `statu` int(11) NOT NULL default '0' COMMENT '0未读1已读',
  `title` varchar(100) collate utf8_unicode_ci NOT NULL,
  `text` text collate utf8_unicode_ci NOT NULL,
  `time` datetime NOT NULL,
  `deadline` datetime NOT NULL COMMENT '过期时间',
  PRIMARY KEY  (`mid`)
) ENGINE=InnoDB AUTO_INCREMENT=13948 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_order`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_order` (
  `id` int(11) NOT NULL auto_increment,
  `username` varchar(30) collate utf8_unicode_ci NOT NULL,
  `goodsId` int(11) NOT NULL,
  `acb` int(11) NOT NULL,
  `time` datetime NOT NULL,
  `isCancel` tinyint(4) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `goodsid` (`goodsId`)
) ENGINE=InnoDB AUTO_INCREMENT=155 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_problem_sample`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_problem_sample` (
  `pid` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `input` text collate utf8_unicode_ci NOT NULL,
  `output` text collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`pid`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_problem_tag`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_problem_tag` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(50) collate utf8_unicode_ci NOT NULL,
  `ttype` int(11) NOT NULL default '0',
  `priority` double NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_problem_tag_record`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_problem_tag_record` (
  `pid` int(11) NOT NULL,
  `username` varchar(30) collate utf8_unicode_ci NOT NULL,
  `tagid` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  PRIMARY KEY  (`pid`,`username`,`tagid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;


--
-- Table structure for table `t_problemview`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_problemview` (
  `pid` int(11) NOT NULL,
  `timelimit` varchar(30) collate utf8_unicode_ci NOT NULL,
  `MenoryLimit` varchar(30) collate utf8_unicode_ci NOT NULL,
  `Int64` varchar(10) collate utf8_unicode_ci NOT NULL,
  `spj` int(11) NOT NULL,
  `Dis` text collate utf8_unicode_ci NOT NULL,
  `Input` text collate utf8_unicode_ci NOT NULL,
  `Output` text collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_rank_icpc`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_rank_icpc` (
  `cid` int(11) NOT NULL,
  `penalty` int(11) NOT NULL COMMENT 'ç½šæ—¶(åˆ†é’Ÿ)',
  `mtype_1` int(11) NOT NULL,
  `m1` int(11) NOT NULL,
  `mtype_2` int(11) NOT NULL,
  `m2` int(11) NOT NULL,
  `mtype_3` int(11) NOT NULL,
  `m3` int(11) NOT NULL,
  PRIMARY KEY  (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_rank_shortcode`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_rank_shortcode` (
  `cid` int(11) NOT NULL,
  `mtype_1` int(11) NOT NULL,
  `m1` int(11) NOT NULL,
  `mtype_2` int(11) NOT NULL,
  `m2` int(11) NOT NULL,
  `mtype_3` int(11) NOT NULL,
  `m3` int(11) NOT NULL,
  `chengfa` int(11) NOT NULL default '0',
  PRIMARY KEY  (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_rank_training`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_rank_training` (
  `cid` int(11) NOT NULL,
  `mtype_1` int(11) NOT NULL,
  `m1` int(11) NOT NULL,
  `mtype_2` int(11) NOT NULL,
  `m2` int(11) NOT NULL,
  `mtype_3` int(11) NOT NULL,
  `m3` int(11) NOT NULL,
  PRIMARY KEY  (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_rating`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_rating` (
  `username` varchar(30) collate utf8_unicode_ci NOT NULL,
  `time` datetime NOT NULL,
  `cid` int(11) NOT NULL,
  `prating` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  `ratingnum` int(11) NOT NULL,
  `rank` int(11) NOT NULL,
  PRIMARY KEY  (`username`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_register_team`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_register_team` (
  `username` varchar(30) character set utf8 collate utf8_unicode_ci NOT NULL,
  `cid` int(11) NOT NULL,
  `teamusername` varchar(30) character set utf8 collate utf8_unicode_ci default NULL,
  `teampassword` varchar(30) character set utf8 collate utf8_unicode_ci default NULL,
  `teamname` varchar(30) character set utf8 collate utf8_unicode_ci NOT NULL,
  `statu` int(11) NOT NULL,
  `info` varchar(50) character set utf8 collate utf8_unicode_ci NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY  (`username`,`cid`),
  KEY `cid` (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_replyreply`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_replyreply` (
  `did` int(11) NOT NULL,
  `rid` int(11) NOT NULL,
  `rrid` int(11) NOT NULL,
  `replyRid` int(11) NOT NULL,
  `username` varchar(30) collate utf8_unicode_ci NOT NULL,
  `time` datetime NOT NULL,
  `text` text collate utf8_unicode_ci NOT NULL,
  `visible` tinyint(4) NOT NULL,
  PRIMARY KEY  (`did`,`rid`,`rrid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_some_opt_record`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_some_opt_record` (
  `username` varchar(30) NOT NULL,
  `time` timestamp NOT NULL default '0000-00-00 00:00:00' on update CURRENT_TIMESTAMP,
  `type` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `data` varchar(100) NOT NULL,
  PRIMARY KEY  (`username`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_star`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_star` (
  `id` int(11) NOT NULL auto_increment,
  `username` varchar(30) collate utf8_unicode_ci NOT NULL,
  `type` int(11) NOT NULL,
  `star_id` int(11) NOT NULL,
  `text` varchar(50) collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_team_member_info`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_team_member_info` (
  `id` int(11) NOT NULL auto_increment,
  `time` date NOT NULL,
  `username1` varchar(30) collate utf8_unicode_ci NOT NULL,
  `username2` varchar(30) collate utf8_unicode_ci NOT NULL,
  `username3` varchar(30) collate utf8_unicode_ci NOT NULL,
  `name1` varchar(10) collate utf8_unicode_ci NOT NULL,
  `name2` varchar(10) collate utf8_unicode_ci NOT NULL,
  `name3` varchar(10) collate utf8_unicode_ci NOT NULL,
  `contest_level` int(11) NOT NULL,
  `awards_level` int(11) NOT NULL,
  `text` varchar(128) collate utf8_unicode_ci NOT NULL COMMENT '描述',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_title`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_title` (
  `username` varchar(30) collate utf8_unicode_ci NOT NULL,
  `id` int(11) NOT NULL,
  `jd` int(11) NOT NULL default '0',
  `endtime` timestamp NULL default NULL on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`username`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_title_config`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_title_config` (
  `username` varchar(30) collate utf8_unicode_ci NOT NULL,
  `isShow` bit(1) NOT NULL default '\0',
  `config` varchar(1000) collate utf8_unicode_ci NOT NULL,
  `adj` int(11) NOT NULL default '-1',
  `n` int(11) NOT NULL default '-1',
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_userinfo`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_userinfo` (
  `username` varchar(30) character set utf8 collate utf8_unicode_ci NOT NULL,
  `cid` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `name` varchar(30) character set utf8 collate utf8_unicode_ci NOT NULL,
  `gender` int(11) default NULL,
  `school` varchar(30) character set utf8 collate utf8_unicode_ci default NULL,
  `faculty` varchar(30) character set utf8 collate utf8_unicode_ci default NULL,
  `major` varchar(30) character set utf8 collate utf8_unicode_ci default NULL,
  `cla` varchar(30) character set utf8 collate utf8_unicode_ci default NULL,
  `no` varchar(30) character set utf8 collate utf8_unicode_ci default NULL,
  `phone` varchar(30) character set utf8 collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`username`,`cid`,`id`),
  KEY `cid` (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_usersolve`
--

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_usersolve` (
  `username` varchar(20) collate utf8_unicode_ci NOT NULL,
  `pid` int(11) NOT NULL,
  `status` tinyint(4) NOT NULL,
  PRIMARY KEY  (`username`,`pid`),
  KEY `suoying` (`pid`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

/*!50003 SET @SAVE_SQL_MODE=@@SQL_MODE*/;

DELIMITER ;
/*!50003 SET SESSION SQL_MODE="NO_AUTO_VALUE_ON_ZERO" */;;
/*!50003 CREATE */ /*!50017 DEFINER=`root`@`localhost` */ /*!50003 TRIGGER `tri_user_acnum` AFTER INSERT ON `t_usersolve` FOR EACH ROW UPDATE users SET acnum = (select acnum from v_solved where username=new.username) where username=new.username */;;

/*!50003 SET SESSION SQL_MODE="NO_AUTO_VALUE_ON_ZERO" */;;
/*!50003 CREATE */ /*!50017 DEFINER=`root`@`localhost` */ /*!50003 TRIGGER `tri_user_acnum_2` AFTER UPDATE ON `t_usersolve` FOR EACH ROW UPDATE users SET acnum = (select acnum from v_solved where username=new.username) where username=new.username */;;

DELIMITER ;
/*!50003 SET SESSION SQL_MODE=@SAVE_SQL_MODE*/;

--
-- Table structure for table `t_viewcode`
--

DROP TABLE IF EXISTS `t_viewcode`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_viewcode` (
  `username` varchar(30) collate utf8_unicode_ci NOT NULL,
  `pid` int(11) NOT NULL,
  `type` int(11) NOT NULL default '0',
  PRIMARY KEY  (`username`,`pid`,`type`),
  KEY `pid` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `t_vote`
--

DROP TABLE IF EXISTS `t_vote`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `t_vote` (
  `did` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `isHide` int(11) NOT NULL,
  `isDisable` int(11) NOT NULL,
  `des` varchar(300) collate utf8_unicode_ci NOT NULL,
  PRIMARY KEY  (`did`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `userper`
--

DROP TABLE IF EXISTS `userper`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `userper` (
  `username` varchar(30) collate utf8_unicode_ci NOT NULL,
  `perid` int(11) NOT NULL,
  PRIMARY KEY  (`username`,`perid`),
  CONSTRAINT `userper_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `users` (
  `username` varchar(30) collate utf8_unicode_ci NOT NULL,
  `password` varchar(32) collate utf8_unicode_ci NOT NULL,
  `nick` varchar(50) collate utf8_unicode_ci NOT NULL,
  `gender` int(11) NOT NULL,
  `school` varchar(50) collate utf8_unicode_ci NOT NULL,
  `Email` varchar(50) collate utf8_unicode_ci NOT NULL,
  `motto` varchar(100) collate utf8_unicode_ci NOT NULL,
  `registertime` datetime NOT NULL,
  `type` int(11) NOT NULL,
  `Mark` varchar(100) collate utf8_unicode_ci NOT NULL,
  `rating` int(11) NOT NULL default '-100000',
  `ratingnum` int(11) NOT NULL default '0',
  `acb` int(11) NOT NULL default '0',
  `name` varchar(30) collate utf8_unicode_ci NOT NULL,
  `faculty` varchar(30) collate utf8_unicode_ci NOT NULL,
  `major` varchar(30) collate utf8_unicode_ci NOT NULL,
  `cla` varchar(30) collate utf8_unicode_ci NOT NULL,
  `no` varchar(30) collate utf8_unicode_ci NOT NULL,
  `phone` varchar(30) collate utf8_unicode_ci NOT NULL,
  `acnum` int(11) NOT NULL default '0',
  `inTeamStatus` int(11) NOT NULL COMMENT '队员状态 0非队员 1现役队员 2退役队员',
  `inTeamLv` int(11) NOT NULL COMMENT '队员等级',
  `rank` int(11) default NULL,
  `graduationTime` datetime default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `usersolve_view`
--

DROP TABLE IF EXISTS `usersolve_view`;
/*!50001 DROP VIEW IF EXISTS `usersolve_view`*/;
/*!50001 CREATE TABLE `usersolve_view` (
  `username` varchar(20),
  `pid` int(11),
  `solved` bigint(20)
) */;

--
-- Temporary table structure for view `v_contestuser`
--

DROP TABLE IF EXISTS `v_contestuser`;
/*!50001 DROP VIEW IF EXISTS `v_contestuser`*/;
/*!50001 CREATE TABLE `v_contestuser` (
  `cid` int(11),
  `username` varchar(30),
  `nick` varchar(50),
  `rating` int(11),
  `ratingnum` int(11),
  `statu` int(11),
  `time` datetime,
  `name` varchar(30),
  `gender` int(11),
  `faculty` varchar(30),
  `major` varchar(30),
  `cla` varchar(30),
  `no` varchar(30),
  `phone` varchar(30),
  `info` varchar(50)
) */;

--
-- Temporary table structure for view `v_problem`
--

DROP TABLE IF EXISTS `v_problem`;
/*!50001 DROP VIEW IF EXISTS `v_problem`*/;
/*!50001 CREATE TABLE `v_problem` (
  `pid` int(11),
  `ptype` int(11),
  `title` varchar(80),
  `ojid` int(11),
  `ojspid` varchar(10),
  `visiable` int(11),
  `acusernum` bigint(21),
  `submitnum` bigint(21)
) */;

--
-- Temporary table structure for view `v_problem_tag`
--

DROP TABLE IF EXISTS `v_problem_tag`;
/*!50001 DROP VIEW IF EXISTS `v_problem_tag`*/;
/*!50001 CREATE TABLE `v_problem_tag` (
  `pid` int(11),
  `tagid` int(11),
  `rating` decimal(33,0)
) */;

--
-- Temporary table structure for view `v_solved`
--

DROP TABLE IF EXISTS `v_solved`;
/*!50001 DROP VIEW IF EXISTS `v_solved`*/;
/*!50001 CREATE TABLE `v_solved` (
  `username` varchar(20),
  `acnum` decimal(25,0)
) */;

--
-- Temporary table structure for view `v_user`
--

DROP TABLE IF EXISTS `v_user`;
/*!50001 DROP VIEW IF EXISTS `v_user`*/;
/*!50001 CREATE TABLE `v_user` (
  `rank` bigint(21),
  `username` varchar(30),
  `password` varchar(32),
  `nick` varchar(50),
  `gender` int(11),
  `school` varchar(50),
  `Email` varchar(50),
  `motto` varchar(100),
  `registertime` datetime,
  `type` int(11),
  `Mark` varchar(100),
  `rating` int(11),
  `showrating` double(23,0),
  `ratingnum` int(11),
  `acnum` int(11),
  `acb` int(11),
  `name` varchar(30),
  `faculty` varchar(30),
  `major` varchar(30),
  `cla` varchar(30),
  `no` varchar(30),
  `phone` varchar(30),
  `inTeamLv` int(11),
  `inTeamStatus` int(11)
) */;

--
-- Temporary table structure for view `v_user_`
--

DROP TABLE IF EXISTS `v_user_`;
/*!50001 DROP VIEW IF EXISTS `v_user_`*/;
/*!50001 CREATE TABLE `v_user_` (
  `username` varchar(30),
  `password` varchar(32),
  `nick` varchar(50),
  `gender` int(11),
  `school` varchar(50),
  `Email` varchar(50),
  `motto` varchar(100),
  `registertime` datetime,
  `type` int(11),
  `Mark` varchar(100),
  `rating` int(11),
  `showrating` double(23,0),
  `ratingnum` int(11),
  `acnum` int(11),
  `acb` int(11),
  `name` varchar(30),
  `faculty` varchar(30),
  `major` varchar(30),
  `cla` varchar(30),
  `no` varchar(30),
  `phone` varchar(30),
  `inTeamLv` int(11),
  `inTeamStatus` int(11)
) */;

--
-- Final view structure for view `contestusersolve_view`
--

/*!50001 DROP TABLE `contestusersolve_view`*/;
/*!50001 DROP VIEW IF EXISTS `contestusersolve_view`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` sql SECURITY DEFINER */
/*!50001 VIEW `contestusersolve_view` AS select `statu`.`cid` AS `cid`,(select `contestproblems`.`pid` AS `pid` from `contestproblems` where ((`contestproblems`.`cid` = `statu`.`cid`) and (`contestproblems`.`tpid` = `statu`.`pid`))) AS `pid`,`statu`.`ruser` AS `username`,max((`statu`.`result` = 1)) AS `solved` from `statu` where (`statu`.`cid` <> -(1)) group by `statu`.`ruser`,`statu`.`pid`,`statu`.`cid` */;

--
-- Final view structure for view `usersolve_view`
--

/*!50001 DROP TABLE `usersolve_view`*/;
/*!50001 DROP VIEW IF EXISTS `usersolve_view`*/;
/*!50001 CREATE ALGORITHM=TEMPTABLE */
/*!50013 DEFINER=`root`@`localhost` sql SECURITY DEFINER */
/*!50001 VIEW `usersolve_view` AS select `statu`.`ruser` AS `username`,`statu`.`pid` AS `pid`,max((`statu`.`result` = 1)) AS `solved` from `statu` group by `statu`.`ruser`,`statu`.`pid` */;

--
-- Final view structure for view `v_contestuser`
--

/*!50001 DROP TABLE `v_contestuser`*/;
/*!50001 DROP VIEW IF EXISTS `v_contestuser`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` sql SECURITY DEFINER */
/*!50001 VIEW `v_contestuser` AS select `contestuser`.`cid` AS `cid`,`contestuser`.`username` AS `username`,`users`.`nick` AS `nick`,`users`.`rating` AS `rating`,`users`.`ratingnum` AS `ratingnum`,`contestuser`.`statu` AS `statu`,`contestuser`.`time` AS `time`,`users`.`name` AS `name`,`users`.`gender` AS `gender`,`users`.`faculty` AS `faculty`,`users`.`major` AS `major`,`users`.`cla` AS `cla`,`users`.`no` AS `no`,`users`.`phone` AS `phone`,`contestuser`.`info` AS `info` from (`contestuser` left join `users` on((`contestuser`.`username` = `users`.`username`))) */;

--
-- Final view structure for view `v_problem`
--

/*!50001 DROP TABLE `v_problem`*/;
/*!50001 DROP VIEW IF EXISTS `v_problem`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` sql SECURITY DEFINER */
/*!50001 VIEW `v_problem` AS select `problem`.`pid` AS `pid`,`problem`.`ptype` AS `ptype`,`problem`.`title` AS `title`,`problem`.`ojid` AS `ojid`,`problem`.`ojspid` AS `ojspid`,`problem`.`visiable` AS `visiable`,(select count(`usersolve_view`.`username`) AS `COUNT(username)` from `usersolve_view` where ((`usersolve_view`.`pid` = `problem`.`pid`) and (`usersolve_view`.`solved` = 1))) AS `acusernum`,(select count(`statu`.`id`) AS `COUNT(id)` from `statu` where (`statu`.`pid` = `problem`.`pid`)) AS `submitnum` from `problem` */;

--
-- Final view structure for view `v_problem_tag`
--

/*!50001 DROP TABLE `v_problem_tag`*/;
/*!50001 DROP VIEW IF EXISTS `v_problem_tag`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` sql SECURITY DEFINER */
/*!50001 VIEW `v_problem_tag` AS select `t_problem_tag_record`.`pid` AS `pid`,`t_problem_tag_record`.`tagid` AS `tagid`,sum((`t_problem_tag_record`.`rating` - 500)) AS `rating` from `t_problem_tag_record` group by `t_problem_tag_record`.`pid`,`t_problem_tag_record`.`tagid` */;

--
-- Final view structure for view `v_solved`
--

/*!50001 DROP TABLE `v_solved`*/;
/*!50001 DROP VIEW IF EXISTS `v_solved`*/;
/*!50001 CREATE ALGORITHM=TEMPTABLE */
/*!50013 DEFINER=`root`@`localhost` sql SECURITY DEFINER */
/*!50001 VIEW `v_solved` AS select `t_usersolve`.`username` AS `username`,sum(`t_usersolve`.`status`) AS `acnum` from `t_usersolve` group by `t_usersolve`.`username` */;

--
-- Final view structure for view `v_user`
--

/*!50001 DROP TABLE `v_user`*/;
/*!50001 DROP VIEW IF EXISTS `v_user`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` sql SECURITY DEFINER */
/*!50001 VIEW `v_user` AS select (select count(0) AS `count(0)` from `v_user_` where ((`v_user_`.`showrating` > `a`.`showrating`) or ((`v_user_`.`showrating` = `a`.`showrating`) and (`v_user_`.`acnum` > `a`.`acnum`)))) AS `rank`,`a`.`username` AS `username`,`a`.`password` AS `password`,`a`.`nick` AS `nick`,`a`.`gender` AS `gender`,`a`.`school` AS `school`,`a`.`Email` AS `Email`,`a`.`motto` AS `motto`,`a`.`registertime` AS `registertime`,`a`.`type` AS `type`,`a`.`Mark` AS `Mark`,`a`.`rating` AS `rating`,`a`.`showrating` AS `showrating`,`a`.`ratingnum` AS `ratingnum`,`a`.`acnum` AS `acnum`,`a`.`acb` AS `acb`,`a`.`name` AS `name`,`a`.`faculty` AS `faculty`,`a`.`major` AS `major`,`a`.`cla` AS `cla`,`a`.`no` AS `no`,`a`.`phone` AS `phone`,`a`.`inTeamLv` AS `inTeamLv`,`a`.`inTeamStatus` AS `inTeamStatus` from `v_user_` `a` order by (select count(0) AS `count(0)` from `v_user_` where ((`v_user_`.`showrating` > `a`.`showrating`) or ((`v_user_`.`showrating` = `a`.`showrating`) and (`v_user_`.`acnum` > `a`.`acnum`)))) */;

--
-- Final view structure for view `v_user_`
--

/*!50001 DROP TABLE `v_user_`*/;
/*!50001 DROP VIEW IF EXISTS `v_user_`*/;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` sql SECURITY DEFINER */
/*!50001 VIEW `v_user_` AS select `a`.`username` AS `username`,`a`.`password` AS `password`,`a`.`nick` AS `nick`,`a`.`gender` AS `gender`,`a`.`school` AS `school`,`a`.`Email` AS `Email`,`a`.`motto` AS `motto`,`a`.`registertime` AS `registertime`,`a`.`type` AS `type`,`a`.`Mark` AS `Mark`,`a`.`rating` AS `rating`,round(((((`a`.`rating` - 700) * (1 - pow(0.6,`a`.`ratingnum`))) + 700) + 0.5),0) AS `showrating`,`a`.`ratingnum` AS `ratingnum`,`a`.`acnum` AS `acnum`,`a`.`acb` AS `acb`,`a`.`name` AS `name`,`a`.`faculty` AS `faculty`,`a`.`major` AS `major`,`a`.`cla` AS `cla`,`a`.`no` AS `no`,`a`.`phone` AS `phone`,`a`.`inTeamLv` AS `inTeamLv`,`a`.`inTeamStatus` AS `inTeamStatus` from `users` `a` */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

