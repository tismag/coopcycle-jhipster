import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICommercant, NewCommercant } from '../commercant.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICommercant for edit and NewCommercantFormGroupInput for create.
 */
type CommercantFormGroupInput = ICommercant | PartialWithRequiredKeyOf<NewCommercant>;

type CommercantFormDefaults = Pick<NewCommercant, 'id' | 'idCommercants'>;

type CommercantFormGroupContent = {
  id: FormControl<ICommercant['id'] | NewCommercant['id']>;
  idCommercant: FormControl<ICommercant['idCommercant']>;
  nom: FormControl<ICommercant['nom']>;
  prenom: FormControl<ICommercant['prenom']>;
  email: FormControl<ICommercant['email']>;
  numeroTel: FormControl<ICommercant['numeroTel']>;
  nomEtablissement: FormControl<ICommercant['nomEtablissement']>;
  adresse: FormControl<ICommercant['adresse']>;
  idCommercants: FormControl<ICommercant['idCommercants']>;
};

export type CommercantFormGroup = FormGroup<CommercantFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CommercantFormService {
  createCommercantFormGroup(commercant: CommercantFormGroupInput = { id: null }): CommercantFormGroup {
    const commercantRawValue = {
      ...this.getFormDefaults(),
      ...commercant,
    };
    return new FormGroup<CommercantFormGroupContent>({
      id: new FormControl(
        { value: commercantRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      idCommercant: new FormControl(commercantRawValue.idCommercant),
      nom: new FormControl(commercantRawValue.nom),
      prenom: new FormControl(commercantRawValue.prenom),
      email: new FormControl(commercantRawValue.email),
      numeroTel: new FormControl(commercantRawValue.numeroTel),
      nomEtablissement: new FormControl(commercantRawValue.nomEtablissement),
      adresse: new FormControl(commercantRawValue.adresse),
      idCommercants: new FormControl(commercantRawValue.idCommercants ?? []),
    });
  }

  getCommercant(form: CommercantFormGroup): ICommercant | NewCommercant {
    return form.getRawValue() as ICommercant | NewCommercant;
  }

  resetForm(form: CommercantFormGroup, commercant: CommercantFormGroupInput): void {
    const commercantRawValue = { ...this.getFormDefaults(), ...commercant };
    form.reset(
      {
        ...commercantRawValue,
        id: { value: commercantRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CommercantFormDefaults {
    return {
      id: null,
      idCommercants: [],
    };
  }
}
