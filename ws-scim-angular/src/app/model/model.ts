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
    schedulerType:string;
    schedulerDesc:string;
    jobClass:string;
    dayOfMonth : number;
	dayOfWeek : number;
	hourOfDay : number;
	minuteOfHour : number;
    sourceSystemId:string;
    tragetSystemId:string;
    lastExecuteDate:string;
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

export class SystemColumn {
    systemId:string;
    columnName:string;
    displayName:string;
    
    dataType:string;
    dataSize:number;
    allowNull:boolean;
	
	defaultValue:string;
    comment:string;
    mappingColumn:string;
}