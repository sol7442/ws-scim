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

import {TabMenuComponent} from './menu/tab-menu/tab-menu.component';
import {TitleComponent} from './title/title.component';
import {SystemManagementComponent} from './main/system/system-management/system-management.component';
import {HrsystemManagementComponent} from './main/system/hrsystem-management/hrsystem-management.component';
import {AccountComponent} from './main/account/account.component';
import {SystemAccountComponent} from './main/account/system-account/system-account.component';
import {AccountManagementComponent} from './main/account/account-management/account-management.component';
import {AuditManagementComponent } from './main/audit/audit-management/audit-management.component';
import {EnvironmentComponent } from './main/environment/environment.component';
import {EnvAdminComponent } from './main/environment/env-admin/env-admin.component';
import {EnvAngentComponent } from './main/environment/env-angent/env-angent.component';
import {EnvSchedulerComponent } from './main/environment/env-scheduler/env-scheduler.component';

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





import 'rxjs/Rx';

@NgModule({
   declarations: [
      AppComponent,AlertComponent,
      TabMenuComponent,TitleComponent,
      SystemManagementComponent,HrsystemManagementComponent,
      AccountComponent,SystemAccountComponent,AccountManagementComponent,
      AuditManagementComponent,
      EnvironmentComponent,EnvAdminComponent,EnvAngentComponent,EnvSchedulerComponent
   ],
   imports: [
      BrowserModule,FormsModule,
      BrowserAnimationsModule,ReactiveFormsModule,
      TabMenuModule,ToolbarModule,TooltipModule,
      ButtonModule,SplitButtonModule,PanelModule,
      ListboxModule,DataViewModule,DialogModule,TableModule,PaginatorModule,CardModule,
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
