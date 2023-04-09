import { ILivraison, NewLivraison } from './livraison.model';

export const sampleWithRequiredData: ILivraison = {
  id: 49857,
};

export const sampleWithPartialData: ILivraison = {
  id: 70942,
  idLivraison: 72460,
  contenu: 'strategy',
  quantite: 49675,
};

export const sampleWithFullData: ILivraison = {
  id: 99305,
  idLivraison: 95835,
  contenu: 'Awesome',
  quantite: 15972,
};

export const sampleWithNewData: NewLivraison = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
