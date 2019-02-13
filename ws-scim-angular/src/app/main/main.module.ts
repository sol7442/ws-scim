// Angular Imports
import { NgModule } from '@angular/core';

// This Module's Components
//import {MenubarModule} from 'primeng/menubar';
import {TabMenuModule} from 'primeng/tabmenu';

import { TabMenuComponent } from '../menu/tab-menu/tab-menu.component';

import { MainComponent } from './main.component';
import { MainRoutingModule }  from './main-routing.module';

 
import { SystemManagementComponent }  from './system/system-management/system-management.component';
import { AccountComponent }  from './account/account.component';

@NgModule({
    imports: [
        MainRoutingModule, TabMenuModule
    ],
    declarations: [
        MainComponent, 
        TabMenuComponent,
        SystemManagementComponent,AccountComponent
    ],
    exports: [        
        
    ]
})
export class MainModule {

}
