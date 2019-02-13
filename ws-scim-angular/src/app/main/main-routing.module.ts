import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';

import {MainComponent} from './main.component';

import {SystemManagementComponent} from './system/system-management/system-management.component';

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
          {path:'',component:SystemManagementComponent},
          {path:'main/system',component:SystemManagementComponent},
          {path:'main/account',component:AccountComponent},
      ] 
    },
];

@NgModule({
    imports: [ RouterModule.forChild(mainRoutes) ],
    exports: [ RouterModule ]
  })

export class MainRoutingModule{ } 