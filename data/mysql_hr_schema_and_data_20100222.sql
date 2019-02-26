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

-- ws_scim_hr1 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `ws_scim_hr1` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ws_scim_hr1`;


-- 테이블 ws_scim_hr1.GW_USER 구조 내보내기
CREATE TABLE IF NOT EXISTS `GW_USER` (
  `UR_Code` varchar(50) NOT NULL,
  `GR_Code` varchar(50) DEFAULT NULL,
  `EmpNo` varchar(50) DEFAULT NULL,
  `DisplayName` varchar(50) DEFAULT NULL,
  `ExGroupName` varchar(50) DEFAULT NULL,
  `JobPositionCode` varchar(50) DEFAULT NULL,
  `ExJobPositionName` varchar(50) DEFAULT NULL,
  `JobTitleCode` varchar(50) DEFAULT NULL,
  `ExJobTitleName` varchar(50) DEFAULT NULL,
  `JobLevelCode` varchar(50) DEFAULT NULL,
  `ExJobLevelName` varchar(50) DEFAULT NULL,
  `IsUse` varchar(50) DEFAULT NULL,
  `Ex_PrimaryMail` varchar(50) DEFAULT NULL,
  `EnterDate` datetime DEFAULT NULL,
  `RetireDate` datetime DEFAULT NULL,
  `RegistDate` datetime DEFAULT NULL,
  `ModifyDate` datetime DEFAULT NULL,
  PRIMARY KEY (`UR_Code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 테이블 데이터 ws_scim_hr1.GW_USER:~20 rows (대략적) 내보내기
