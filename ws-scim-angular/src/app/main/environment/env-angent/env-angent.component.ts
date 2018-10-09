import { Component ,OnInit  } from '@angular/core';
import {TreeNode} from 'primeng/api';

import {SCIMService} from '../../../service/service.component';


@Component({
    selector: 'env-angent',
    templateUrl: 'env-angent.component.html',
    styleUrls: ['env-angent.component.scss']
})
export class EnvAngentComponent implements OnInit {
    agents: TreeNode[];
    constructor(
        private scim_service: SCIMService
    ) {}

    ngOnInit() {
        this.getAgentList();
        this.agents = [
            {label: 'View', icon: 'fa fa-search'},
            {label: 'Unselect', icon: 'fa fa-close'}
        ];
    }

    getAgentList(){
        console.log("get agent list")

        this.scim_service.get('/env/agent/all').subscribe(
        result =>{
            console.log("result", result);
        },
        error => {
            console.log("Error", error);
        });
    }
}
