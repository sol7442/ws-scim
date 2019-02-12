DELETE SCIM_USER WHERE userId='asdf'

INSERT INTO SCIM_USER (userId,userName) VALUES ('sys-scim-admin','scim-admin');
INSERT INTO SCIM_SYSTEM (systemId,systemName) VALUES ('sys-scim-im','scim-im');
INSERT INTO SCIM_SYSTEM_ADMIN (userId,systemId) VALUES ('sys-scim-admin','sys-scim-im');

SELECT U.userId, U.userName, U.password, SA.systemId FROM SCIM_USER U , SCIM_SYSTEM_ADMIN SA WHERE U.userId = SA.userId AND U.userId = 'sys-scim-admin'

UPDATE SCIM_USER SET password='pasword!1234' WHERE userId = 'sys-scim-admin'