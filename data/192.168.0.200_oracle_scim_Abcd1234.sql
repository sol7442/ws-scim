-- ����� ���̺� --
SELECT * FROM SCIM_USER;
SELECT * FROM SCIM_USER_PROFILE;
SELECT * FROM SCIM_USER WHERE USERID='46975646';
SELECT * FROM SCIM_USER_PROFILE WHERE USERID='46975646';

SELECT count(*) FROM SCIM_USER_PROFILE;

ALTER TABLE SCIM_USER ADD(PWCHANGEDATE DATE DEFAULT NULL); 

DELETE FROM SCIM_USER;
DELETE FROM SCIM_USER_PROFILE;

-- ������ ���̺� --
SELECT * FROM SCIM_ADMIN;
INSERT INTO SCIM_ADMIN (ADMINID,ADMINNAME,ADMINTYPE) VALUES ('sys-admin','RELLY','OPERATOR');
ALTER TABLE SCIM_ADMIN ADD(ACTIVE NUMBER(10,0) DEFAULT 1); 
DELETE FROM SCIM_ADMIN WHERE ADMINID='ADMIN' --'61271125';

-- �����ٷ� --
SELECT * FROM SCIM_SCHEDULER;

UPDATE SCIM_SCHEDULER SET LASTEXECUTEDATE='18/01/01' WHERE SCHEDULERID='sch-sync-gw';
UPDATE SCIM_SCHEDULER SET LASTEXECUTEDATE=null WHERE SCHEDULERID='sch-sync-gw';


-- ���� ������ --
SELECT * FROM SCIM_AUDIT ORDER BY WORKDATE;
SELECT * FROM SCIM_SCHEDULER_HISTORY;


DELETE FROM SCIM_AUDIT;
DELETE FROM SCIM_SCHEDULER_HISTORY;

ALTER TABLE SCIM_SCHEDULER_HISTORY ADD (WORKERID VARCHAR2(50 CHAR)); 
ALTER TABLE SCIM_SCHEDULER_HISTORY ADD(SUCCESSCOUNT NUMBER(10,0) DEFAULT 0); 
ALTER TABLE SCIM_SCHEDULER_HISTORY ADD(FAILCOUNT NUMBER(10,0) DEFAULT 0); 

ALTER TABLE SCIM_SCHEDULER_HISTORY DROP COLUMN REQPOST;
ALTER TABLE SCIM_SCHEDULER_HISTORY DROP COLUMN REQPUT;
ALTER TABLE SCIM_SCHEDULER_HISTORY DROP COLUMN REQDELETE;
ALTER TABLE SCIM_SCHEDULER_HISTORY DROP COLUMN REQPATCH;
ALTER TABLE SCIM_SCHEDULER_HISTORY DROP COLUMN RESPOST;
ALTER TABLE SCIM_SCHEDULER_HISTORY DROP COLUMN RESPUT;
ALTER TABLE SCIM_SCHEDULER_HISTORY DROP COLUMN RESDELETE;
ALTER TABLE SCIM_SCHEDULER_HISTORY DROP COLUMN RESPATCH;



DELETE FROM SCIM_AUDIT ;

ALTER TABLE SCIM_AUDIT ADD (WORKERTYPE VARCHAR2(50 CHAR)); 
ALTER TABLE SCIM_AUDIT ADD (ACTION VARCHAR2(50 CHAR)); 
ALTER TABLE SCIM_AUDIT RENAME COLUMN ADMINID TO WORKERID;
ALTER TABLE SCIM_AUDIT RENAME COLUMN DIRECTSYSTEM TO TARGETSYSTEMID;
ALTER TABLE SCIM_AUDIT RENAME COLUMN SOURCESYSTEM TO SOURCESYSTEMID;