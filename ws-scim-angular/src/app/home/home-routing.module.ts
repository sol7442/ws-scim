import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';



import {LoginComponent } from '../login/login.component';
import {HomeComponent} from '../home/home.component';

const homeRoutes: Routes = [
    { path: 'home', 
      component: HomeComponent,
      children:[
          {path:'',component:LoginComponent}
      ] 
    },
];

@NgModule({
    imports: [ RouterModule.forChild(homeRoutes) ],
    exports: [ RouterModule ]
  })

export class HomeRoutingModule{ } 