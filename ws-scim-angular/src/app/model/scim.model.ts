export class SCIMFindRequest{
    attributes : string[];
    where : string;
    order : string;
    startIndex : number;
    count : number;
}
export class MapperRequest{
    name : string;
    type : string;
    mapper:any;
}

export class FrontRequest{
    method:string;
    params:{};
}