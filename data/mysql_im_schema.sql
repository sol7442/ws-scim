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

-- 내보낼 데이터가 선택되어 있지 않습니다.


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

-- 내보낼 데이터가 선택되어 있지 않습니다.


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
  `sourceSystemId` varchar(50) DEFAULT NULL,
  `targetSystemId` varchar(50) DEFAULT NULL,
  `lastExecuteDate` datetime DEFAULT NULL,
  KEY `FK_SCIM_SCHEDULER_SCIM_SYSTEM` (`sourceSystemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.


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

-- 내보낼 데이터가 선택되어 있지 않습니다.


-- 테이블 ws_scim_im.SCIM_SYSTEM 구조 내보내기
CREATE TABLE IF NOT EXISTS `SCIM_SYSTEM` (
  `systemId` varchar(50) NOT NULL,
  `systemName` varchar(50) DEFAULT NULL,
  `systemDesc` varchar(50) DEFAULT NULL,
  `systemUrl` varchar(50) DEFAULT NULL,
  `systemType` varchar(50) NOT NULL,
  PRIMARY KEY (`systemId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.


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

-- 내보낼 데이터가 선택되어 있지 않습니다.


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

-- 내보낼 데이터가 선택되어 있지 않습니다.


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

-- 내보낼 데이터가 선택되어 있지 않습니다.


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

-- 내보낼 데이터가 선택되어 있지 않습니다.


-- 테이블 ws_scim_im.SCIM_USER_PROFILE 구조 내보내기
CREATE TABLE IF NOT EXISTS `SCIM_USER_PROFILE` (
  `userId` varchar(50) DEFAULT NULL,
  `pkey` varchar(50) DEFAULT NULL,
  `pvalue` varchar(50) DEFAULT NULL,
  `pver` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
