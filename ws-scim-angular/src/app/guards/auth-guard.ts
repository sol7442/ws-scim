import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable()
export class AuthGuard implements CanActivate {
    constructor(private router: Router) { }
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        console.log("AuthGuard : ",sessionStorage.getItem('currentUser'))
        if (sessionStorage.getItem('currentUser')) {
            return true;
        }
        console.log("AuthGuard : goto login page");
        this.router.navigate(['/login']);
        return false;
    }

}
