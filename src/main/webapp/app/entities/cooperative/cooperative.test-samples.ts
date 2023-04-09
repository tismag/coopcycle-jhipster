import { ICooperative, NewCooperative } from './cooperative.model';

export const sampleWithRequiredData: ICooperative = {
  id: 84816,
};

export const sampleWithPartialData: ICooperative = {
  id: 24269,
  idCooperative: 93196,
  numeroTel: 'Rupee primary',
  adresse: 'compressing program intuitive',
};

export const sampleWithFullData: ICooperative = {
  id: 50739,
  idCooperative: 15401,
  nom: 'Rubber olive Chicken',
  email: 'Jason_Remy@yahoo.fr',
  numeroTel: 'Implemented Grass-roots',
  adresse: 'Clothing',
};

export const sampleWithNewData: NewCooperative = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
