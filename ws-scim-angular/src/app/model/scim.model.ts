export class SCIMFindRequest{
    attributes : string[];
    where : string;
    order : string;
    startIndex : number;
    count : number;
}
export class FrontRequest{
    method:string;
    params:{};
}