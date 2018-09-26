import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import {AuthComponent} from './auth/auth.component';
import {SCIMService} from './service/service.component';
import {LoginModule} from './login/login.module';


import {AppComponent } from './app.component';
import {HomeComponent } from './home/home.component';

import {routing} from './app.router';




@NgModule({
  declarations: [
    AppComponent,    
    HomeComponent
  ],
  imports: [
    BrowserModule,HttpClientModule,    
    LoginModule,
    routing,

  ],
  providers: [
    AuthComponent,
    SCIMService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
