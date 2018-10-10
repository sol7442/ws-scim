// Angular Imports
import { NgModule } from '@angular/core';

import {TreeModule} from 'primeng/tree';
import {ScrollPanelModule} from 'primeng/scrollpanel';

// This Module's Components
import { EnvAngentComponent } from './env-angent.component';


@NgModule({
    imports: [
        ScrollPanelModule
    ],
    declarations: [
        EnvAngentComponent,
    ],
    exports: [
        EnvAngentComponent,ScrollPanelModule
    ]
})
export class EnvAngentModule {

}
