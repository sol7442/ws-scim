/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { EnvLogComponent } from './env-log.component';

describe('EnvLogComponent', () => {
  let component: EnvLogComponent;
  let fixture: ComponentFixture<EnvLogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EnvLogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EnvLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
