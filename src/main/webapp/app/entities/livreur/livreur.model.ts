import { ICooperative } from 'app/entities/cooperative/cooperative.model';

export interface ILivreur {
  id: number;
  idLivreur?: number | null;
  nom?: string | null;
  prenom?: string | null;
  email?: string | null;
  numeroTel?: string | null;
  cooperative?: string | null;
  cooperative?: Pick<ICooperative, 'id'> | null;
}

export type NewLivreur = Omit<ILivreur, 'id'> & { id: null };
