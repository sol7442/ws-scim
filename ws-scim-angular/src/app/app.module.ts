import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; 

import {AuthComponent} from './auth/auth.component';
import {SCIMService} from './service/service.component';
import {LoginModule} from './login/login.module';

import {AppComponent } from './app.component';

import {routing} from './app-routing.module';
import { HomeComponent } from './home/home.component';

import {BasicMenuModule} from './menu/basic-menu/basic-menu.module';
import {BasicMenuComponent} from './menu/basic-menu/basic-menu.component';

import {PanelMenuModule} from 'primeng/panelmenu';


@NgModule({
   declarations: [
      AppComponent,
      HomeComponent,
      BasicMenuComponent
   ],
   imports: [
      BrowserModule,BrowserAnimationsModule,
      HttpClientModule,
      LoginModule,
      PanelMenuModule,
      routing
   ],
   providers: [
      AuthComponent,
      SCIMService
   ],
   bootstrap: [
      AppComponent
   ]
})
export class AppModule { }
