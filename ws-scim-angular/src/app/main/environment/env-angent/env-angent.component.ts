import { Component ,OnInit  } from '@angular/core';
import {TreeNode} from 'primeng/api';



@Component({
    selector: 'env-angent',
    templateUrl: 'env-angent.component.html',
    styleUrls: ['env-angent.component.scss']
})
export class EnvAngentComponent implements OnInit {
    agents: TreeNode[];
    constructor( ) {
        
    }

    ngOnInit() {
       
    }

    getAgentList(){
        
    }
}
