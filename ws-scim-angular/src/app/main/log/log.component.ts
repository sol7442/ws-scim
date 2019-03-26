import { Component, OnInit } from '@angular/core';

import { ScimApiService } from './../../service/scim-api.service';
import { AlertService } from './../../service/alert.service';

import { System } from '../../model/model';


@Component({
  selector: 'app-log',
  templateUrl: './log.component.html',
  styleUrls: ['./log.component.css']
})
export class LogComponent implements OnInit {

  private systems:System[];


  constructor(
    private scimApiService:ScimApiService,
    private alertService:AlertService,
  ) { }

  ngOnInit() {



  }

}
