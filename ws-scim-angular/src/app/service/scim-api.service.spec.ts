/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ScimApiService } from './scim-api.service';

describe('Service: ScimApi', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ScimApiService]
    });
  });

  it('should ...', inject([ScimApiService], (service: ScimApiService) => {
    expect(service).toBeTruthy();
  }));
});
