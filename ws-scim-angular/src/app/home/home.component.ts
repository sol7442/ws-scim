import { Component } from '@angular/core';
import {MenuItem} from 'primeng/api';

@Component({
    selector: 'home',
    templateUrl: 'home.component.html',
    styleUrls: ['home.component.scss']
})
export class HomeComponent {
    items: MenuItem[];
    ngOnInit() {
        this.items = [
            {label: 'Stats', icon: 'fa fa-fw fa-bar-chart', routerLink:[""]},
            {label: 'Calendar', icon: 'fa fa-fw fa-calendar'},
            {label: 'Documentation', icon: 'fa fa-fw fa-book'},
            {label: 'Support', icon: 'fa fa-fw fa-support'},
            {label: 'Social', icon: 'fa fa-fw fa-twitter'}
        ];
    }
}
