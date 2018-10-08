import { Routes, RouterModule } from '@angular/router';

import {AuthComponent} from './auth/auth.component';

import {LoginComponent } from './login/login.component';
import {MainComponent} from './main/main.component';

const appRoutes: Routes = [
    { path: '', component: MainComponent, canActivate: [AuthComponent] },
    { path: 'login', component: LoginComponent },
    { path: 'main', component: MainComponent },

    // otherwise redirect to home
    { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes);