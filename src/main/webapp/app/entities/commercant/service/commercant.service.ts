import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommercant, NewCommercant } from '../commercant.model';

export type PartialUpdateCommercant = Partial<ICommercant> & Pick<ICommercant, 'id'>;

export type EntityResponseType = HttpResponse<ICommercant>;
export type EntityArrayResponseType = HttpResponse<ICommercant[]>;

@Injectable({ providedIn: 'root' })
export class CommercantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/commercants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(commercant: NewCommercant): Observable<EntityResponseType> {
    return this.http.post<ICommercant>(this.resourceUrl, commercant, { observe: 'response' });
  }

  update(commercant: ICommercant): Observable<EntityResponseType> {
    return this.http.put<ICommercant>(`${this.resourceUrl}/${this.getCommercantIdentifier(commercant)}`, commercant, {
      observe: 'response',
    });
  }

  partialUpdate(commercant: PartialUpdateCommercant): Observable<EntityResponseType> {
    return this.http.patch<ICommercant>(`${this.resourceUrl}/${this.getCommercantIdentifier(commercant)}`, commercant, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICommercant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommercant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCommercantIdentifier(commercant: Pick<ICommercant, 'id'>): number {
    return commercant.id;
  }

  compareCommercant(o1: Pick<ICommercant, 'id'> | null, o2: Pick<ICommercant, 'id'> | null): boolean {
    return o1 && o2 ? this.getCommercantIdentifier(o1) === this.getCommercantIdentifier(o2) : o1 === o2;
  }

  addCommercantToCollectionIfMissing<Type extends Pick<ICommercant, 'id'>>(
    commercantCollection: Type[],
    ...commercantsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const commercants: Type[] = commercantsToCheck.filter(isPresent);
    if (commercants.length > 0) {
      const commercantCollectionIdentifiers = commercantCollection.map(commercantItem => this.getCommercantIdentifier(commercantItem)!);
      const commercantsToAdd = commercants.filter(commercantItem => {
        const commercantIdentifier = this.getCommercantIdentifier(commercantItem);
        if (commercantCollectionIdentifiers.includes(commercantIdentifier)) {
          return false;
        }
        commercantCollectionIdentifiers.push(commercantIdentifier);
        return true;
      });
      return [...commercantsToAdd, ...commercantCollection];
    }
    return commercantCollection;
  }
}
