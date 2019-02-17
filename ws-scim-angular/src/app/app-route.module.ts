import { Routes, RouterModule } from '@angular/router';

import {AuthGuard} from './guards/auth-guard';

import {AppComponent } from './app.component';
import {LoginComponent } from './login/login.component';

import {SystemManagementComponent} from './main/system/system-management/system-management.component';
import {AccountComponent} from './main/account/account.component';

const appRoutes: Routes = [
    { path: '', component: AppComponent, canActivate: [AuthGuard] },
    { path: 'login', component: LoginComponent },
    { path: 'main/system',component:SystemManagementComponent},
    { path: 'main/account',component:AccountComponent},    
    { path: '**', redirectTo: '' }
];

export const AppRouteModule = RouterModule.forRoot(appRoutes);