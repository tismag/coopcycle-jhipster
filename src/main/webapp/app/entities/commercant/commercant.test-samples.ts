import { ICommercant, NewCommercant } from './commercant.model';

export const sampleWithRequiredData: ICommercant = {
  id: 83777,
};

export const sampleWithPartialData: ICommercant = {
  id: 57884,
  nom: 'Refined seize',
  prenom: 'Table enhance XML',
  numeroTel: 'drive solid',
};

export const sampleWithFullData: ICommercant = {
  id: 62495,
  idCommercant: 7466,
  nom: 'Armenian invoice next-generation',
  prenom: 'Handmade Baby',
  email: 'Fulcran42@hotmail.fr',
  numeroTel: 'systems',
  nomEtablissement: 'maximize Grass-roots',
  adresse: 'Granite Checking Corse',
};

export const sampleWithNewData: NewCommercant = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
