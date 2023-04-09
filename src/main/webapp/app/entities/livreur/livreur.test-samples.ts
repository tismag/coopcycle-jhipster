import { ILivreur, NewLivreur } from './livreur.model';

export const sampleWithRequiredData: ILivreur = {
  id: 71552,
};

export const sampleWithPartialData: ILivreur = {
  id: 37626,
  prenom: 'Consultant',
  email: 'Christophe_Dupuy@hotmail.fr',
};

export const sampleWithFullData: ILivreur = {
  id: 99769,
  idLivreur: 34747,
  nom: 'Buckinghamshire Tools',
  prenom: 'orchid',
  email: 'Bon_Leroy@yahoo.fr',
  numeroTel: 'Poitou-Charentes',
  cooperative: 'Frozen',
};

export const sampleWithNewData: NewLivreur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
