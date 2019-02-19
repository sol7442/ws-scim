import { Timestamp } from "rxjs/Rx";

export class User {
    id: string;
    passwd: string;
    name:string;
    type:string;
    token:string;
}

export class System {
    systemId: string;
    systemName:string;
    systemDesc:string;
    systemUrl:string;
}

export class Scheduler {
    schedulerId: string;
    schedulerName:string;
    schedulerDesc:string;
    schedulerType:string;

    sourceSystemId:string;
    tragetSystemId:string;
}

export class SchedulerHistory {
    schedulerId: string;
    workId:string;

    reqPut:number;
    reqPost:number;
    reqPatch:number;
    reqDelete:number;
    
    resPut:number;
    resPost:number;
    resPatch:number;
    resDelete:number;

    workDate:string;
}