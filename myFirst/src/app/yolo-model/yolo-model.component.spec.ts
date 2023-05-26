import { ComponentFixture, TestBed } from '@angular/core/testing';

import { YoloModelComponent } from './yolo-model.component';

describe('YoloModelComponent', () => {
  let component: YoloModelComponent;
  let fixture: ComponentFixture<YoloModelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ YoloModelComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(YoloModelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
