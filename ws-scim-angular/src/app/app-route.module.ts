import { Routes, RouterModule } from '@angular/router';

import {AuthGuard} from './guards/auth-guard';

import {AppComponent } from './app.component';
import {LoginComponent } from './login/login.component';

import {SystemManagementComponent} from './main/system/system-management/system-management.component';
import {HrsystemManagementComponent} from './main/system/hrsystem-management/hrsystem-management.component';
import {AccountComponent} from './main/account/account.component';
import {SystemAccountComponent} from './main/account/system-account/system-account.component';
import {AccountManagementComponent} from './main/account/account-management/account-management.component';
import {AuditManagementComponent } from './main/audit/audit-management/audit-management.component';

import {EnvironmentComponent } from './main/environment/environment.component';
import {EnvAdminComponent } from './main/environment/env-admin/env-admin.component';
import {EnvHrAgentComponent } from './main/environment/env-hr-agent/env-hr-agent.component';
import {EnvSchedulerComponent } from './main/environment/env-scheduler/env-scheduler.component';
import {EnvLogComponent } from './main/environment/env-log/env-log.component';

const appRoutes: Routes = [
    { path: '', component: AppComponent, canActivate: [AuthGuard] },    
    { path: 'login', component: LoginComponent },
    { path: 'main/hrsystem',component:HrsystemManagementComponent},
    { path: 'main/system',component:SystemManagementComponent},
    { path: 'main/sysaccount',component:SystemAccountComponent},    
    { path: 'main/accountmgr',component:AccountManagementComponent}, 
    { path: 'main/auditmgr',component:AuditManagementComponent}, 
    { path: 'main/environment',
        component:EnvironmentComponent,
        children: [
            { path: "", redirectTo: "admin", pathMatch: "full" },
            { path: "admin", component: EnvAdminComponent   },
            { path: "hr_agent", component: EnvHrAgentComponent  },
            { path: "env_log", component: EnvLogComponent  }
          ]
    }, 

    { path: '**', redirectTo: '' }
];

export const AppRouteModule = RouterModule.forRoot(appRoutes,{ enableTracing: true } );