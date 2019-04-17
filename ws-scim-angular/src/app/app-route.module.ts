import { Routes, RouterModule } from '@angular/router';

import {AuthGuard} from './guards/auth-guard';

import {AppComponent } from './app.component';
import {LoginComponent } from './login/login.component';

import {SystemManagementComponent} from './main/system/system-management/system-management.component';
import {HrsystemManagementComponent} from './main/system/hrsystem-management/hrsystem-management.component';
import {AccountComponent} from './main/account/account.component';
import {SystemAccountComponent} from './main/account/system-account/system-account.component';
import {ImAccountComponent} from './main/account/im-account/im-account.component';
import {AuditManagementComponent } from './main/audit/audit-management/audit-management.component';

import {EnvironmentComponent } from './main/environment/environment.component';
import {EnvAdminComponent } from './main/environment/env-admin/env-admin.component';
import {EnvHrAgentComponent } from './main/environment/env-hr-agent/env-hr-agent.component';
import {EnvSysAgentComponent } from './main/environment/env-sys-agent/env-sys-agent.component';

import {EnvSchedulerComponent } from './main/environment/env-scheduler/env-scheduler.component';
import {EnvLogComponent } from './main/environment/env-log/env-log.component';

const appRoutes: Routes = [
    { path: '', component: AppComponent, canActivate: [AuthGuard] },    
    { path: 'login', component: LoginComponent },
    { path: 'main/hrsystem',component:HrsystemManagementComponent, canActivate: [AuthGuard]},
    { path: 'main/system',component:SystemManagementComponent, canActivate: [AuthGuard]},
    { path: 'main/account',
        component:AccountComponent,
        children:[
            { path: "", redirectTo: "im_account", pathMatch: "full" },
            { path: "im_account", component: ImAccountComponent      },
            { path: "sys_account/:id", component: SystemAccountComponent},
        ],
        canActivate: [AuthGuard]},        
    { path: 'main/auditmgr',component:AuditManagementComponent, canActivate: [AuthGuard]}, 
    { path: 'main/environment',
        component:EnvironmentComponent,
        children: [
            { path: "", redirectTo: "main/environment/admin", pathMatch: "full" },
            { path: "admin"     , component: EnvAdminComponent    },
            { path: "hr_agent"  , component: EnvHrAgentComponent  },
            { path: "sys_agent" , component: EnvSysAgentComponent },
            { path: "env_log"   , component: EnvLogComponent      }
          ]         
         , canActivate: [AuthGuard]  
    }, 
    //{ path: 'admin',component: EnvAdminComponent        , outlet: "evn" },
    { path: '**', redirectTo: '' }
];

export const AppRouteModule = RouterModule.forRoot(appRoutes,{ enableTracing: true } );