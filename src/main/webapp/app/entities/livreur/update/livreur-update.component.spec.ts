import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LivreurFormService } from './livreur-form.service';
import { LivreurService } from '../service/livreur.service';
import { ILivreur } from '../livreur.model';
import { ICooperative } from 'app/entities/cooperative/cooperative.model';
import { CooperativeService } from 'app/entities/cooperative/service/cooperative.service';

import { LivreurUpdateComponent } from './livreur-update.component';

describe('Livreur Management Update Component', () => {
  let comp: LivreurUpdateComponent;
  let fixture: ComponentFixture<LivreurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let livreurFormService: LivreurFormService;
  let livreurService: LivreurService;
  let cooperativeService: CooperativeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LivreurUpdateComponent],
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
      .overrideTemplate(LivreurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LivreurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    livreurFormService = TestBed.inject(LivreurFormService);
    livreurService = TestBed.inject(LivreurService);
    cooperativeService = TestBed.inject(CooperativeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cooperative query and add missing value', () => {
      const livreur: ILivreur = { id: 456 };
      const cooperative: ICooperative = { id: 23373 };
      livreur.cooperative = cooperative;

      const cooperativeCollection: ICooperative[] = [{ id: 84327 }];
      jest.spyOn(cooperativeService, 'query').mockReturnValue(of(new HttpResponse({ body: cooperativeCollection })));
      const additionalCooperatives = [cooperative];
      const expectedCollection: ICooperative[] = [...additionalCooperatives, ...cooperativeCollection];
      jest.spyOn(cooperativeService, 'addCooperativeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ livreur });
      comp.ngOnInit();

      expect(cooperativeService.query).toHaveBeenCalled();
      expect(cooperativeService.addCooperativeToCollectionIfMissing).toHaveBeenCalledWith(
        cooperativeCollection,
        ...additionalCooperatives.map(expect.objectContaining)
      );
      expect(comp.cooperativesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const livreur: ILivreur = { id: 456 };
      const cooperative: ICooperative = { id: 360 };
      livreur.cooperative = cooperative;

      activatedRoute.data = of({ livreur });
      comp.ngOnInit();

      expect(comp.cooperativesSharedCollection).toContain(cooperative);
      expect(comp.livreur).toEqual(livreur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILivreur>>();
      const livreur = { id: 123 };
      jest.spyOn(livreurFormService, 'getLivreur').mockReturnValue(livreur);
      jest.spyOn(livreurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livreur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: livreur }));
      saveSubject.complete();

      // THEN
      expect(livreurFormService.getLivreur).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(livreurService.update).toHaveBeenCalledWith(expect.objectContaining(livreur));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILivreur>>();
      const livreur = { id: 123 };
      jest.spyOn(livreurFormService, 'getLivreur').mockReturnValue({ id: null });
      jest.spyOn(livreurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livreur: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: livreur }));
      saveSubject.complete();

      // THEN
      expect(livreurFormService.getLivreur).toHaveBeenCalled();
      expect(livreurService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILivreur>>();
      const livreur = { id: 123 };
      jest.spyOn(livreurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livreur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(livreurService.update).toHaveBeenCalled();
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
