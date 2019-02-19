import { Routes, RouterModule } from '@angular/router';

import {AuthGuard} from './guards/auth-guard';

import {AppComponent } from './app.component';
import {LoginComponent } from './login/login.component';

import {SystemManagementComponent} from './main/system/system-management/system-management.component';
import {HrsystemManagementComponent} from './main/system/hrsystem-management/hrsystem-management.component';
import {AccountComponent} from './main/account/account.component';
import {SystemAccountComponent} from './main/account/system-account/system-account.component';
import {AccountManagementComponent} from './main/account/account-management/account-management.component';

const appRoutes: Routes = [
    { path: '', component: AppComponent, canActivate: [AuthGuard] },    
    { path: 'login', component: LoginComponent },
    { path: 'main/hrsystem',component:HrsystemManagementComponent},
    { path: 'main/system',component:SystemManagementComponent},
    { path: 'main/sysaccount',component:SystemAccountComponent},    
    { path: 'main/accountmgr',component:AccountManagementComponent}, 

    { path: '**', redirectTo: '' }
];

export const AppRouteModule = RouterModule.forRoot(appRoutes);