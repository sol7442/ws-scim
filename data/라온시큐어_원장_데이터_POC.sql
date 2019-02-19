
-- 전체 사원 수 --
SELECT COUNT(*) FROM GW_USER
SELECT * FROM GW_USER

-- 재직 사원 수 --
SELECT COUNT(*) FROM GW_USER WHERE isUse = "Y"
SELECT * FROM GW_USER WHERE isUse = "Y"

-- 신입 사원 수 --
SELECT count(*) FROM GW_USER WHERE RegistDate = ModifyDate
SELECT * FROM GW_USER WHERE RegistDate = ModifyDate

-- 퇴직 사원 수 --
SELECT COUNT(*) FROM GW_USER WHERE isUse = "N"
SELECT * FROM GW_USER WHERE isUse = "Y"

SELECT * FROM GW_USER WHERE UR_Code = "90078827"

SELECT * FROM 

UPDATE GW_USER  SET ModifyDate=