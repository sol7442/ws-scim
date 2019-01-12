/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { SCIMAuthService } from './SCIMAuth.service';

describe('Service: SCIMAuth', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SCIMAuthService]
    });
  });

  it('should ...', inject([SCIMAuthService], (service: SCIMAuthService) => {
    expect(service).toBeTruthy();
  }));
});
