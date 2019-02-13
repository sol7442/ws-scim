import { Routes, RouterModule } from '@angular/router';

import {AuthComponent} from './auth/auth.component';

import {LoginComponent } from './login/login.component';
//import {HomeComponent} from './home/home.component';
import {MainComponent} from './main/main.component';
//    path: 'lazy', loadChildren: 'app/player.module#PlayerModule'

const appRoutes: Routes = [
    { path: '', component: MainComponent, canActivate: [AuthComponent] },
    { path: 'login', component: LoginComponent },
    { path: 'main' , component: MainComponent},
    { path: '**', redirectTo: '' }
];

export const AppRouteModule = RouterModule.forRoot(appRoutes);