/*!40000 ALTER TABLE `GW_USER` DISABLE KEYS */;
INSERT INTO `GW_USER` (`UR_Code`, `GR_Code`, `EmpNo`, `DisplayName`, `ExGroupName`, `JobPositionCode`, `ExJobPositionName`, `JobTitleCode`, `ExJobTitleName`, `JobLevelCode`, `ExJobLevelName`, `IsUse`, `Ex_PrimaryMail`, `EnterDate`, `RetireDate`, `RegistDate`, `ModifyDate`) VALUES
	('14358698', '현대백화점', '14358698', '최미재', 'Group1', NULL, '대리', NULL, '팀장', NULL, NULL, 'N', '14358698@ehyundai.com', '2018-09-05 00:00:00', NULL, '2018-09-05 00:00:00', '2018-10-03 00:00:00'),
	('15014561', '현대백화점', '15014561', '장지민', 'Group2', NULL, '대리', NULL, '파트원', NULL, NULL, 'Y', '15014561@ehyundai.com', '2018-02-21 00:00:00', NULL, '2018-02-21 00:00:00', '2018-04-07 00:00:00'),
	('17794541', NULL, '17794541', '이지재', 'Group3', NULL, '사원', NULL, '파트원', NULL, NULL, 'Y', '17794541@ehyundai.com', '2018-04-14 00:00:00', NULL, '2018-04-14 00:00:00', '2018-10-26 00:00:00'),
	('20158170', NULL, '20158170', '이형호', 'Group3', NULL, '부장', NULL, '파트원', NULL, NULL, 'Y', '20158170@ehyundai.com', '2018-12-09 00:00:00', NULL, '2018-12-09 00:00:00', '2019-01-15 00:00:00'),
	('28408190', NULL, '28408190', '장철진', 'Group3', NULL, '대리', NULL, '파트원', NULL, NULL, 'N', '28408190@ehyundai.com', '2019-01-21 00:00:00', '2019-02-02 00:00:00', '2019-01-21 00:00:00', '2019-02-02 00:00:00'),
	('29169444', '현대백화점', '29169444', '정철일', 'Group1', NULL, '과장', NULL, '파트원', NULL, NULL, 'N', '29169444@ehyundai.com', '2018-04-26 00:00:00', '2018-12-05 00:00:00', '2018-04-26 00:00:00', '2018-12-05 00:00:00'),
	('32376169', '현대백화점', '32376169', '강구일', 'Group3', NULL, '과장', NULL, '팀장', NULL, NULL, 'Y', '32376169@ehyundai.com', '2018-08-07 00:00:00', NULL, '2018-08-07 00:00:00', '2018-08-07 00:00:00'),
	('44782724', NULL, '44782724', '정연민', 'Group2', NULL, '대리', NULL, '팀장', NULL, NULL, 'Y', '44782724@ehyundai.com', '2018-11-11 00:00:00', NULL, '2018-11-11 00:00:00', '2018-11-23 00:00:00'),
	('50090006', '현대백화점', '50090006', '이용민', 'Group3', NULL, '대리', NULL, '파트원', NULL, NULL, 'Y', '50090006@ehyundai.com', '2019-01-15 00:00:00', NULL, '2019-01-15 00:00:00', '2019-02-19 00:00:00'),
	('50465181', '현대백화점', '50465181', '이철호', 'Group2', NULL, '차장', NULL, '파트원', NULL, NULL, 'Y', '50465181@ehyundai.com', '2018-10-13 00:00:00', NULL, '2018-10-13 00:00:00', '2019-01-18 00:00:00'),
	('56386178', '현대백화점', '56386178', '최구섭', 'Group2', NULL, '대리', NULL, '파트원', NULL, NULL, 'Y', '56386178@ehyundai.com', '2018-05-10 00:00:00', NULL, '2018-05-10 00:00:00', '2018-12-17 00:00:00'),
	('64247504', '현대백화점', '64247504', '이현민', 'Group3', NULL, '대리', NULL, '파트원', NULL, NULL, 'N', '64247504@ehyundai.com', '2018-06-09 00:00:00', '2018-12-02 00:00:00', '2018-06-09 00:00:00', '2018-12-02 00:00:00'),
	('69487399', NULL, '69487399', '정구중', 'Group2', NULL, '과장', NULL, '파트원', NULL, NULL, 'Y', '69487399@ehyundai.com', '2019-01-28 00:00:00', NULL, '2019-01-28 00:00:00', '2019-02-08 00:00:00'),
	('74842218', NULL, '74842218', '정미섭', 'Group1', NULL, '차장', NULL, '', NULL, NULL, 'Y', '74842218@ehyundai.com', '2018-07-09 00:00:00', NULL, '2018-07-09 00:00:00', '2018-07-09 00:00:00'),
	('75233981', NULL, '75233981', '박성민', 'Group2', NULL, '대리', NULL, '파트원', NULL, NULL, 'Y', '75233981@ehyundai.com', '2018-08-29 00:00:00', NULL, '2018-08-29 00:00:00', '2019-01-19 00:00:00'),
	('75854680', NULL, '75854680', '조철인', 'Group2', NULL, '과장', NULL, '파트원', NULL, NULL, 'Y', '75854680@ehyundai.com', '2018-04-02 00:00:00', NULL, '2018-04-02 00:00:00', '2018-11-26 00:00:00'),
	('88162811', '현대백화점', '88162811', '최연재', 'Group3', NULL, '이사', NULL, '파트장', NULL, NULL, 'Y', '88162811@ehyundai.com', '2019-01-18 00:00:00', NULL, '2019-01-18 00:00:00', '2019-02-06 00:00:00'),
	('89932650', NULL, '89932650', '김성인', 'Group1', NULL, '과장', NULL, '파트원', NULL, NULL, 'Y', '89932650@ehyundai.com', '2018-09-17 00:00:00', NULL, '2018-09-17 00:00:00', '2018-09-17 00:00:00'),
	('94035985', '현대백화점', '94035985', '박용호', 'Group3', NULL, '이사', NULL, '파트원', NULL, NULL, 'Y', '94035985@ehyundai.com', '2018-04-11 00:00:00', NULL, '2018-04-11 00:00:00', '2018-05-16 00:00:00'),
	('95857314', NULL, '95857314', '김형일', 'Group3', NULL, '사원', NULL, '파트원', NULL, NULL, 'Y', '95857314@ehyundai.com', '2018-07-17 00:00:00', NULL, '2018-07-17 00:00:00', '2018-12-23 00:00:00');
/*!40000 ALTER TABLE `GW_USER` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
