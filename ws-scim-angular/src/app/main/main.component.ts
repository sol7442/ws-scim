import { Component } from '@angular/core';
import {MenuItem} from 'primeng/api';

@Component({
    selector: 'main',
    templateUrl: 'main.component.html',
    styleUrls: ['main.component.scss']
})
export class MainComponent {
    menus: MenuItem[];
    ngOnInit() {
        this.menus = [
            {label: '작업현황', icon: 'fa fa-fw fa-bar-chart', routerLink:["work"]},
            {label: '계정현황', icon: 'fa fa-fw fa-calendar', routerLink:["account"]},
            {label: '정책설정', icon: 'fa fa-fw fa-book', routerLink:["policy"]},
            {label: '계정감사', icon: 'fa fa-fw fa-support', routerLink:["audit"]},
            {   label: '환경설정', 
                icon: 'pi pi-fw pi-cog', 
                items:[
                    { label: '서버설정',routerLink:["environment"] },
                    { label: 'XX설정',routerLink:["environment"] },
                    { label: '에이전트',routerLink:["env_agent"] },
                    { label: '관리자',routerLink:["env_agent"] },
                ]

            }
        ];
    }
}
