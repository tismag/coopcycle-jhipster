import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CooperativeService } from '../service/cooperative.service';

import { CooperativeComponent } from './cooperative.component';

describe('Cooperative Management Component', () => {
  let comp: CooperativeComponent;
  let fixture: ComponentFixture<CooperativeComponent>;
  let service: CooperativeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'cooperative', component: CooperativeComponent }]), HttpClientTestingModule],
      declarations: [CooperativeComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(CooperativeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CooperativeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CooperativeService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.cooperatives?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to cooperativeService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getCooperativeIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getCooperativeIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
