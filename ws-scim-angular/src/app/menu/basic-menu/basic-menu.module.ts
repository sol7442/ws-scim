import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BasicMenuComponent } from './basic-menu.component';
import {PanelMenuModule} from 'primeng/panelmenu';

@NgModule({
  imports: [
    CommonModule,
    PanelMenuModule
  ],
  declarations: [BasicMenuComponent],
  exports: [
    BasicMenuComponent
  ]
})
export class BasicMenuModule { }
