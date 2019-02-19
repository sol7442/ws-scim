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

-- 내보낼 데이터가 선택되어 있지 않습니다.


-- 테이블 ws_scim_hr1.USER 구조 내보내기
CREATE TABLE IF NOT EXISTS `USER` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` varchar(50) NOT NULL,
  `USER_NAME` varchar(50) DEFAULT NULL,
  `USER_ORG` varchar(50) DEFAULT NULL,
  `USER_ROLE` varchar(50) DEFAULT NULL,
  `USER_POS` varchar(50) DEFAULT NULL,
  `STATE` int(10) unsigned DEFAULT '0',
  `REGIST_DATE` date DEFAULT NULL,
  `MODIFY_DATE` date DEFAULT NULL,
  `RELEASE_DATE` date DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
