import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICooperative, NewCooperative } from '../cooperative.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICooperative for edit and NewCooperativeFormGroupInput for create.
 */
type CooperativeFormGroupInput = ICooperative | PartialWithRequiredKeyOf<NewCooperative>;

type CooperativeFormDefaults = Pick<NewCooperative, 'id' | 'idCooperatives'>;

type CooperativeFormGroupContent = {
  id: FormControl<ICooperative['id'] | NewCooperative['id']>;
  idCooperative: FormControl<ICooperative['idCooperative']>;
  nom: FormControl<ICooperative['nom']>;
  email: FormControl<ICooperative['email']>;
  numeroTel: FormControl<ICooperative['numeroTel']>;
  adresse: FormControl<ICooperative['adresse']>;
  idCooperatives: FormControl<ICooperative['idCooperatives']>;
};

export type CooperativeFormGroup = FormGroup<CooperativeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CooperativeFormService {
  createCooperativeFormGroup(cooperative: CooperativeFormGroupInput = { id: null }): CooperativeFormGroup {
    const cooperativeRawValue = {
      ...this.getFormDefaults(),
      ...cooperative,
    };
    return new FormGroup<CooperativeFormGroupContent>({
      id: new FormControl(
        { value: cooperativeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      idCooperative: new FormControl(cooperativeRawValue.idCooperative),
      nom: new FormControl(cooperativeRawValue.nom),
      email: new FormControl(cooperativeRawValue.email),
      numeroTel: new FormControl(cooperativeRawValue.numeroTel),
      adresse: new FormControl(cooperativeRawValue.adresse),
      idCooperatives: new FormControl(cooperativeRawValue.idCooperatives ?? []),
    });
  }

  getCooperative(form: CooperativeFormGroup): ICooperative | NewCooperative {
    return form.getRawValue() as ICooperative | NewCooperative;
  }

  resetForm(form: CooperativeFormGroup, cooperative: CooperativeFormGroupInput): void {
    const cooperativeRawValue = { ...this.getFormDefaults(), ...cooperative };
    form.reset(
      {
        ...cooperativeRawValue,
        id: { value: cooperativeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CooperativeFormDefaults {
    return {
      id: null,
      idCooperatives: [],
    };
  }
}
