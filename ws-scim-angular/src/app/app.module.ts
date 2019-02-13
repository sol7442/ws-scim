import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import {ConfigService, ConfigModule} from './service/config.service';


import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; 

import {AppComponent } from './app.component';
import {AppRouteModule} from './app-route.module';

import {AuthenticationService} from './service/authentication.service';

import {AuthComponent} from './auth/auth.component';
import {SCIMService} from './service/service.component';
import {SCIMAuthService} from './service/SCIMAuth.service';
import {LoginModule} from './login/login.module';



//import { HomeComponent } from './home/home.component';
//import { MainComponent } from './main/main.component';

//import {TabMenuComponent} from './menu/tab-menu/tab-menu.component';
//import {BasicMenuComponent} from './menu/basic-menu/basic-menu.component';

import { MainModule } from './main/main.module';

//import {PanelMenuModule} from 'primeng/panelmenu';
//import {TabMenuModule} from 'primeng/tabmenu';

@NgModule({
   declarations: [
      AppComponent,
   ],
   imports: [
      BrowserModule,BrowserAnimationsModule,AppRouteModule,
      HttpClientModule,
      LoginModule,
      MainModule          
   ],
   providers: [
      ConfigService,ConfigModule.init(),
      AuthenticationService,
      AuthComponent,
      SCIMService,
      SCIMAuthService
   ],
   bootstrap: [
      AppComponent
   ]
})
export class AppModule { }
