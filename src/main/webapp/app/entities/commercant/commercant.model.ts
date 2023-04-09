import { ICooperative } from 'app/entities/cooperative/cooperative.model';

export interface ICommercant {
  id: number;
  idCommercant?: number | null;
  nom?: string | null;
  prenom?: string | null;
  email?: string | null;
  numeroTel?: string | null;
  nomEtablissement?: string | null;
  adresse?: string | null;
  idCommercants?: Pick<ICooperative, 'id'>[] | null;
}

export type NewCommercant = Omit<ICommercant, 'id'> & { id: null };
