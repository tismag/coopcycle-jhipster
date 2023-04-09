import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILivraison, NewLivraison } from '../livraison.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILivraison for edit and NewLivraisonFormGroupInput for create.
 */
type LivraisonFormGroupInput = ILivraison | PartialWithRequiredKeyOf<NewLivraison>;

type LivraisonFormDefaults = Pick<NewLivraison, 'id'>;

type LivraisonFormGroupContent = {
  id: FormControl<ILivraison['id'] | NewLivraison['id']>;
  idLivraison: FormControl<ILivraison['idLivraison']>;
  contenu: FormControl<ILivraison['contenu']>;
  quantite: FormControl<ILivraison['quantite']>;
  livreur: FormControl<ILivraison['livreur']>;
};

export type LivraisonFormGroup = FormGroup<LivraisonFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LivraisonFormService {
  createLivraisonFormGroup(livraison: LivraisonFormGroupInput = { id: null }): LivraisonFormGroup {
    const livraisonRawValue = {
      ...this.getFormDefaults(),
      ...livraison,
    };
    return new FormGroup<LivraisonFormGroupContent>({
      id: new FormControl(
        { value: livraisonRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      idLivraison: new FormControl(livraisonRawValue.idLivraison),
      contenu: new FormControl(livraisonRawValue.contenu),
      quantite: new FormControl(livraisonRawValue.quantite),
      livreur: new FormControl(livraisonRawValue.livreur),
    });
  }

  getLivraison(form: LivraisonFormGroup): ILivraison | NewLivraison {
    return form.getRawValue() as ILivraison | NewLivraison;
  }

  resetForm(form: LivraisonFormGroup, livraison: LivraisonFormGroupInput): void {
    const livraisonRawValue = { ...this.getFormDefaults(), ...livraison };
    form.reset(
      {
        ...livraisonRawValue,
        id: { value: livraisonRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LivraisonFormDefaults {
    return {
      id: null,
    };
  }
}
