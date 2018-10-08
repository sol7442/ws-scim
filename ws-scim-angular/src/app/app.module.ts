import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import {AuthComponent} from './auth/auth.component';
import {SCIMService} from './service/service.component';
import {LoginModule} from './login/login.module';
import {MainModule} from './main/main.module';

import {AppComponent } from './app.component';

import {routing} from './app-routing.module';




@NgModule({
  declarations: [
    AppComponent    
  ],
  imports: [
    BrowserModule,HttpClientModule,    
    LoginModule,MainModule,
    routing,

  ],
  providers: [
    AuthComponent,
    SCIMService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
