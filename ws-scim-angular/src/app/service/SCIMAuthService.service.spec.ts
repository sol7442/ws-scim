/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { SCIMAuthServiceService } from './SCIMAuthService.service';

describe('Service: SCIMAuthService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SCIMAuthServiceService]
    });
  });

  it('should ...', inject([SCIMAuthServiceService], (service: SCIMAuthServiceService) => {
    expect(service).toBeTruthy();
  }));
});
