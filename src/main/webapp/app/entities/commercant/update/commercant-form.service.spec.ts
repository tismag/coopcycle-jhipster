import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../commercant.test-samples';

import { CommercantFormService } from './commercant-form.service';

describe('Commercant Form Service', () => {
  let service: CommercantFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommercantFormService);
  });

  describe('Service methods', () => {
    describe('createCommercantFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCommercantFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idCommercant: expect.any(Object),
            nom: expect.any(Object),
            prenom: expect.any(Object),
            email: expect.any(Object),
            numeroTel: expect.any(Object),
            nomEtablissement: expect.any(Object),
            adresse: expect.any(Object),
            idCommercants: expect.any(Object),
          })
        );
      });

      it('passing ICommercant should create a new form with FormGroup', () => {
        const formGroup = service.createCommercantFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idCommercant: expect.any(Object),
            nom: expect.any(Object),
            prenom: expect.any(Object),
            email: expect.any(Object),
            numeroTel: expect.any(Object),
            nomEtablissement: expect.any(Object),
            adresse: expect.any(Object),
            idCommercants: expect.any(Object),
          })
        );
      });
    });

    describe('getCommercant', () => {
      it('should return NewCommercant for default Commercant initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCommercantFormGroup(sampleWithNewData);

        const commercant = service.getCommercant(formGroup) as any;

        expect(commercant).toMatchObject(sampleWithNewData);
      });

      it('should return NewCommercant for empty Commercant initial value', () => {
        const formGroup = service.createCommercantFormGroup();

        const commercant = service.getCommercant(formGroup) as any;

        expect(commercant).toMatchObject({});
      });

      it('should return ICommercant', () => {
        const formGroup = service.createCommercantFormGroup(sampleWithRequiredData);

        const commercant = service.getCommercant(formGroup) as any;

        expect(commercant).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICommercant should not enable id FormControl', () => {
        const formGroup = service.createCommercantFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCommercant should disable id FormControl', () => {
        const formGroup = service.createCommercantFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
