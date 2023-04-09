import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILivraison, NewLivraison } from '../livraison.model';

export type PartialUpdateLivraison = Partial<ILivraison> & Pick<ILivraison, 'id'>;

export type EntityResponseType = HttpResponse<ILivraison>;
export type EntityArrayResponseType = HttpResponse<ILivraison[]>;

@Injectable({ providedIn: 'root' })
export class LivraisonService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/livraisons');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(livraison: NewLivraison): Observable<EntityResponseType> {
    return this.http.post<ILivraison>(this.resourceUrl, livraison, { observe: 'response' });
  }

  update(livraison: ILivraison): Observable<EntityResponseType> {
    return this.http.put<ILivraison>(`${this.resourceUrl}/${this.getLivraisonIdentifier(livraison)}`, livraison, { observe: 'response' });
  }

  partialUpdate(livraison: PartialUpdateLivraison): Observable<EntityResponseType> {
    return this.http.patch<ILivraison>(`${this.resourceUrl}/${this.getLivraisonIdentifier(livraison)}`, livraison, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILivraison>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILivraison[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLivraisonIdentifier(livraison: Pick<ILivraison, 'id'>): number {
    return livraison.id;
  }

  compareLivraison(o1: Pick<ILivraison, 'id'> | null, o2: Pick<ILivraison, 'id'> | null): boolean {
    return o1 && o2 ? this.getLivraisonIdentifier(o1) === this.getLivraisonIdentifier(o2) : o1 === o2;
  }

  addLivraisonToCollectionIfMissing<Type extends Pick<ILivraison, 'id'>>(
    livraisonCollection: Type[],
    ...livraisonsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const livraisons: Type[] = livraisonsToCheck.filter(isPresent);
    if (livraisons.length > 0) {
      const livraisonCollectionIdentifiers = livraisonCollection.map(livraisonItem => this.getLivraisonIdentifier(livraisonItem)!);
      const livraisonsToAdd = livraisons.filter(livraisonItem => {
        const livraisonIdentifier = this.getLivraisonIdentifier(livraisonItem);
        if (livraisonCollectionIdentifiers.includes(livraisonIdentifier)) {
          return false;
        }
        livraisonCollectionIdentifiers.push(livraisonIdentifier);
        return true;
      });
      return [...livraisonsToAdd, ...livraisonCollection];
    }
    return livraisonCollection;
  }
}
