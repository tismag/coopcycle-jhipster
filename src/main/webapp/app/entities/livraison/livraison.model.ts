import { ILivreur } from 'app/entities/livreur/livreur.model';

export interface ILivraison {
  id: number;
  idLivraison?: number | null;
  contenu?: string | null;
  quantite?: number | null;
  livreur?: Pick<ILivreur, 'id'> | null;
}

export type NewLivraison = Omit<ILivraison, 'id'> & { id: null };
