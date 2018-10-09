// Angular Imports
import { NgModule } from '@angular/core';

// This Module's Components
import { EnvAngentComponent } from './env-angent.component';
import {TreeModule} from 'primeng/tree';

@NgModule({
    imports: [
        TreeModule
    ],
    declarations: [
        EnvAngentComponent,
    ],
    exports: [
        EnvAngentComponent,TreeModule
    ]
})
export class EnvAngentModule {

}
