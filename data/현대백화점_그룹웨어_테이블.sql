CREATE TABLE "BASE_OBJECT_UR" (
	"UR_Code" VARCHAR(50) NULL,
	"DN_Code" VARCHAR(50) NULL,
	"GR_Code" VARCHAR(50) NULL,
	"EmpNo" VARCHAR(50) NULL,
	"DisplayName" VARCHAR(50) NULL,
	"ExGroupName" VARCHAR(50) NULL,
	"JobPositionCode" VARCHAR(50) NULL,
	"ExJobPositionName" VARCHAR(50) NULL,
	"JobTitleCode" VARCHAR(50) NULL,
	"ExJobTitleName" VARCHAR(50) NULL,
	"JobLevelCode" VARCHAR(50) NULL,
	"ExJobLevelName" VARCHAR(50) NULL,
	"IsUse" VARCHAR(50) NULL,
	"Ex_PrimaryMail" VARCHAR(50) NULL,
	"EnterDate" DATE NULL,
	"RetireDate" DATE NULL,
	"RegistDate" DATE NULL,
	"ModifyDate" DATE NULL
);

SELECT COUNT(*) FROM BASE_OBJECT_UR

SELECT UR_Code, DisplayName , EnterDate, RegistDate, RetireDate,  ModifyDate FROM BASE_OBJECT_UR WHERE RetireDate IS NOT NULL ORDER BY ModifyDate