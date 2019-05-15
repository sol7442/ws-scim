package com.ehyundai.im;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.ehyundai.object.User;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.policy.impl.DefaultPasswordPoilcy;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMServerResourceRepository;
import com.wowsanta.scim.repository.system.SCIMProviderRepository;
import com.wowsanta.scim.repository.system.SCIMSystemRepository;
import com.wowsanta.scim.resource.SCIMSystemColumn;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.util.Random;

public class IMProviderRepositoryTest {

	private final String config_file = "../config/home_dev_scim-service-provider.json";
	//private final String repository_config_file = "../config/ehyundai_im_oracle_repository.json";
	private final String repository_config_file = "../config/im_oracle_repository.json";
	
	private final int create_user_size = 1;
	
	@Test
	public void admin_test() {
		
		load_manager(this.repository_config_file);
		SCIMProviderRepository system_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
		try {
			
			SCIMAdmin sch_gw_sync_admin = new SCIMAdmin();
			sch_gw_sync_admin.setAdminId("sch-gw-sync");
			sch_gw_sync_admin.setAdminName("그룹웨어 동기화 스케줄러");
			sch_gw_sync_admin.setAdminType(SCIMDefinitions.AdminType.SYS_SCHEDULER.toString());
			
			
			SCIMAdmin sys_root_admdin = new SCIMAdmin();
			sys_root_admdin.setAdminId("sys-root-admin");
			sys_root_admdin.setAdminName("SCIM ROOT ADMIN");
			sys_root_admdin.setPassword("PASSWORD1234!");
			
			DefaultPasswordPoilcy policy = new DefaultPasswordPoilcy();
			sys_root_admdin.setPassword(policy.encrypt(sys_root_admdin.getPassword()));
			
			sys_root_admdin.setAdminType(SCIMDefinitions.AdminType.SYS_ADMIN.toString());
			
//			system_repository.createAdmin(sch_gw_sync_admin);
//			system_repository.createAdmin(sys_root_admdin);
			
//			system_repository.updateAdmin(admin);
//			system_repository.deleteAdmin(admin.getAdminId());
			SCIMAdmin scim_admin = system_repository.getAdmin(sch_gw_sync_admin.getAdminId());
			if(scim_admin != null) {
				System.out.println(scim_admin.toString(true));
			}
			
			
			Calendar cal = Calendar.getInstance();
			Date to_day = cal.getTime();
			cal.add(Calendar.DATE, -90*1);	// 1 년간의 데이터 모두
			Date expire_day = cal.getTime();
			
			sys_root_admdin.setLoginTime(to_day);
			sys_root_admdin.setPwExpireTime(expire_day);
			system_repository.updateAdmin(sys_root_admdin);
			
			List<SCIMAdmin> admin_list = system_repository.getAdminList();
			System.out.println("admin list size : " + admin_list.size());
			for (SCIMAdmin scimAdmin : admin_list) {
				System.out.println(scimAdmin.toString(true));
				//system_repository.deleteAdmin(sch_gw_sync_admin.getAdminId());
			}
			
		}catch (SCIMException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	//@Test
	public void add_sso_column() {
		load_manager(this.repository_config_file);
		SCIMProviderRepository system_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
		
		try {
			
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-sso","ID","사용자 아이디"	 ,	"userId"			));
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-sso","DIV_ID","회사 아이디"	,"organization"		));
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-sso","ORG_ID","그룹 아이디"	,"groupName"		));
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-sso","PATH_ID","조직도"	  	, ""		));
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-sso","NAME","사용자 이름","userName"			));
			
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-sso","DISABLED","사용여부"	,"active"	));

			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-sso","IsUse","사용 여부","active")       );        
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-sso","EMAIL","이메일","eMail")           );
			   									
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-sso","LAST_LOGON_TIME","최종 로그인 시간","lastAccessDate")          );
			
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-sso","CREATE_TIME","등록 일자" , "createDate")      );       									
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-sso","MODIFY_TIME","변경 일자" , "modifyDate")          );
			
		} catch (SCIMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//@Test
	public void get_gw_column() {
		load_manager(this.repository_config_file);
		SCIMProviderRepository provider_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
		
		try {
			
			List<SCIMSystemColumn> column_list = provider_repository.getSystemColumnsBySystemId("sys-scim-gw");
			
			for (SCIMSystemColumn scimSystemColumn : column_list) {
				System.out.println(scimSystemColumn);
			}
			
		} catch (SCIMException e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void add_gw_column() {
		load_manager(this.repository_config_file);
		SCIMProviderRepository system_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
		
		try {
			
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","UR_Code","사용자 아이디"	 ,"userId"			));
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","GR_Code","회사 코드"	  	 ,"organization"		));   
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","EmpNo","사원 번호"		 ,"employeeNumber"	));       
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","DisplayName","사용자 이름","userName"			));     
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","ExGroupName","그룹 이름"	 ,"division"			));
			
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","JobPositionCode","직위 코드", 	"positionCode")   );    
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","ExJobPositionName","직위 명칭" 	,"position")     );      
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","JobTitleCode","직책 코드"		,"jobcode"		)     );      
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","ExJobTitleName","직책 명칭"		,"job")  );
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","JobLevelCode","직급 코드"		,"rankCode"));
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","ExJobLevelName","직급 명칭"		,"rank"));
			
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","IsUse","사용 여부","active")       );        
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","Ex_PrimaryMail","이메일","eMail")           );
			
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","EnterDate","입사 일자","joinDate")      );       									
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","RetireDate","퇴직 일자","retireDate")          );
			
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","RegistDate","등록 일자" , "createDate")      );       									
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-gw","ModifyDate","변경 일자" , "modifyDate")          );
			
			//system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","createDate","생성 일자")    );     
			//system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","modifyDate","변경 일자"));
			
			
			
		} catch (SCIMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Test
	public void add_im_column() {
		load_manager(this.repository_config_file);
		SCIMProviderRepository system_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
		
		try {
			
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","organization","회사 코드")  );   
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","division","부문 명")      );       
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","department","부서 명")    );     
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","employeeNumber","사원 번호")); 
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","companyCode","회사 코드")   );    
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","groupCode","그룹 코드")     );      
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","groupName","그룹 명칭")     );      
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","positionCode","직위 코드")  );   
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","position","직위 명칭")      );       
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","jobCode","직책 코드")       );        
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","job","직책 코드")           );            
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","rankCode","직책 명칭")      );       									
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","rank","직급 이름")          );  		     
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","job","직책 이름")           );			       
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","joinDate","입사 일자")      );       
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","retireDate","퇴직 일자")    );     
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","lastAccessDate","최종 접속 일자")); 
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","eMail","메일 주소")         );          
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","userName","사용자 이름")    );     
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","password","비밀번호")       );        
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","exernalId","외부 아이디")   ); 
			
			
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","createDate","생성 일자")    );     
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","modifyDate","변경 일자"));
			
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","userId","사용자 ID")   );
			system_repository.addSystemColumn(new SCIMSystemColumn("sys-scim-im","active","사용 여부")   );
			
		} catch (SCIMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//repository_config_file
	}
	
	public void load_manager(String conf_file_path) {
		try {
			SCIMRepositoryManager.loadFromFile(conf_file_path).initailze();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void groupware_repository_create_20000_test() {
		try {
			
			load_manager(config_file);
			createUsers(create_user_size);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void groupware_repository_update() {
		try {
			
			load_manager(config_file);
			
			SCIMServerResourceRepository res_repo = (SCIMServerResourceRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
			
			List<SCIMUser> user_list = res_repo.getUsersByActive();
			System.out.println("user_list["+user_list.size()+"]");
			if(user_list.size() > 0) {
				SCIMUser user_0 = user_list.get(0);
				user_0.setActive(!user_0.isActive());
				
				System.out.println("UPDATE : " + user_0.isActive());
				
				res_repo.updateUser(user_0);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void im_repository_get_all() {
		try {
			
			load_manager(config_file);
			
			SCIMServerResourceRepository res_repo = (SCIMServerResourceRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
			
			List<SCIMUser> user_list = res_repo.getUsersByActive();
			System.out.println("user_list["+user_list.size()+"]");
			for (SCIMUser scimUser : user_list) {
				System.out.println(scimUser.toString());
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	//@Test
	public void im_repository_get_by_date() {
		try {
			
			load_manager(config_file);
			
			SCIMServerResourceRepository res_repo = (SCIMServerResourceRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
			
			Calendar cal = Calendar.getInstance();
			Date to = cal.getTime();
			cal.add(Calendar.DATE, -365*10);	// 10 년간의 데이터 모두
			Date from = cal.getTime();
			
			List<SCIMUser> user_list = res_repo.getUsersByDate(from, to);
			System.out.println("user_list["+user_list.size()+"]");
			for (SCIMUser scimUser : user_list) {
				System.out.println(scimUser.toString());
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	//
	public void createUsers(int size) {
		SCIMServerResourceRepository res_repo = (SCIMServerResourceRepository)SCIMRepositoryManager.getInstance().getResourceRepository();
		
		for (int i = 0; i < size; i++) {
			User user = new User();
			user.setId(Random.number(10000000, 99999999));
			user.setEmployeeNumber(user.getId());
			user.setUserName(Random.name());
			user.setActive(Random.yn_boolean(90));
			user.setPosition(Random.position());
			user.setJob(Random.job());

			user.seteMail(user.getEmployeeNumber() + "@ehyundai.com");

			user.setCompanyCode(Random.number(0, 5));
			user.setGroupCode(Random.number(0, 10));

			Date join_date   = Random.beforeYears(1);
			Date retire_date = null;
			Date create_date = join_date;//Random.beforeYears(1);
			Date modify_date = null;
			
			user.setMeta(new SCIMUserMeta());
			if (user.isActive()) {
				if(Random.yn_boolean(10)) {	// 신입사원 비율
					modify_date = create_date;
				}else {
					modify_date = Random.date(create_date, new Date());
				}
				
			}else {
				if(Random.yn_boolean(40)) { // 비 활성화된 사용자 중 40 % 1년 안에 퇴사한 사람  
					retire_date   = Random.beforeYears(1);
					modify_date   = retire_date;
				}else {
					retire_date   = Random.date(create_date, new Date());
					modify_date   = retire_date;
				}
			}
			
			user.setJoinDate(join_date);
			user.setRetireDate(retire_date);
			user.getMeta().setCreated(create_date);
			user.getMeta().setLastModified(modify_date);
			
			System.out.println(user.toString());

			try {
				res_repo.createUser(user);
			} catch (SCIMException e) {
				e.printStackTrace();
			}
		}
	}
}
