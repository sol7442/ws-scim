import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; 
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import {TabMenuModule} from 'primeng/tabmenu';

import {ConfigService, ConfigModule} from './service/config.service';
import {AppComponent } from './app.component';
import {AppRouteModule} from './app-route.module';


import {AuthGuard} from './guards/auth-guard';

import {AuthenticationService} from './service/authentication.service';
import {ScimApiService} from './service/scim-api.service';
import {AlertService} from './service/alert.service';
import {AuthInterceptor} from './service/auth-interceptor';
import {HttpErrorInerceptor} from './service/http-error-inerceptor';





import {LoginModule} from './login/login.module';
import {AlertComponent} from './alert/alert.component';

import {SchemaMappingPanelComponent} from './common/schema-mapping-panel/schema-mapping-panel.component'
import {SchemaMappingDlgComponent} from './common/schema-mapping-dlg/schema-mapping-dlg.component'
import {RepositoryMappingPanelComponent} from './common/repository-mapping-panel/repository-mapping-panel.component'
import {RepositoryMappingDlgComponent} from './common/repository-mapping-dlg/repository-mapping-dlg.component'
import {SchedulerListPanelComponent} from './common/scheduler-list-panel/scheduler-list-panel.component'
import {AdminInfoDlgComponent} from './common/admin-info-dlg/admin-info-dlg.component'
import {SystemInfoDlgComponent} from './common/system-info-dlg/system-info-dlg.component'
import {SchedulerInfoDlgComponent} from './common/scheduler-info-dlg/scheduler-info-dlg.component'
import {SchedulerHistoryDlgComponent} from './common/scheduler-history-dlg/scheduler-history-dlg.component'
import {ConnectionPanelComponent} from './common/connection-panel/connection-panel.component'

import {TabMenuComponent} from './menu/tab-menu/tab-menu.component';
import {TitleComponent} from './title/title.component';
import {SystemManagementComponent} from './main/system/system-management/system-management.component';
import {HrsystemManagementComponent} from './main/system/hrsystem-management/hrsystem-management.component';
import {AccountComponent} from './main/account/account.component';
import {SystemAccountComponent} from './main/account/system-account/system-account.component';
import {ImAccountComponent} from './main/account/im-account/im-account.component';
import {AuditManagementComponent } from './main/audit/audit-management/audit-management.component';
import {EnvironmentComponent } from './main/environment/environment.component';
import {EnvAdminComponent } from './main/environment/env-admin/env-admin.component';
import {EnvSchedulerComponent } from './main/environment/env-scheduler/env-scheduler.component';
import {EnvHrAgentComponent } from './main/environment/env-hr-agent/env-hr-agent.component';
import {EnvSysAgentComponent } from './main/environment/env-sys-agent/env-sys-agent.component';
import {EnvLogComponent } from './main/environment/env-log/env-log.component';

import {ToolbarModule} from 'primeng/toolbar';
import {ButtonModule} from 'primeng/button';
import {ListboxModule} from 'primeng/listbox';
import {SplitButtonModule} from 'primeng/splitbutton';
import {PanelModule} from 'primeng/panel';
import {DataViewModule} from 'primeng/dataview';
import {DialogModule} from 'primeng/dialog';
import {TableModule} from 'primeng/table';
import {PaginatorModule} from 'primeng/paginator';
import {CardModule} from 'primeng/card';
import {TooltipModule} from 'primeng/tooltip';
import {DropdownModule} from 'primeng/dropdown';
import {ScrollPanelModule} from 'primeng/scrollpanel';
import {EditorModule} from 'primeng/editor';
import {FileUploadModule} from 'primeng/fileupload';
import {ChartModule} from 'primeng/chart';
import {InputSwitchModule} from 'primeng/inputswitch';
import {PasswordModule} from 'primeng/password';





import 'rxjs/Rx';

@NgModule({
   declarations: [
      AppComponent,AlertComponent,TabMenuComponent,TitleComponent,
      SchemaMappingPanelComponent,SchemaMappingDlgComponent,
      RepositoryMappingPanelComponent,RepositoryMappingDlgComponent,
      SchedulerListPanelComponent,ConnectionPanelComponent,
      AdminInfoDlgComponent,SystemInfoDlgComponent,SchedulerInfoDlgComponent,SchedulerHistoryDlgComponent,
      SystemManagementComponent,HrsystemManagementComponent,
      AccountComponent,SystemAccountComponent,ImAccountComponent,
      AuditManagementComponent,
      EnvironmentComponent,EnvAdminComponent,EnvHrAgentComponent,EnvSysAgentComponent,EnvSchedulerComponent,EnvLogComponent
   ],
   imports: [
      BrowserModule,FormsModule,
      BrowserAnimationsModule,ReactiveFormsModule,
      TabMenuModule,ToolbarModule,TooltipModule,
      ButtonModule,SplitButtonModule,PanelModule,DropdownModule,
      ListboxModule,DataViewModule,DialogModule,TableModule,PaginatorModule,CardModule,
      ScrollPanelModule,EditorModule,FileUploadModule,ChartModule,InputSwitchModule,PasswordModule,
      AppRouteModule,
      HttpClientModule,
      LoginModule,
   ],
   providers: [
      {provide:HTTP_INTERCEPTORS,  useClass:AuthInterceptor,  multi:true  },
      {provide:HTTP_INTERCEPTORS,  useClass:HttpErrorInerceptor,  multi:true  },
      ConfigService,
      ConfigModule.init(),
      AuthGuard,
      AlertService,AuthenticationService,ScimApiService
   ],
   bootstrap: [
      AppComponent
   ]
})
export class AppModule { }
