-- --------------------------------------------------------
-- 호스트:                          wession.com
-- 서버 버전:                        5.5.38-0ubuntu0.12.04.1 - (Ubuntu)
-- 서버 OS:                        debian-linux-gnu
-- HeidiSQL 버전:                  9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- ws_scim_im 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `ws_scim_im` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ws_scim_im`;


-- 테이블 ws_scim_im.SCIM_AUDIT 구조 내보내기
CREATE TABLE IF NOT EXISTS `SCIM_AUDIT` (
  `workId` varchar(50) DEFAULT NULL,
  `adminId` varchar(50) DEFAULT NULL,
  `userId` varchar(50) DEFAULT NULL,
  `sourceSystem` varchar(50) DEFAULT NULL,
  `directSystem` varchar(50) DEFAULT NULL,
  `method` varchar(50) DEFAULT NULL,
  `result` varchar(50) DEFAULT NULL,
  `detail` varchar(256) DEFAULT NULL,
  `workDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 ws_scim_im.SCIM_AUDIT:~60 rows (대략적) 내보내기
/*!40000 ALTER TABLE `SCIM_AUDIT` DISABLE KEYS */;
INSERT INTO `SCIM_AUDIT` (`workId`, `adminId`, `userId`, `sourceSystem`, `directSystem`, `method`, `result`, `detail`, `workDate`) VALUES
	('7204719', 'sys-scim-admin', '14358698', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-21 22:08:44'),
	('7204719', 'sys-scim-admin', '15014561', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-21 22:08:44'),
	('7204719', 'sys-scim-admin', '29169444', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-21 22:08:44'),
	('7204719', 'sys-scim-admin', '32376169', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-21 22:08:44'),
	('7204719', 'sys-scim-admin', '50090006', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-21 22:08:44'),
	('7204719', 'sys-scim-admin', '50465181', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-21 22:08:44'),
	('7204719', 'sys-scim-admin', '56386178', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-21 22:08:44'),
	('7204719', 'sys-scim-admin', '64247504', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-21 22:08:44'),
	('7204719', 'sys-scim-admin', '88162811', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-21 22:08:44'),
	('7204719', 'sys-scim-admin', '94035985', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-21 22:08:44'),
	('7204720', 'sys-scim-admin', '14358698', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-21 22:12:09'),
	('7204720', 'sys-scim-admin', '15014561', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-21 22:12:09'),
	('7204720', 'sys-scim-admin', '29169444', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-21 22:12:09'),
	('7204720', 'sys-scim-admin', '32376169', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-21 22:12:09'),
	('7204720', 'sys-scim-admin', '50090006', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-21 22:12:09'),
	('7204720', 'sys-scim-admin', '50465181', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-21 22:12:09'),
	('7204720', 'sys-scim-admin', '56386178', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-21 22:12:09'),
	('7204720', 'sys-scim-admin', '64247504', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-21 22:12:09'),
	('7204720', 'sys-scim-admin', '88162811', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-21 22:12:09'),
	(NULL, 'sys-scim-admin', '94035985', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-21 22:12:09'),
	('5884276', 'sys-scim-admin', '14358698', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-22 06:18:03'),
	('5884276', 'sys-scim-admin', '15014561', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-22 06:18:03'),
	('5884276', 'sys-scim-admin', '29169444', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-22 06:18:03'),
	('5884276', 'sys-scim-admin', '32376169', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-22 06:18:03'),
	('5884276', 'sys-scim-admin', '50090006', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-22 06:18:03'),
	('5884276', 'sys-scim-admin', '50465181', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-22 06:18:03'),
	('5884276', 'sys-scim-admin', '56386178', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-22 06:18:03'),
	('5884276', 'sys-scim-admin', '64247504', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-22 06:18:03'),
	('5884276', 'sys-scim-admin', '88162811', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-22 06:18:03'),
	('5884276', 'sys-scim-admin', '94035985', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-22 06:18:03'),
	('9079249', 'sys-scim-admin', '14358698', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-22 06:21:45'),
	('9079249', 'sys-scim-admin', '15014561', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-22 06:21:45'),
	('9079249', 'sys-scim-admin', '29169444', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-22 06:21:45'),
	('9079249', 'sys-scim-admin', '32376169', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-22 06:21:45'),
	('9079249', 'sys-scim-admin', '50090006', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-22 06:21:45'),
	('9079249', 'sys-scim-admin', '50465181', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-22 06:21:45'),
	('9079249', 'sys-scim-admin', '56386178', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-22 06:21:45'),
	('9079249', 'sys-scim-admin', '64247504', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-22 06:21:45'),
	('9079249', 'sys-scim-admin', '88162811', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-22 06:21:45'),
	('9079249', 'sys-scim-admin', '94035985', 'sys-scim-hr', 'sys-scim-im', 'POST', '200', NULL, '2019-02-22 06:21:45'),
	(NULL, 'sys-scim-admin', '14358698', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-22 06:24:14'),
	(NULL, 'sys-scim-admin', '15014561', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-22 06:24:14'),
	(NULL, 'sys-scim-admin', '29169444', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-22 06:24:14'),
	(NULL, 'sys-scim-admin', '32376169', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-22 06:24:14'),
	(NULL, 'sys-scim-admin', '50090006', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-22 06:24:14'),
	(NULL, 'sys-scim-admin', '50465181', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-22 06:24:14'),
	(NULL, 'sys-scim-admin', '56386178', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-22 06:24:14'),
	(NULL, 'sys-scim-admin', '64247504', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-22 06:24:14'),
	(NULL, 'sys-scim-admin', '88162811', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-22 06:24:14'),
	(NULL, 'sys-scim-admin', '94035985', 'sys-scim-hr', 'sys-scim-im', 'PUT', '200', NULL, '2019-02-22 06:24:15'),
	(NULL, 'sys-scim-admin', '14358698', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-22 06:42:05'),
	(NULL, 'sys-scim-admin', '15014561', 'sys-scim-hr', 'sys-scim-im', 'PUT', 'uniqueness', 'Conflict', '2019-02-22 06:42:05'),
	(NULL, 'sys-scim-admin', '29169444', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-22 06:42:05'),
	(NULL, 'sys-scim-admin', '32376169', 'sys-scim-hr', 'sys-scim-im', 'PUT', 'uniqueness', 'Conflict', '2019-02-22 06:42:05'),
	(NULL, 'sys-scim-admin', '50090006', 'sys-scim-hr', 'sys-scim-im', 'PUT', 'uniqueness', 'Conflict', '2019-02-22 06:42:05'),
	(NULL, 'sys-scim-admin', '50465181', 'sys-scim-hr', 'sys-scim-im', 'PUT', 'uniqueness', 'Conflict', '2019-02-22 06:42:05'),
	(NULL, 'sys-scim-admin', '56386178', 'sys-scim-hr', 'sys-scim-im', 'PUT', 'uniqueness', 'Conflict', '2019-02-22 06:42:05'),
	(NULL, 'sys-scim-admin', '64247504', 'sys-scim-hr', 'sys-scim-im', 'PATCH', '200', NULL, '2019-02-22 06:42:05'),
	(NULL, 'sys-scim-admin', '88162811', 'sys-scim-hr', 'sys-scim-im', 'PUT', 'uniqueness', 'Conflict', '2019-02-22 06:42:05'),
	(NULL, 'sys-scim-admin', '94035985', 'sys-scim-hr', 'sys-scim-im', 'PUT', 'uniqueness', 'Conflict', '2019-02-22 06:42:05');
/*!40000 ALTER TABLE `SCIM_AUDIT` ENABLE KEYS */;


-- 테이블 ws_scim_im.SCIM_DATA_DEFINITION 구조 내보내기
CREATE TABLE IF NOT EXISTS `SCIM_DATA_DEFINITION` (
  `dataId` varchar(50) DEFAULT NULL,
  `dataName` varchar(50) DEFAULT NULL,
  `dataType` varchar(50) DEFAULT NULL,
  `dataSize` varchar(50) DEFAULT NULL,
  `nullAble` varchar(50) DEFAULT NULL,
  `dataDefault` varchar(50) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 ws_scim_im.SCIM_DATA_DEFINITION:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `SCIM_DATA_DEFINITION` DISABLE KEYS */;
/*!40000 ALTER TABLE `SCIM_DATA_DEFINITION` ENABLE KEYS */;


-- 테이블 ws_scim_im.SCIM_SCHEDULER 구조 내보내기
CREATE TABLE IF NOT EXISTS `SCIM_SCHEDULER` (
  `schedulerId` varchar(50) DEFAULT NULL,
  `schedulerName` varchar(50) DEFAULT NULL,
  `schedulerType` varchar(50) DEFAULT NULL,
  `schedulerDesc` varchar(50) DEFAULT NULL,
  `jobClass` varchar(50) DEFAULT NULL,
  `triggerType` varchar(50) DEFAULT NULL,
  `dayOfMonth` int(11) DEFAULT NULL,
  `dayOfWeek` int(11) DEFAULT NULL,
  `hourOfDay` int(11) DEFAULT NULL,
  `minuteOfHour` int(11) DEFAULT NULL,
  `excuteSystemId` varchar(50) DEFAULT NULL,
  `sourceSystemId` varchar(50) DEFAULT NULL,
  `targetSystemId` varchar(50) DEFAULT NULL,
  `lastExecuteDate` datetime DEFAULT NULL,
  KEY `FK_SCIM_SCHEDULER_SCIM_SYSTEM` (`sourceSystemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 ws_scim_im.SCIM_SCHEDULER:~7 rows (대략적) 내보내기
/*!40000 ALTER TABLE `SCIM_SCHEDULER` DISABLE KEYS */;
INSERT INTO `SCIM_SCHEDULER` (`schedulerId`, `schedulerName`, `schedulerType`, `schedulerDesc`, `jobClass`, `triggerType`, `dayOfMonth`, `dayOfWeek`, `hourOfDay`, `minuteOfHour`, `excuteSystemId`, `sourceSystemId`, `targetSystemId`, `lastExecuteDate`) VALUES
	('sch-con-gw', 'GW 계정 동기화', 'CONCILIATION', '계정동기화 (GW to IM)', 'com.ehyundai.gw.ConciliationJob_GW', 'DAY', NULL, NULL, 1, 15, 'sys-scim-gw', 'sys-scim-gw', 'sys-scim-im', NULL),
	('sch-pro-sso', 'SSO 계정 배포', 'PROVISION', '계정 배포 (IM to SSO)', 'com.ehyundai.im.ProvisioningJob_SSO', 'DAY', NULL, NULL, 1, 12, 'sys-scim-im', 'sys-scim-im', 'sys-scim-sso', NULL),
	('sch-any-sso', 'SSO 계정 분석', 'ANALYSIS', '계정 분석 (SSO)', 'com.ehyundai.im.AnalysisJob_SSO', 'DAY', NULL, NULL, 1, 16, 'sys-scim-im', 'sys-scim-sso', 'sys-scim-im', NULL),
	('sch-con-sso', 'SSO 계정 동기화 ', 'CONCILIATION', '계정 동기화 (SSO to IM)', 'com.ehyundai.sso.ConciliationJob_SSO', 'DAY', NULL, NULL, 1, 14, 'sys-scim-sso', 'sys-scim-sso', 'sys-scim-im', NULL),
	('sch-con-hr', 'HR 계정 동기화', 'CONCILIATION', '계정 동기화 (HRto IM)', 'com.ehyundai.hr.ConciliationJob_HR', 'DAY', NULL, NULL, 1, 10, 'sys-scim-hr', 'sys-scim-hr', 'sys-scim-im', '2019-02-22 00:00:00'),
	('sch-rec-hr', 'HR 계정 통합', 'RECONCILIATION', '계정 통합 (HRto IM)', 'com.ehyundai.im.ReconciliationJob_HR', 'DAY', NULL, NULL, 1, 11, 'sys-scim-im', 'sys-scim-hr', 'sys-scim-im', '2019-02-22 00:00:00'),
	('sch-pro-gw', 'GW 계정 배포', 'PROVISION', '계정 배포 (IM to GW)', 'com.ehyundai.im.ProvisioningJob_GW', 'DAY', NULL, NULL, 1, 13, 'sys-scim-im', 'sys-scim-im', 'sys-scim-gw', NULL);
/*!40000 ALTER TABLE `SCIM_SCHEDULER` ENABLE KEYS */;


-- 테이블 ws_scim_im.SCIM_SCHEDULER_HISTORY 구조 내보내기
CREATE TABLE IF NOT EXISTS `SCIM_SCHEDULER_HISTORY` (
  `schedulerId` varchar(50) DEFAULT NULL,
  `workId` varchar(50) DEFAULT NULL,
  `reqPost` int(11) DEFAULT NULL,
  `reqPut` int(11) DEFAULT NULL,
  `reqDelete` int(11) DEFAULT NULL,
  `reqPatch` int(11) DEFAULT NULL,
  `resPost` int(11) DEFAULT NULL,
  `resPut` int(11) DEFAULT NULL,
  `resDelete` int(11) DEFAULT NULL,
  `resPatch` int(11) DEFAULT NULL,
  `workDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 ws_scim_im.SCIM_SCHEDULER_HISTORY:~11 rows (대략적) 내보내기
/*!40000 ALTER TABLE `SCIM_SCHEDULER_HISTORY` DISABLE KEYS */;
INSERT INTO `SCIM_SCHEDULER_HISTORY` (`schedulerId`, `workId`, `reqPost`, `reqPut`, `reqDelete`, `reqPatch`, `resPost`, `resPut`, `resDelete`, `resPatch`, `workDate`) VALUES
	('sch-con-hr', '7204719', 6, 1, 0, 3, 6, 1, 0, 3, '2019-02-21 22:08:44'),
	('sch-rec-hr', '7204720', 0, 7, 0, 3, 0, 7, 0, 3, '2019-02-21 22:12:09'),
	('sch-con-hr', '7268391', 0, 0, 0, 0, 0, 0, 0, 0, '2019-02-22 05:20:22'),
	('sch-con-hr', '5884276', 6, 1, 0, 3, 6, 1, 0, 3, '2019-02-22 06:18:03'),
	('sch-con-hr', '9079249', 6, 1, 0, 3, 6, 1, 0, 3, '2019-02-22 06:21:45'),
	('sch-con-hr', '3242226', 0, 0, 0, 0, 0, 0, 0, 0, '2019-02-22 06:21:57'),
	('sch-rec-hr', NULL, 0, 7, 0, 3, 0, 7, 0, 3, '2019-02-22 06:24:15'),
	('sch-rec-hr', NULL, 0, 7, 0, 3, 0, 0, 0, 3, '2019-02-22 06:42:05'),
	('sch-con-hr', '4446168', 0, 0, 0, 0, 0, 0, 0, 0, '2019-02-22 06:42:30'),
	('sch-con-hr', '9777246', 0, 0, 0, 0, 0, 0, 0, 0, '2019-02-22 07:15:33'),
	('sch-con-hr', '251080', 0, 0, 0, 0, 0, 0, 0, 0, '2019-02-22 07:27:57');
/*!40000 ALTER TABLE `SCIM_SCHEDULER_HISTORY` ENABLE KEYS */;


-- 테이블 ws_scim_im.SCIM_SYSTEM 구조 내보내기
CREATE TABLE IF NOT EXISTS `SCIM_SYSTEM` (
  `systemId` varchar(50) NOT NULL,
  `systemName` varchar(50) DEFAULT NULL,
  `systemDesc` varchar(50) DEFAULT NULL,
  `systemUrl` varchar(50) DEFAULT NULL,
  `systemType` varchar(50) NOT NULL,
  PRIMARY KEY (`systemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 ws_scim_im.SCIM_SYSTEM:~4 rows (대략적) 내보내기
/*!40000 ALTER TABLE `SCIM_SYSTEM` DISABLE KEYS */;
INSERT INTO `SCIM_SYSTEM` (`systemId`, `systemName`, `systemDesc`, `systemUrl`, `systemType`) VALUES
	('sys-scim-gw', '그룹 웨어', '라온 시큐어 그룹웨어 시스템', 'http://127.0.0.1:5002', 'CONSUMER'),
	('sys-scim-hr', '인사 시스템', '라온 시큐어 인사 시스템', 'http://127.0.0.1:5001', 'RESOURCE'),
	('sys-scim-im', '통합계정', '라온 시큐어 통합계정 시스템', 'http://127.0.0.1:5000', 'PROVIDER'),
	('sys-scim-sso', '통합로그인', '라온 시큐어 통합로그인 시스템', 'http://127.0.0.1:5003', 'CONSUMER');
/*!40000 ALTER TABLE `SCIM_SYSTEM` ENABLE KEYS */;


-- 테이블 ws_scim_im.SCIM_SYSTEM_ADMIN 구조 내보내기
CREATE TABLE IF NOT EXISTS `SCIM_SYSTEM_ADMIN` (
  `userId` varchar(50) DEFAULT NULL,
  `systemId` varchar(50) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  KEY `FK_SCIM_SYSTEM_ADMIN_SCIM_USER` (`userId`),
  KEY `FK_SCIM_SYSTEM_ADMIN_SCIM_SYSTEM` (`systemId`),
  CONSTRAINT `FK_SCIM_SYSTEM_ADMIN_SCIM_USER` FOREIGN KEY (`userId`) REFERENCES `SCIM_USER` (`userId`),
  CONSTRAINT `FK_SCIM_SYSTEM_ADMIN_SCIM_SYSTEM` FOREIGN KEY (`systemId`) REFERENCES `SCIM_SYSTEM` (`systemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 ws_scim_im.SCIM_SYSTEM_ADMIN:~1 rows (대략적) 내보내기
/*!40000 ALTER TABLE `SCIM_SYSTEM_ADMIN` DISABLE KEYS */;
INSERT INTO `SCIM_SYSTEM_ADMIN` (`userId`, `systemId`, `role`) VALUES
	('sys-scim-admin', 'sys-scim-im', NULL);
/*!40000 ALTER TABLE `SCIM_SYSTEM_ADMIN` ENABLE KEYS */;


-- 테이블 ws_scim_im.SCIM_SYSTEM_COLUMN 구조 내보내기
CREATE TABLE IF NOT EXISTS `SCIM_SYSTEM_COLUMN` (
  `systemId` varchar(50) DEFAULT NULL,
  `columnName` varchar(50) DEFAULT NULL,
  `displayName` varchar(50) DEFAULT NULL,
  `datatype` varchar(50) DEFAULT NULL,
  `dataSize` int(11) DEFAULT NULL,
  `allowNull` int(11) DEFAULT '1',
  `defaultValue` varchar(50) DEFAULT NULL,
  `comment` varchar(50) DEFAULT NULL,
  `mappingColumn` varchar(50) DEFAULT NULL,
  KEY `FK_SCIM_SYSTEM_COLUMN_SCIM_SYSTEM` (`systemId`),
  CONSTRAINT `FK_SCIM_SYSTEM_COLUMN_SCIM_SYSTEM` FOREIGN KEY (`systemId`) REFERENCES `SCIM_SYSTEM` (`systemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 ws_scim_im.SCIM_SYSTEM_COLUMN:~60 rows (대략적) 내보내기
/*!40000 ALTER TABLE `SCIM_SYSTEM_COLUMN` DISABLE KEYS */;
INSERT INTO `SCIM_SYSTEM_COLUMN` (`systemId`, `columnName`, `displayName`, `datatype`, `dataSize`, `allowNull`, `defaultValue`, `comment`, `mappingColumn`) VALUES
	('sys-scim-im', 'organization', '회사 코드', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'division', '부문 명', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'department', '부서 명', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'employeeNumber', '사원 번호', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'companyCode', '회사 코드', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'groupCode', '그룹 코드', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'groupName', '그룹 명칭', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'positionCode', '직위 코드', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'position', '직위 명칭', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'jobCode', '직책 코드', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'job', '직책 코드', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'rankCode', '직책 명칭', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'rank', '직급 이름', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'job', '직책 이름', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'joinDate', '입사 일자', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'retireDate', '퇴직 일자', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'lastAccessDate', '최종 접속 일자', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'eMail', '메일 주소', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'userName', '사용자 이름', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'password', '비밀번호', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'exernalId', '외부 아이디', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'createDate', '생성 일자', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'modifyDate', '변경 일자', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'userId', '사용자 ID', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-im', 'active', '사용 여부', NULL, 0, 0, NULL, NULL, NULL),
	('sys-scim-hr', 'UR_Code', '사용자 아이디', NULL, 0, 0, NULL, NULL, 'userId'),
	('sys-scim-hr', 'GR_Code', '회사 코드', NULL, 0, 0, NULL, NULL, 'organization'),
	('sys-scim-hr', 'EmpNo', '사원 번호', NULL, 0, 0, NULL, NULL, 'employeeNumber'),
	('sys-scim-hr', 'DisplayName', '사용자 이름', NULL, 0, 0, NULL, NULL, 'userName'),
	('sys-scim-hr', 'ExGroupName', '그룹 이름', NULL, 0, 0, NULL, NULL, 'division'),
	('sys-scim-hr', 'JobPositionCode', '직위 코드', NULL, 0, 0, NULL, NULL, 'positionCode'),
	('sys-scim-hr', 'ExJobPositionName', '직위 명칭', NULL, 0, 0, NULL, NULL, 'position'),
	('sys-scim-hr', 'JobTitleCode', '직책 코드', NULL, 0, 0, NULL, NULL, 'jobcode'),
	('sys-scim-hr', 'ExJobTitleName', '직책 명칭', NULL, 0, 0, NULL, NULL, 'job'),
	('sys-scim-hr', 'JobLevelCode', '직급 코드', NULL, 0, 0, NULL, NULL, 'rankCode'),
	('sys-scim-hr', 'ExJobLevelName', '직급 명칭', NULL, 0, 0, NULL, NULL, 'rank'),
	('sys-scim-hr', 'IsUse', '사용 여부', NULL, 0, 0, NULL, NULL, 'active'),
	('sys-scim-hr', 'Ex_PrimaryMail', '이메일', NULL, 0, 0, NULL, NULL, 'eMail'),
	('sys-scim-hr', 'EnterDate', '입사 일자', NULL, 0, 0, NULL, NULL, 'joinDate'),
	('sys-scim-hr', 'RetireDate', '퇴직 일자', NULL, 0, 0, NULL, NULL, 'retireDate'),
	('sys-scim-hr', 'RegistDate', '등록 일자', NULL, 0, 0, NULL, NULL, 'createDate'),
	('sys-scim-hr', 'ModifyDate', '변경 일자', NULL, 0, 0, NULL, NULL, 'modifyDate'),
	('sys-scim-sso', 'ID', '사용자 아이디', NULL, 0, 0, NULL, NULL, 'userId'),
	('sys-scim-sso', 'DIV_ID', '회사 아이디', NULL, 0, 0, NULL, NULL, 'organization'),
	('sys-scim-sso', 'ORG_ID', '그룹 아이디', NULL, 0, 0, NULL, NULL, 'division'),
	('sys-scim-sso', 'PATH_ID', '조직도', NULL, 0, 0, NULL, NULL, 'department'),
	('sys-scim-sso', 'NAME', '사용자 이름', NULL, 0, 0, NULL, NULL, 'userName'),
	('sys-scim-sso', 'DISABLED', '사용여부', NULL, 0, 0, NULL, NULL, 'active'),
	('sys-scim-sso', 'IsUse', '사용 여부', NULL, 0, 0, NULL, NULL, 'active'),
	('sys-scim-sso', 'EMAIL', '이메일', NULL, 0, 0, NULL, NULL, 'eMail'),
	('sys-scim-sso', 'LAST_LOGON_TIME', '최종 로그인 시간', NULL, 0, 0, NULL, NULL, 'lastAccessDate'),
	('sys-scim-sso', 'CREATE_TIME', '등록 일자', NULL, 0, 0, NULL, NULL, 'createDate'),
	('sys-scim-sso', 'MODIFY_TIME', '변경 일자', NULL, 0, 0, NULL, NULL, 'modifyDate'),
	('sys-scim-gw', 'User_ID', '사용자 아이디', NULL, NULL, 1, NULL, NULL, 'userId'),
	('sys-scim-gw', 'User_NAME', '사용자 이름', NULL, NULL, 1, NULL, NULL, 'userName'),
	('sys-scim-gw', 'USER_ORG', '사용자 부서', NULL, NULL, 1, NULL, NULL, 'department'),
	('sys-scim-gw', 'USER_POS', '사용자 직위', NULL, NULL, 1, NULL, NULL, 'position'),
	('sys-scim-gw', 'STATE', '사용자 상태', NULL, NULL, 1, NULL, NULL, 'active'),
	('sys-scim-gw', 'REGIST_DATE', '사용자 들록일', NULL, NULL, 1, NULL, NULL, 'createDate'),
	('sys-scim-gw', 'MODIFY_DATE', '사용자 변경일상', NULL, NULL, 1, NULL, NULL, 'modifyDate');
/*!40000 ALTER TABLE `SCIM_SYSTEM_COLUMN` ENABLE KEYS */;


-- 테이블 ws_scim_im.SCIM_SYSTEM_DATA_DEFINITION 구조 내보내기
CREATE TABLE IF NOT EXISTS `SCIM_SYSTEM_DATA_DEFINITION` (
  `systemId` varchar(50) NOT NULL,
  `dataId` varchar(50) DEFAULT NULL,
  `dataName` varchar(50) DEFAULT NULL,
  `dataType` varchar(50) DEFAULT NULL,
  `dataSize` varchar(50) DEFAULT NULL,
  `nullAble` varchar(50) DEFAULT NULL,
  `dataDefault` varchar(50) DEFAULT NULL,
  `description` varchar(50) DEFAULT NULL,
  KEY `FK_SCIM_SYSTEM_DATA_DEFINITION_SCIM_SYSTEM` (`systemId`),
  CONSTRAINT `FK_SCIM_SYSTEM_DATA_DEFINITION_SCIM_SYSTEM` FOREIGN KEY (`systemId`) REFERENCES `SCIM_SYSTEM` (`systemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- 테이블 데이터 ws_scim_im.SCIM_SYSTEM_DATA_DEFINITION:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `SCIM_SYSTEM_DATA_DEFINITION` DISABLE KEYS */;
/*!40000 ALTER TABLE `SCIM_SYSTEM_DATA_DEFINITION` ENABLE KEYS */;


-- 테이블 ws_scim_im.SCIM_SYSTEM_USER 구조 내보내기
CREATE TABLE IF NOT EXISTS `SCIM_SYSTEM_USER` (
  `systemId` varchar(50) NOT NULL,
  `userId` varchar(50) NOT NULL,
  `externalId` varchar(50) DEFAULT NULL,
  `userName` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `userType` varchar(50) DEFAULT NULL,
  `active` int(11) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `modifyDate` datetime DEFAULT NULL,
  `lastAccessDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- 테이블 데이터 ws_scim_im.SCIM_SYSTEM_USER:~37 rows (대략적) 내보내기
/*!40000 ALTER TABLE `SCIM_SYSTEM_USER` DISABLE KEYS */;
INSERT INTO `SCIM_SYSTEM_USER` (`systemId`, `userId`, `externalId`, `userName`, `password`, `userType`, `active`, `createDate`, `modifyDate`, `lastAccessDate`) VALUES
	('sys-scim-hr', '14358698', NULL, '최미재', NULL, 'USER', 0, '2019-02-22 06:21:44', '2019-02-22 06:21:44', NULL),
	('sys-scim-hr', '15014561', NULL, '장지민', NULL, 'USER', 1, '2019-02-22 06:21:44', '2019-02-22 06:21:44', NULL),
	('sys-scim-hr', '29169444', NULL, '정철일', NULL, 'USER', 0, '2019-02-22 06:21:44', '2019-02-22 06:21:44', NULL),
	('sys-scim-hr', '32376169', NULL, '강구일', NULL, 'USER', 1, '2019-02-22 06:21:44', '2019-02-22 06:21:44', NULL),
	('sys-scim-hr', '50090006', NULL, '이용민', NULL, 'USER', 1, '2019-02-22 06:21:44', '2019-02-22 06:21:44', NULL),
	('sys-scim-hr', '50465181', NULL, '이철호', NULL, 'USER', 1, '2019-02-22 06:21:45', '2019-02-22 06:21:45', NULL),
	('sys-scim-hr', '56386178', NULL, '최구섭', NULL, 'USER', 1, '2019-02-22 06:21:45', '2019-02-22 06:21:45', NULL),
	('sys-scim-hr', '64247504', NULL, '이현민', NULL, 'USER', 0, '2019-02-22 06:21:45', '2019-02-22 06:21:45', NULL),
	('sys-scim-hr', '88162811', NULL, '최연재', NULL, 'USER', 1, '2019-02-22 06:21:45', '2019-02-22 06:21:45', NULL),
	('sys-scim-hr', '94035985', NULL, '박용호', NULL, 'USER', 1, '2019-02-22 06:21:45', '2019-02-22 06:21:45', NULL),
	('sys-scim-sso', '15014561', '15014561', '�옣吏�誘�', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '25837059', '25837059', '議고삎誘�', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '26821332', '26821332', '�옣援ы샇', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '2019-02-01 15:47:30'),
	('sys-scim-sso', '30468129', '30468129', '�옣�뿰以�', NULL, 'DummyUser', 0, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '32376169', '32376169', '媛뺢뎄�씪', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '33073969', '33073969', '源��삎�꽠', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '2018-12-20 06:25:17'),
	('sys-scim-sso', '33420390', '33420390', '媛뺢뎄�씤', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '2019-01-08 16:19:55'),
	('sys-scim-sso', '42433073', '42433073', '�옣�꽦吏�', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '43747193', '43747193', '諛뺢뎄�옱', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '45273379', '45273379', '媛뺤슜�샇', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '2019-02-09 17:57:52'),
	('sys-scim-sso', '50090006', '50090006', '�씠�슜誘�', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '50465181', '50465181', '�씠泥좏샇', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '54191583', '54191583', '�젙�꽦�샇', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '2018-11-25 03:34:31'),
	('sys-scim-sso', '54464586', '54464586', '議곗쿋�씎', NULL, 'DummyUser', 0, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '56386178', '56386178', '理쒓뎄�꽠', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '57280109', '57280109', '理쒖�以�', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '2019-02-12 07:55:04'),
	('sys-scim-sso', '64207500', '64207500', '媛뺤쿋以�', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '70554578', '70554578', '議곕�몄쨷', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '2019-01-31 00:54:47'),
	('sys-scim-sso', '79457483', '79457483', '�젙誘몄옱', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '80282031', '80282031', '諛뺥삎�씪', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '81902889', '81902889', '源�吏��꽠', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '87640523', '87640523', '源��꽦誘�', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '88162811', '88162811', '理쒖뿰�옱', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '94035985', '94035985', '諛뺤슜�샇', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '1970-01-01 00:00:00'),
	('sys-scim-sso', '94671434', '94671434', '�젙�삎�씤', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '2018-09-19 23:21:27'),
	('sys-scim-sso', '97299829', '97299829', '�옣�쁽以�', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '2018-05-04 12:44:38'),
	('sys-scim-sso', '98402111', '98402111', '�쑄援ъ꽠', NULL, 'DummyUser', 1, '2019-02-22 07:28:51', '2019-02-22 07:28:51', '2018-12-06 00:00:14');
/*!40000 ALTER TABLE `SCIM_SYSTEM_USER` ENABLE KEYS */;


-- 테이블 ws_scim_im.SCIM_SYSTEM_USER_PROFILE 구조 내보내기
CREATE TABLE IF NOT EXISTS `SCIM_SYSTEM_USER_PROFILE` (
  `systemId` varchar(50) DEFAULT NULL,
  `userId` varchar(50) DEFAULT NULL,
  `pkey` varchar(50) DEFAULT NULL,
  `pvalue` varchar(50) DEFAULT NULL,
  `pver` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- 테이블 데이터 ws_scim_im.SCIM_SYSTEM_USER_PROFILE:~160 rows (대략적) 내보내기
/*!40000 ALTER TABLE `SCIM_SYSTEM_USER_PROFILE` DISABLE KEYS */;
INSERT INTO `SCIM_SYSTEM_USER_PROFILE` (`systemId`, `userId`, `pkey`, `pvalue`, `pver`) VALUES
	('sys-scim-hr', '14358698', 'companyCode', NULL, NULL),
	('sys-scim-hr', '14358698', 'rankCode', NULL, NULL),
	('sys-scim-hr', '14358698', 'positionCode', NULL, NULL),
	('sys-scim-hr', '14358698', 'jobCode', NULL, NULL),
	('sys-scim-hr', '14358698', 'retireDate', '', NULL),
	('sys-scim-hr', '14358698', 'eMail', '14358698@ehyundai.com', NULL),
	('sys-scim-hr', '14358698', 'employeeNumber', '14358698', NULL),
	('sys-scim-hr', '14358698', 'division', 'Group1', NULL),
	('sys-scim-hr', '14358698', 'groupName', NULL, NULL),
	('sys-scim-hr', '14358698', 'joinDate', '2018-09-05 09:00:00', NULL),
	('sys-scim-hr', '14358698', 'organization', '현대백화점', NULL),
	('sys-scim-hr', '14358698', 'rank', NULL, NULL),
	('sys-scim-hr', '14358698', 'position', '대리', NULL),
	('sys-scim-hr', '14358698', 'job', '팀장', NULL),
	('sys-scim-hr', '14358698', 'department', 'defaultvalue', NULL),
	('sys-scim-hr', '14358698', 'groupCode', NULL, NULL),
	('sys-scim-hr', '15014561', 'companyCode', NULL, NULL),
	('sys-scim-hr', '15014561', 'rankCode', NULL, NULL),
	('sys-scim-hr', '15014561', 'positionCode', NULL, NULL),
	('sys-scim-hr', '15014561', 'jobCode', NULL, NULL),
	('sys-scim-hr', '15014561', 'retireDate', '', NULL),
	('sys-scim-hr', '15014561', 'eMail', '15014561@ehyundai.com', NULL),
	('sys-scim-hr', '15014561', 'employeeNumber', '15014561', NULL),
	('sys-scim-hr', '15014561', 'division', 'Group2', NULL),
	('sys-scim-hr', '15014561', 'groupName', NULL, NULL),
	('sys-scim-hr', '15014561', 'joinDate', '2018-02-21 09:00:00', NULL),
	('sys-scim-hr', '15014561', 'organization', '현대백화점', NULL),
	('sys-scim-hr', '15014561', 'rank', NULL, NULL),
	('sys-scim-hr', '15014561', 'position', '대리', NULL),
	('sys-scim-hr', '15014561', 'job', '파트원', NULL),
	('sys-scim-hr', '15014561', 'department', 'defaultvalue', NULL),
	('sys-scim-hr', '15014561', 'groupCode', NULL, NULL),
	('sys-scim-hr', '29169444', 'companyCode', NULL, NULL),
	('sys-scim-hr', '29169444', 'rankCode', NULL, NULL),
	('sys-scim-hr', '29169444', 'positionCode', NULL, NULL),
	('sys-scim-hr', '29169444', 'jobCode', NULL, NULL),
	('sys-scim-hr', '29169444', 'retireDate', '2018-12-05 09:00:00', NULL),
	('sys-scim-hr', '29169444', 'eMail', '29169444@ehyundai.com', NULL),
	('sys-scim-hr', '29169444', 'employeeNumber', '29169444', NULL),
	('sys-scim-hr', '29169444', 'division', 'Group1', NULL),
	('sys-scim-hr', '29169444', 'groupName', NULL, NULL),
	('sys-scim-hr', '29169444', 'joinDate', '2018-04-26 09:00:00', NULL),
	('sys-scim-hr', '29169444', 'organization', '현대백화점', NULL),
	('sys-scim-hr', '29169444', 'rank', NULL, NULL),
	('sys-scim-hr', '29169444', 'position', '과장', NULL),
	('sys-scim-hr', '29169444', 'job', '파트원', NULL),
	('sys-scim-hr', '29169444', 'department', 'defaultvalue', NULL),
	('sys-scim-hr', '29169444', 'groupCode', NULL, NULL),
	('sys-scim-hr', '32376169', 'companyCode', NULL, NULL),
	('sys-scim-hr', '32376169', 'rankCode', NULL, NULL),
	('sys-scim-hr', '32376169', 'positionCode', NULL, NULL),
	('sys-scim-hr', '32376169', 'jobCode', NULL, NULL),
	('sys-scim-hr', '32376169', 'retireDate', '', NULL),
	('sys-scim-hr', '32376169', 'eMail', '32376169@ehyundai.com', NULL),
	('sys-scim-hr', '32376169', 'employeeNumber', '32376169', NULL),
	('sys-scim-hr', '32376169', 'division', 'Group3', NULL),
	('sys-scim-hr', '32376169', 'groupName', NULL, NULL),
	('sys-scim-hr', '32376169', 'joinDate', '2018-08-07 09:00:00', NULL),
	('sys-scim-hr', '32376169', 'organization', '현대백화점', NULL),
	('sys-scim-hr', '32376169', 'rank', NULL, NULL),
	('sys-scim-hr', '32376169', 'position', '과장', NULL),
	('sys-scim-hr', '32376169', 'job', '팀장', NULL),
	('sys-scim-hr', '32376169', 'department', 'defaultvalue', NULL),
	('sys-scim-hr', '32376169', 'groupCode', NULL, NULL),
	('sys-scim-hr', '50090006', 'companyCode', NULL, NULL),
	('sys-scim-hr', '50090006', 'rankCode', NULL, NULL),
	('sys-scim-hr', '50090006', 'positionCode', NULL, NULL),
	('sys-scim-hr', '50090006', 'jobCode', NULL, NULL),
	('sys-scim-hr', '50090006', 'retireDate', '', NULL),
	('sys-scim-hr', '50090006', 'eMail', '50090006@ehyundai.com', NULL),
	('sys-scim-hr', '50090006', 'employeeNumber', '50090006', NULL),
	('sys-scim-hr', '50090006', 'division', 'Group3', NULL),
	('sys-scim-hr', '50090006', 'groupName', NULL, NULL),
	('sys-scim-hr', '50090006', 'joinDate', '2019-01-15 09:00:00', NULL),
	('sys-scim-hr', '50090006', 'organization', '현대백화점', NULL),
	('sys-scim-hr', '50090006', 'rank', NULL, NULL),
	('sys-scim-hr', '50090006', 'position', '대리', NULL),
	('sys-scim-hr', '50090006', 'job', '파트원', NULL),
	('sys-scim-hr', '50090006', 'department', 'defaultvalue', NULL),
	('sys-scim-hr', '50090006', 'groupCode', NULL, NULL),
	('sys-scim-hr', '50465181', 'companyCode', NULL, NULL),
	('sys-scim-hr', '50465181', 'rankCode', NULL, NULL),
	('sys-scim-hr', '50465181', 'positionCode', NULL, NULL),
	('sys-scim-hr', '50465181', 'jobCode', NULL, NULL),
	('sys-scim-hr', '50465181', 'retireDate', '', NULL),
	('sys-scim-hr', '50465181', 'eMail', '50465181@ehyundai.com', NULL),
	('sys-scim-hr', '50465181', 'employeeNumber', '50465181', NULL),
	('sys-scim-hr', '50465181', 'division', 'Group2', NULL),
	('sys-scim-hr', '50465181', 'groupName', NULL, NULL),
	('sys-scim-hr', '50465181', 'joinDate', '2018-10-13 09:00:00', NULL),
	('sys-scim-hr', '50465181', 'organization', '현대백화점', NULL),
	('sys-scim-hr', '50465181', 'rank', NULL, NULL),
	('sys-scim-hr', '50465181', 'position', '차장', NULL),
	('sys-scim-hr', '50465181', 'job', '파트원', NULL),
	('sys-scim-hr', '50465181', 'department', 'defaultvalue', NULL),
	('sys-scim-hr', '50465181', 'groupCode', NULL, NULL),
	('sys-scim-hr', '56386178', 'companyCode', NULL, NULL),
	('sys-scim-hr', '56386178', 'rankCode', NULL, NULL),
	('sys-scim-hr', '56386178', 'positionCode', NULL, NULL),
	('sys-scim-hr', '56386178', 'jobCode', NULL, NULL),
	('sys-scim-hr', '56386178', 'retireDate', '', NULL),
	('sys-scim-hr', '56386178', 'eMail', '56386178@ehyundai.com', NULL),
	('sys-scim-hr', '56386178', 'employeeNumber', '56386178', NULL),
	('sys-scim-hr', '56386178', 'division', 'Group2', NULL),
	('sys-scim-hr', '56386178', 'groupName', NULL, NULL),
	('sys-scim-hr', '56386178', 'joinDate', '2018-05-10 09:00:00', NULL),
	('sys-scim-hr', '56386178', 'organization', '현대백화점', NULL),
	('sys-scim-hr', '56386178', 'rank', NULL, NULL),
	('sys-scim-hr', '56386178', 'position', '대리', NULL),
	('sys-scim-hr', '56386178', 'job', '파트원', NULL),
	('sys-scim-hr', '56386178', 'department', 'defaultvalue', NULL),
	('sys-scim-hr', '56386178', 'groupCode', NULL, NULL),
	('sys-scim-hr', '64247504', 'companyCode', NULL, NULL),
	('sys-scim-hr', '64247504', 'rankCode', NULL, NULL),
	('sys-scim-hr', '64247504', 'positionCode', NULL, NULL),
	('sys-scim-hr', '64247504', 'jobCode', NULL, NULL),
	('sys-scim-hr', '64247504', 'retireDate', '2018-12-02 09:00:00', NULL),
	('sys-scim-hr', '64247504', 'eMail', '64247504@ehyundai.com', NULL),
	('sys-scim-hr', '64247504', 'employeeNumber', '64247504', NULL),
	('sys-scim-hr', '64247504', 'division', 'Group3', NULL),
	('sys-scim-hr', '64247504', 'groupName', NULL, NULL),
	('sys-scim-hr', '64247504', 'joinDate', '2018-06-09 09:00:00', NULL),
	('sys-scim-hr', '64247504', 'organization', '현대백화점', NULL),
	('sys-scim-hr', '64247504', 'rank', NULL, NULL),
	('sys-scim-hr', '64247504', 'position', '대리', NULL),
	('sys-scim-hr', '64247504', 'job', '파트원', NULL),
	('sys-scim-hr', '64247504', 'department', 'defaultvalue', NULL),
	('sys-scim-hr', '64247504', 'groupCode', NULL, NULL),
	('sys-scim-hr', '88162811', 'companyCode', NULL, NULL),
	('sys-scim-hr', '88162811', 'rankCode', NULL, NULL),
	('sys-scim-hr', '88162811', 'positionCode', NULL, NULL),
	('sys-scim-hr', '88162811', 'jobCode', NULL, NULL),
	('sys-scim-hr', '88162811', 'retireDate', '', NULL),
	('sys-scim-hr', '88162811', 'eMail', '88162811@ehyundai.com', NULL),
	('sys-scim-hr', '88162811', 'employeeNumber', '88162811', NULL),
	('sys-scim-hr', '88162811', 'division', 'Group3', NULL),
	('sys-scim-hr', '88162811', 'groupName', NULL, NULL),
	('sys-scim-hr', '88162811', 'joinDate', '2019-01-18 09:00:00', NULL),
	('sys-scim-hr', '88162811', 'organization', '현대백화점', NULL),
	('sys-scim-hr', '88162811', 'rank', NULL, NULL),
	('sys-scim-hr', '88162811', 'position', '이사', NULL),
	('sys-scim-hr', '88162811', 'job', '파트장', NULL),
	('sys-scim-hr', '88162811', 'department', 'defaultvalue', NULL),
	('sys-scim-hr', '88162811', 'groupCode', NULL, NULL),
	('sys-scim-hr', '94035985', 'companyCode', NULL, NULL),
	('sys-scim-hr', '94035985', 'rankCode', NULL, NULL),
	('sys-scim-hr', '94035985', 'positionCode', NULL, NULL),
	('sys-scim-hr', '94035985', 'jobCode', NULL, NULL),
	('sys-scim-hr', '94035985', 'retireDate', '', NULL),
	('sys-scim-hr', '94035985', 'eMail', '94035985@ehyundai.com', NULL),
	('sys-scim-hr', '94035985', 'employeeNumber', '94035985', NULL),
	('sys-scim-hr', '94035985', 'division', 'Group3', NULL),
	('sys-scim-hr', '94035985', 'groupName', NULL, NULL),
	('sys-scim-hr', '94035985', 'joinDate', '2018-04-11 09:00:00', NULL),
	('sys-scim-hr', '94035985', 'organization', '현대백화점', NULL),
	('sys-scim-hr', '94035985', 'rank', NULL, NULL),
	('sys-scim-hr', '94035985', 'position', '이사', NULL),
	('sys-scim-hr', '94035985', 'job', '파트원', NULL),
	('sys-scim-hr', '94035985', 'department', 'defaultvalue', NULL),
	('sys-scim-hr', '94035985', 'groupCode', NULL, NULL);
/*!40000 ALTER TABLE `SCIM_SYSTEM_USER_PROFILE` ENABLE KEYS */;


-- 테이블 ws_scim_im.SCIM_USER 구조 내보내기
CREATE TABLE IF NOT EXISTS `SCIM_USER` (
  `userId` varchar(50) NOT NULL,
  `userName` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `userType` varchar(50) DEFAULT NULL,
  `active` int(11) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `modifyDate` datetime DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 ws_scim_im.SCIM_USER:~13 rows (대략적) 내보내기
/*!40000 ALTER TABLE `SCIM_USER` DISABLE KEYS */;
INSERT INTO `SCIM_USER` (`userId`, `userName`, `password`, `userType`, `active`, `createDate`, `modifyDate`) VALUES
	('14358698', '최미재', '14358698', 'USER', 0, '2019-02-22 06:24:13', '2019-02-22 06:42:05'),
	('15014561', '장지민', '15014561', 'USER', 1, '2019-02-22 06:24:13', '2019-02-22 06:24:13'),
	('29169444', '정철일', '29169444', 'USER', 0, '2019-02-22 06:24:14', '2019-02-22 06:42:05'),
	('32376169', '강구일', '32376169', 'USER', 1, '2019-02-22 06:24:14', '2019-02-22 06:24:14'),
	('50090006', '이용민', '50090006', 'USER', 1, '2019-02-22 06:24:14', '2019-02-22 06:24:14'),
	('50465181', '이철호', '50465181', 'USER', 1, '2019-02-22 06:24:14', '2019-02-22 06:24:14'),
	('56386178', '최구섭', '56386178', 'USER', 1, '2019-02-22 06:24:14', '2019-02-22 06:24:14'),
	('64247504', '이현민', '64247504', 'USER', 0, '2019-02-22 06:24:14', '2019-02-22 06:42:05'),
	('88162811', '최연재', '88162811', 'USER', 1, '2019-02-22 06:24:14', '2019-02-22 06:24:14'),
	('94035985', '박용호', '94035985', 'USER', 1, '2019-02-22 06:24:14', '2019-02-22 06:24:14'),
	('sys-scim-admin', 'scim-admin', 'pasword!1234', 'ADMIN', NULL, NULL, NULL),
	('sys-scim-gw', 'gw system user', NULL, NULL, NULL, NULL, NULL),
	('sys-scim-scheduler', 'scim-scheduler', NULL, 'SYSTEM', NULL, NULL, NULL);
/*!40000 ALTER TABLE `SCIM_USER` ENABLE KEYS */;


-- 테이블 ws_scim_im.SCIM_USER_PROFILE 구조 내보내기
CREATE TABLE IF NOT EXISTS `SCIM_USER_PROFILE` (
  `userId` varchar(50) DEFAULT NULL,
  `pkey` varchar(50) DEFAULT NULL,
  `pvalue` varchar(50) DEFAULT NULL,
  `pver` varchar(50) DEFAULT NULL,
  KEY `FK_SCIM_USER_PROFILE_SCIM_USER` (`userId`),
  CONSTRAINT `FK_SCIM_USER_PROFILE_SCIM_USER` FOREIGN KEY (`userId`) REFERENCES `SCIM_USER` (`userId`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 ws_scim_im.SCIM_USER_PROFILE:~160 rows (대략적) 내보내기
/*!40000 ALTER TABLE `SCIM_USER_PROFILE` DISABLE KEYS */;
INSERT INTO `SCIM_USER_PROFILE` (`userId`, `pkey`, `pvalue`, `pver`) VALUES
	('14358698', 'companyCode', NULL, NULL),
	('14358698', 'rankCode', NULL, NULL),
	('14358698', 'positionCode', NULL, NULL),
	('14358698', 'jobCode', NULL, NULL),
	('14358698', 'retireDate', '', NULL),
	('14358698', 'eMail', '14358698@ehyundai.com', NULL),
	('14358698', 'employeeNumber', '14358698', NULL),
	('14358698', 'division', 'Group1', NULL),
	('14358698', 'groupName', NULL, NULL),
	('14358698', 'joinDate', '2018-09-05 09:00:00', NULL),
	('14358698', 'organization', '현대백화점', NULL),
	('14358698', 'rank', NULL, NULL),
	('14358698', 'position', '대리', NULL),
	('14358698', 'job', '팀장', NULL),
	('14358698', 'department', 'defaultvalue', NULL),
	('14358698', 'groupCode', NULL, NULL),
	('15014561', 'companyCode', NULL, NULL),
	('15014561', 'rankCode', NULL, NULL),
	('15014561', 'positionCode', NULL, NULL),
	('15014561', 'jobCode', NULL, NULL),
	('15014561', 'retireDate', '', NULL),
	('15014561', 'eMail', '15014561@ehyundai.com', NULL),
	('15014561', 'employeeNumber', '15014561', NULL),
	('15014561', 'division', 'Group2', NULL),
	('15014561', 'groupName', NULL, NULL),
	('15014561', 'joinDate', '2018-02-21 09:00:00', NULL),
	('15014561', 'organization', '현대백화점', NULL),
	('15014561', 'rank', NULL, NULL),
	('15014561', 'position', '대리', NULL),
	('15014561', 'job', '파트원', NULL),
	('15014561', 'department', 'defaultvalue', NULL),
	('15014561', 'groupCode', NULL, NULL),
	('29169444', 'companyCode', NULL, NULL),
	('29169444', 'rankCode', NULL, NULL),
	('29169444', 'positionCode', NULL, NULL),
	('29169444', 'jobCode', NULL, NULL),
	('29169444', 'retireDate', '2018-12-05 09:00:00', NULL),
	('29169444', 'eMail', '29169444@ehyundai.com', NULL),
	('29169444', 'employeeNumber', '29169444', NULL),
	('29169444', 'division', 'Group1', NULL),
	('29169444', 'groupName', NULL, NULL),
	('29169444', 'joinDate', '2018-04-26 09:00:00', NULL),
	('29169444', 'organization', '현대백화점', NULL),
	('29169444', 'rank', NULL, NULL),
	('29169444', 'position', '과장', NULL),
	('29169444', 'job', '파트원', NULL),
	('29169444', 'department', 'defaultvalue', NULL),
	('29169444', 'groupCode', NULL, NULL),
	('32376169', 'companyCode', NULL, NULL),
	('32376169', 'rankCode', NULL, NULL),
	('32376169', 'positionCode', NULL, NULL),
	('32376169', 'jobCode', NULL, NULL),
	('32376169', 'retireDate', '', NULL),
	('32376169', 'eMail', '32376169@ehyundai.com', NULL),
	('32376169', 'employeeNumber', '32376169', NULL),
	('32376169', 'division', 'Group3', NULL),
	('32376169', 'groupName', NULL, NULL),
	('32376169', 'joinDate', '2018-08-07 09:00:00', NULL),
	('32376169', 'organization', '현대백화점', NULL),
	('32376169', 'rank', NULL, NULL),
	('32376169', 'position', '과장', NULL),
	('32376169', 'job', '팀장', NULL),
	('32376169', 'department', 'defaultvalue', NULL),
	('32376169', 'groupCode', NULL, NULL),
	('50090006', 'companyCode', NULL, NULL),
	('50090006', 'rankCode', NULL, NULL),
	('50090006', 'positionCode', NULL, NULL),
	('50090006', 'jobCode', NULL, NULL),
	('50090006', 'retireDate', '', NULL),
	('50090006', 'eMail', '50090006@ehyundai.com', NULL),
	('50090006', 'employeeNumber', '50090006', NULL),
	('50090006', 'division', 'Group3', NULL),
	('50090006', 'groupName', NULL, NULL),
	('50090006', 'joinDate', '2019-01-15 09:00:00', NULL),
	('50090006', 'organization', '현대백화점', NULL),
	('50090006', 'rank', NULL, NULL),
	('50090006', 'position', '대리', NULL),
	('50090006', 'job', '파트원', NULL),
	('50090006', 'department', 'defaultvalue', NULL),
	('50090006', 'groupCode', NULL, NULL),
	('50465181', 'companyCode', NULL, NULL),
	('50465181', 'rankCode', NULL, NULL),
	('50465181', 'positionCode', NULL, NULL),
	('50465181', 'jobCode', NULL, NULL),
	('50465181', 'retireDate', '', NULL),
	('50465181', 'eMail', '50465181@ehyundai.com', NULL),
	('50465181', 'employeeNumber', '50465181', NULL),
	('50465181', 'division', 'Group2', NULL),
	('50465181', 'groupName', NULL, NULL),
	('50465181', 'joinDate', '2018-10-13 09:00:00', NULL),
	('50465181', 'organization', '현대백화점', NULL),
	('50465181', 'rank', NULL, NULL),
	('50465181', 'position', '차장', NULL),
	('50465181', 'job', '파트원', NULL),
	('50465181', 'department', 'defaultvalue', NULL),
	('50465181', 'groupCode', NULL, NULL),
	('56386178', 'companyCode', NULL, NULL),
	('56386178', 'rankCode', NULL, NULL),
	('56386178', 'positionCode', NULL, NULL),
	('56386178', 'jobCode', NULL, NULL),
	('56386178', 'retireDate', '', NULL),
	('56386178', 'eMail', '56386178@ehyundai.com', NULL),
	('56386178', 'employeeNumber', '56386178', NULL),
	('56386178', 'division', 'Group2', NULL),
	('56386178', 'groupName', NULL, NULL),
	('56386178', 'joinDate', '2018-05-10 09:00:00', NULL),
	('56386178', 'organization', '현대백화점', NULL),
	('56386178', 'rank', NULL, NULL),
	('56386178', 'position', '대리', NULL),
	('56386178', 'job', '파트원', NULL),
	('56386178', 'department', 'defaultvalue', NULL),
	('56386178', 'groupCode', NULL, NULL),
	('64247504', 'companyCode', NULL, NULL),
	('64247504', 'rankCode', NULL, NULL),
	('64247504', 'positionCode', NULL, NULL),
	('64247504', 'jobCode', NULL, NULL),
	('64247504', 'retireDate', '2018-12-02 09:00:00', NULL),
	('64247504', 'eMail', '64247504@ehyundai.com', NULL),
	('64247504', 'employeeNumber', '64247504', NULL),
	('64247504', 'division', 'Group3', NULL),
	('64247504', 'groupName', NULL, NULL),
	('64247504', 'joinDate', '2018-06-09 09:00:00', NULL),
	('64247504', 'organization', '현대백화점', NULL),
	('64247504', 'rank', NULL, NULL),
	('64247504', 'position', '대리', NULL),
	('64247504', 'job', '파트원', NULL),
	('64247504', 'department', 'defaultvalue', NULL),
	('64247504', 'groupCode', NULL, NULL),
	('88162811', 'companyCode', NULL, NULL),
	('88162811', 'rankCode', NULL, NULL),
	('88162811', 'positionCode', NULL, NULL),
	('88162811', 'jobCode', NULL, NULL),
	('88162811', 'retireDate', '', NULL),
	('88162811', 'eMail', '88162811@ehyundai.com', NULL),
	('88162811', 'employeeNumber', '88162811', NULL),
	('88162811', 'division', 'Group3', NULL),
	('88162811', 'groupName', NULL, NULL),
	('88162811', 'joinDate', '2019-01-18 09:00:00', NULL),
	('88162811', 'organization', '현대백화점', NULL),
	('88162811', 'rank', NULL, NULL),
	('88162811', 'position', '이사', NULL),
	('88162811', 'job', '파트장', NULL),
	('88162811', 'department', 'defaultvalue', NULL),
	('88162811', 'groupCode', NULL, NULL),
	('94035985', 'companyCode', NULL, NULL),
	('94035985', 'rankCode', NULL, NULL),
	('94035985', 'positionCode', NULL, NULL),
	('94035985', 'jobCode', NULL, NULL),
	('94035985', 'retireDate', '', NULL),
	('94035985', 'eMail', '94035985@ehyundai.com', NULL),
	('94035985', 'employeeNumber', '94035985', NULL),
	('94035985', 'division', 'Group3', NULL),
	('94035985', 'groupName', NULL, NULL),
	('94035985', 'joinDate', '2018-04-11 09:00:00', NULL),
	('94035985', 'organization', '현대백화점', NULL),
	('94035985', 'rank', NULL, NULL),
	('94035985', 'position', '이사', NULL),
	('94035985', 'job', '파트원', NULL),
	('94035985', 'department', 'defaultvalue', NULL),
	('94035985', 'groupCode', NULL, NULL);
/*!40000 ALTER TABLE `SCIM_USER_PROFILE` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
