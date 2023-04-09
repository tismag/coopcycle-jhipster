import { ICommercant } from 'app/entities/commercant/commercant.model';

export interface ICooperative {
  id: number;
  idCooperative?: number | null;
  nom?: string | null;
  email?: string | null;
  numeroTel?: string | null;
  adresse?: string | null;
  idCooperatives?: Pick<ICommercant, 'id'>[] | null;
}

export type NewCooperative = Omit<ICooperative, 'id'> & { id: null };
