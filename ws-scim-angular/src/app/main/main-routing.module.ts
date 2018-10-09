import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';

import {LoginComponent } from '../login/login.component';
import {MainComponent} from '../main/main.component';

import {WorkComponent} from './work/work.component';
import {AccountComponent} from './account/account.component';
import {PolicyComponent} from './policy/policy.component';
import {AuditComponent} from './audit/audit.component';
import {EnvironmentComponent} from './environment/environment.component';
import {EnvAngentComponent} from './environment/env-angent/env-angent.component'

const mainRoutes: Routes = [
    { path: 'main', 
      component: MainComponent,
      children:[
          {path:'',component:WorkComponent},
          {path:'work',component:WorkComponent},
          {path:'account',component:AccountComponent},
          {path:'policy',component:PolicyComponent},
          {path:'audit',component:AuditComponent},
          {path:'environment',component:EnvironmentComponent},
          {path:'env_agent',component:EnvAngentComponent},
          
      ] 
    },
];

@NgModule({
    imports: [ RouterModule.forChild(mainRoutes) ],
    exports: [ RouterModule ]
  })

export class MainRoutingModule{ } 