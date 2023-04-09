import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CommercantFormService } from './commercant-form.service';
import { CommercantService } from '../service/commercant.service';
import { ICommercant } from '../commercant.model';
import { ICooperative } from 'app/entities/cooperative/cooperative.model';
import { CooperativeService } from 'app/entities/cooperative/service/cooperative.service';

import { CommercantUpdateComponent } from './commercant-update.component';

describe('Commercant Management Update Component', () => {
  let comp: CommercantUpdateComponent;
  let fixture: ComponentFixture<CommercantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commercantFormService: CommercantFormService;
  let commercantService: CommercantService;
  let cooperativeService: CooperativeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CommercantUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CommercantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommercantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commercantFormService = TestBed.inject(CommercantFormService);
    commercantService = TestBed.inject(CommercantService);
    cooperativeService = TestBed.inject(CooperativeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cooperative query and add missing value', () => {
      const commercant: ICommercant = { id: 456 };
      const idCommercants: ICooperative[] = [{ id: 23574 }];
      commercant.idCommercants = idCommercants;

      const cooperativeCollection: ICooperative[] = [{ id: 88900 }];
      jest.spyOn(cooperativeService, 'query').mockReturnValue(of(new HttpResponse({ body: cooperativeCollection })));
      const additionalCooperatives = [...idCommercants];
      const expectedCollection: ICooperative[] = [...additionalCooperatives, ...cooperativeCollection];
      jest.spyOn(cooperativeService, 'addCooperativeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commercant });
      comp.ngOnInit();

      expect(cooperativeService.query).toHaveBeenCalled();
      expect(cooperativeService.addCooperativeToCollectionIfMissing).toHaveBeenCalledWith(
        cooperativeCollection,
        ...additionalCooperatives.map(expect.objectContaining)
      );
      expect(comp.cooperativesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const commercant: ICommercant = { id: 456 };
      const idCommercant: ICooperative = { id: 62864 };
      commercant.idCommercants = [idCommercant];

      activatedRoute.data = of({ commercant });
      comp.ngOnInit();

      expect(comp.cooperativesSharedCollection).toContain(idCommercant);
      expect(comp.commercant).toEqual(commercant);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommercant>>();
      const commercant = { id: 123 };
      jest.spyOn(commercantFormService, 'getCommercant').mockReturnValue(commercant);
      jest.spyOn(commercantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commercant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commercant }));
      saveSubject.complete();

      // THEN
      expect(commercantFormService.getCommercant).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(commercantService.update).toHaveBeenCalledWith(expect.objectContaining(commercant));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommercant>>();
      const commercant = { id: 123 };
      jest.spyOn(commercantFormService, 'getCommercant').mockReturnValue({ id: null });
      jest.spyOn(commercantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commercant: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commercant }));
      saveSubject.complete();

      // THEN
      expect(commercantFormService.getCommercant).toHaveBeenCalled();
      expect(commercantService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommercant>>();
      const commercant = { id: 123 };
      jest.spyOn(commercantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commercant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commercantService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCooperative', () => {
      it('Should forward to cooperativeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cooperativeService, 'compareCooperative');
        comp.compareCooperative(entity, entity2);
        expect(cooperativeService.compareCooperative).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
