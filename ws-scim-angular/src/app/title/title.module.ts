import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TitleComponent } from './title.component';

import {ToolbarModule} from 'primeng/toolbar';
import {ButtonModule} from 'primeng/button';
import {SplitButtonModule} from 'primeng/splitbutton';


@NgModule({
  imports: [
    CommonModule, 
    ToolbarModule,ButtonModule,SplitButtonModule
  ],
  declarations: [TitleComponent]
})
export class TitleModule { }
