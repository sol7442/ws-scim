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

-- ws_scim_sso 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `ws_scim_sso` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ws_scim_sso`;


-- 테이블 ws_scim_sso.WA3_USER 구조 내보내기
CREATE TABLE IF NOT EXISTS `WA3_USER` (
  `ID` varchar(64) NOT NULL,
  `NAME` varchar(64) NOT NULL,
  `RRN` varchar(64) DEFAULT '''''',
  `EMAIL` varchar(256) DEFAULT '''''',
  `CREATOR` varchar(64) DEFAULT '''''',
  `MODIFIER` varchar(64) DEFAULT '''''',
  `INFO` varchar(512) DEFAULT '''''',
  `DIV_ID` varchar(64) NOT NULL COMMENT '사용자 구분정책 ID',
  `ORG_ID` varchar(64) NOT NULL COMMENT '조직',
  `PATH_ID` varchar(512) NOT NULL,
  `PWD1` varchar(256) DEFAULT NULL,
  `PWD2` varchar(256) DEFAULT NULL,
  `PWD_MUST_CHANGE` tinyint(4) NOT NULL DEFAULT '0' COMMENT '비밀번호 변경 필요여부 (0:필요없음, 1:필요함)',
  `PWD_CHANGE_TIME` bigint(20) DEFAULT NULL,
  `PWD_RETRY_COUNT` int(11) NOT NULL DEFAULT '0',
  `PWD_RETRY_TIME` bigint(20) NOT NULL DEFAULT '0',
  `ACCOUNT_PROFILE` varchar(1024) DEFAULT '''''',
  `ACCESS_ALLOW` tinyint(4) NOT NULL DEFAULT '1',
  `DISABLED` tinyint(4) NOT NULL DEFAULT '0',
  `DISABLED_REASON` varchar(512) DEFAULT '''''',
  `LOCKED` tinyint(4) NOT NULL DEFAULT '0' COMMENT '계정잠금 상태 (0:UNLOCK, 1:LOCK)',
  `LOGIN_POLICY_ID` bigint(20) DEFAULT NULL,
  `VALID_FROM` bigint(20) NOT NULL DEFAULT '0',
  `VALID_TO` bigint(20) NOT NULL DEFAULT '2147385600000',
  `LAST_LOGON_TIME` bigint(20) NOT NULL DEFAULT '0',
  `LAST_LOGON_IP` varchar(20) DEFAULT NULL COMMENT 'LAST 접속 IP',
  `ORG_DELAY_INFO` varchar(64) DEFAULT NULL,
  `CREATE_TIME` bigint(20) DEFAULT NULL,
  `MODIFY_TIME` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='사용자';

-- 테이블 데이터 ws_scim_sso.WA3_USER:~27 rows (대략적) 내보내기
/*!40000 ALTER TABLE `WA3_USER` DISABLE KEYS */;
INSERT INTO `WA3_USER` (`ID`, `NAME`, `RRN`, `EMAIL`, `CREATOR`, `MODIFIER`, `INFO`, `DIV_ID`, `ORG_ID`, `PATH_ID`, `PWD1`, `PWD2`, `PWD_MUST_CHANGE`, `PWD_CHANGE_TIME`, `PWD_RETRY_COUNT`, `PWD_RETRY_TIME`, `ACCOUNT_PROFILE`, `ACCESS_ALLOW`, `DISABLED`, `DISABLED_REASON`, `LOCKED`, `LOGIN_POLICY_ID`, `VALID_FROM`, `VALID_TO`, `LAST_LOGON_TIME`, `LAST_LOGON_IP`, `ORG_DELAY_INFO`, `CREATE_TIME`, `MODIFY_TIME`) VALUES
	('15014561', '장지민', '\'\'', '15014561@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group2', 'defaultvalue', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 0, NULL, NULL, 1550783049000, 1550783049000),
	('25837059', '조형민', '\'\'', '25837059@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group1', '인사', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 0, NULL, NULL, 1543378522476, 1548649108632),
	('26821332', '장구호', '\'\'', '26821332@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group3', '개발1', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 1549036050877, NULL, NULL, 1530011575202, 1549883022809),
	('30468129', '장연중', '\'\'', '30468129@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group1', '영업1', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 0, '\'\'', 0, NULL, 0, 2147385600000, 0, NULL, NULL, 1526773448925, 1525796734066),
	('32376169', '강구일', '\'\'', '32376169@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group3', 'defaultvalue', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 0, NULL, NULL, 1550783049000, 1550783049000),
	('33073969', '김형섭', '\'\'', '33073969@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group3', '영업1', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 1545287117268, NULL, NULL, 1524452388455, 1528339832563),
	('33420390', '강구인', '\'\'', '33420390@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group2', '총무', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 1546964395100, NULL, NULL, 1536743089006, 1540623159985),
	('42433073', '장성진', '\'\'', '42433073@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group1', '영업2', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 0, NULL, NULL, 1543370551627, 1543370551627),
	('43747193', '박구재', '\'\'', '43747193@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group1', '개발2', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 0, NULL, NULL, 1542080990117, 1547037151622),
	('45273379', '강용호', '\'\'', '45273379@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group1', '영업2', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 1549735072643, NULL, NULL, 1539829115656, 1549640572476),
	('50090006', '이용민', '\'\'', '50090006@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group3', 'defaultvalue', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 0, NULL, NULL, 1550783050000, 1550783050000),
	('50465181', '이철호', '\'\'', '50465181@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group2', 'defaultvalue', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 0, NULL, NULL, 1550783050000, 1550783050000),
	('54191583', '정성호', '\'\'', '54191583@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group1', '인사', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 1543116871266, NULL, NULL, 1531089367009, 1535205735358),
	('54464586', '조철흥', '\'\'', '54464586@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group2', '개발2', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 0, '\'\'', 0, NULL, 0, 2147385600000, 0, NULL, NULL, 1542201959008, 1544524611694),
	('56386178', '최구섭', '\'\'', '56386178@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group2', 'defaultvalue', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 0, NULL, NULL, 1550783050000, 1550783050000),
	('57280109', '최지중', '\'\'', '57280109@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group2', '인사', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 1549958104289, NULL, NULL, 1549586465966, 1549605080122),
	('64207500', '강철중', '\'\'', '64207500@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group1', '영업2', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 0, NULL, NULL, 1524218107135, 1549199878567),
	('70554578', '조미중', '\'\'', '70554578@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group3', '개발1', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 1548896087302, NULL, NULL, 1547556524258, 1549242147952),
	('79457483', '정미재', '\'\'', '79457483@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group3', '개발1', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 0, NULL, NULL, 1532090646996, 1549530120724),
	('80282031', '박형일', '\'\'', '80282031@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group2', '인사', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 0, NULL, NULL, 1522415237133, 1535246356704),
	('81902889', '김지섭', '\'\'', '81902889@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group1', '영업1', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 0, NULL, NULL, 1521502413377, 1541623707685),
	('87640523', '김성민', '\'\'', '87640523@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group1', '영업2', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 0, NULL, NULL, 1519499415791, 1520017012674),
	('88162811', '최연재', '\'\'', '88162811@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group3', 'defaultvalue', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 0, NULL, NULL, 1550783050000, 1550783050000),
	('94035985', '박용호', '\'\'', '94035985@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group3', 'defaultvalue', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 0, NULL, NULL, 1550783050000, 1550783050000),
	('94671434', '정형인', '\'\'', '94671434@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group2', '개발2', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 1537399287451, NULL, NULL, 1525821038088, 1539430131952),
	('97299829', '장현중', '\'\'', '97299829@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group3', '영업2', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 1525437878953, NULL, NULL, 1524289332418, 1531922632344),
	('98402111', '윤구섭', '\'\'', '98402111@ehyundai.com', '\'\'', '\'\'', '\'\'', '현대백화점', 'Group1', '영업1', NULL, NULL, 0, NULL, 0, 0, '\'\'', 1, 1, '\'\'', 1, NULL, 0, 2147385600000, 1544054414833, NULL, NULL, 1528137255429, 1532443763055);
/*!40000 ALTER TABLE `WA3_USER` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
