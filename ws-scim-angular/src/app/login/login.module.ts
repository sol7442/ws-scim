// Angular Imports
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {MatCardModule} from '@angular/material';

// This Module's Components
import { LoginComponent } from './login.component';
import {CardModule} from 'primeng/card';
import {ButtonModule} from 'primeng/button';

@NgModule({
    imports: [
        CardModule,
        ButtonModule,
        FormsModule,
        MatCardModule
    ],
    declarations: [
        LoginComponent,
    ],
    exports: [
        LoginComponent,
        CardModule,
    ]
})
export class LoginModule {
    
}
