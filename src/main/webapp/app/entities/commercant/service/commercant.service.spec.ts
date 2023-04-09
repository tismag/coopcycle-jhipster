import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICommercant } from '../commercant.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../commercant.test-samples';

import { CommercantService } from './commercant.service';

const requireRestSample: ICommercant = {
  ...sampleWithRequiredData,
};

describe('Commercant Service', () => {
  let service: CommercantService;
  let httpMock: HttpTestingController;
  let expectedResult: ICommercant | ICommercant[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CommercantService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Commercant', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const commercant = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(commercant).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Commercant', () => {
      const commercant = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(commercant).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Commercant', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Commercant', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Commercant', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCommercantToCollectionIfMissing', () => {
      it('should add a Commercant to an empty array', () => {
        const commercant: ICommercant = sampleWithRequiredData;
        expectedResult = service.addCommercantToCollectionIfMissing([], commercant);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commercant);
      });

      it('should not add a Commercant to an array that contains it', () => {
        const commercant: ICommercant = sampleWithRequiredData;
        const commercantCollection: ICommercant[] = [
          {
            ...commercant,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCommercantToCollectionIfMissing(commercantCollection, commercant);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Commercant to an array that doesn't contain it", () => {
        const commercant: ICommercant = sampleWithRequiredData;
        const commercantCollection: ICommercant[] = [sampleWithPartialData];
        expectedResult = service.addCommercantToCollectionIfMissing(commercantCollection, commercant);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commercant);
      });

      it('should add only unique Commercant to an array', () => {
        const commercantArray: ICommercant[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const commercantCollection: ICommercant[] = [sampleWithRequiredData];
        expectedResult = service.addCommercantToCollectionIfMissing(commercantCollection, ...commercantArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const commercant: ICommercant = sampleWithRequiredData;
        const commercant2: ICommercant = sampleWithPartialData;
        expectedResult = service.addCommercantToCollectionIfMissing([], commercant, commercant2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commercant);
        expect(expectedResult).toContain(commercant2);
      });

      it('should accept null and undefined values', () => {
        const commercant: ICommercant = sampleWithRequiredData;
        expectedResult = service.addCommercantToCollectionIfMissing([], null, commercant, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commercant);
      });

      it('should return initial array if no Commercant is added', () => {
        const commercantCollection: ICommercant[] = [sampleWithRequiredData];
        expectedResult = service.addCommercantToCollectionIfMissing(commercantCollection, undefined, null);
        expect(expectedResult).toEqual(commercantCollection);
      });
    });

    describe('compareCommercant', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCommercant(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCommercant(entity1, entity2);
        const compareResult2 = service.compareCommercant(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCommercant(entity1, entity2);
        const compareResult2 = service.compareCommercant(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCommercant(entity1, entity2);
        const compareResult2 = service.compareCommercant(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
