import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CommercantFormService, CommercantFormGroup } from './commercant-form.service';
import { ICommercant } from '../commercant.model';
import { CommercantService } from '../service/commercant.service';
import { ICooperative } from 'app/entities/cooperative/cooperative.model';
import { CooperativeService } from 'app/entities/cooperative/service/cooperative.service';

@Component({
  selector: 'jhi-commercant-update',
  templateUrl: './commercant-update.component.html',
})
export class CommercantUpdateComponent implements OnInit {
  isSaving = false;
  commercant: ICommercant | null = null;

  cooperativesSharedCollection: ICooperative[] = [];

  editForm: CommercantFormGroup = this.commercantFormService.createCommercantFormGroup();

  constructor(
    protected commercantService: CommercantService,
    protected commercantFormService: CommercantFormService,
    protected cooperativeService: CooperativeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCooperative = (o1: ICooperative | null, o2: ICooperative | null): boolean => this.cooperativeService.compareCooperative(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commercant }) => {
      this.commercant = commercant;
      if (commercant) {
        this.updateForm(commercant);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commercant = this.commercantFormService.getCommercant(this.editForm);
    if (commercant.id !== null) {
      this.subscribeToSaveResponse(this.commercantService.update(commercant));
    } else {
      this.subscribeToSaveResponse(this.commercantService.create(commercant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommercant>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(commercant: ICommercant): void {
    this.commercant = commercant;
    this.commercantFormService.resetForm(this.editForm, commercant);

    this.cooperativesSharedCollection = this.cooperativeService.addCooperativeToCollectionIfMissing<ICooperative>(
      this.cooperativesSharedCollection,
      ...(commercant.idCommercants ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cooperativeService
      .query()
      .pipe(map((res: HttpResponse<ICooperative[]>) => res.body ?? []))
      .pipe(
        map((cooperatives: ICooperative[]) =>
          this.cooperativeService.addCooperativeToCollectionIfMissing<ICooperative>(cooperatives, ...(this.commercant?.idCommercants ?? []))
        )
      )
      .subscribe((cooperatives: ICooperative[]) => (this.cooperativesSharedCollection = cooperatives));
  }
}
