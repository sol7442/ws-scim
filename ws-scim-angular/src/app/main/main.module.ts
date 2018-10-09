// Angular Imports
import { NgModule } from '@angular/core';

// This Module's Components
import {MenubarModule} from 'primeng/menubar';
import {TabMenuModule} from 'primeng/tabmenu';

import { MainComponent } from './main.component';
import { MainRoutingModule }  from './main-routing.module';
import {WorkComponent} from './work/work.component';
import {AccountComponent} from './account/account.component';
import {PolicyComponent} from './policy/policy.component';
import {AuditComponent} from './audit/audit.component';
import {EnvironmentComponent} from './environment/environment.component';
import {EnvAngentModule} from './environment/env-angent/env-angent.module'

@NgModule({
    imports: [
        MenubarModule,TabMenuModule,
        MainRoutingModule,
        EnvAngentModule

    ],
    declarations: [
        MainComponent,WorkComponent,AccountComponent,PolicyComponent,AuditComponent,EnvironmentComponent
    ],
    exports: [        
        
    ]
})
export class MainModule {

}
