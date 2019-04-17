/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { EnvSysAgentComponent } from './env-sys-agent.component';

describe('EnvSysAgentComponent', () => {
  let component: EnvSysAgentComponent;
  let fixture: ComponentFixture<EnvSysAgentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EnvSysAgentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EnvSysAgentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
