/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { EnvHrAgentComponent } from './env-hr-agent.component';

describe('EnvHrAgentComponent', () => {
  let component: EnvHrAgentComponent;
  let fixture: ComponentFixture<EnvHrAgentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EnvHrAgentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EnvHrAgentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
