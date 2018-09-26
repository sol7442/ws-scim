import { Routes, RouterModule } from '@angular/router';

import {AuthComponent} from './auth/auth.component';

import {LoginComponent } from './login/login.component';
import {HomeComponent} from './home/home.component';

const appRoutes: Routes = [
    { path: '', component: HomeComponent, canActivate: [AuthComponent] },
    { path: 'login', component: LoginComponent },

    // otherwise redirect to home
    { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes);