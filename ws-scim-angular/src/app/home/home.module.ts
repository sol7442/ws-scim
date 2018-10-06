// Angular Imports
import { NgModule } from '@angular/core';

// This Module's Components
import {TabMenuModule} from 'primeng/tabmenu';

import { HomeComponent } from './home.component';
import {HomeRoutingModule }  from './home-routing.module';



@NgModule({
    imports: [
        TabMenuModule,
        HomeRoutingModule
    ],
    declarations: [
        HomeComponent,
    ],
    exports: [
        HomeComponent,
        TabMenuModule
    ]
})
export class HomeModule {

}
