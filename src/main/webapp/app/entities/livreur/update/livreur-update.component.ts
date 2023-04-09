import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { LivreurFormService, LivreurFormGroup } from './livreur-form.service';
import { ILivreur } from '../livreur.model';
import { LivreurService } from '../service/livreur.service';
import { ICooperative } from 'app/entities/cooperative/cooperative.model';
import { CooperativeService } from 'app/entities/cooperative/service/cooperative.service';

@Component({
  selector: 'jhi-livreur-update',
  templateUrl: './livreur-update.component.html',
})
export class LivreurUpdateComponent implements OnInit {
  isSaving = false;
  livreur: ILivreur | null = null;

  cooperativesSharedCollection: ICooperative[] = [];

  editForm: LivreurFormGroup = this.livreurFormService.createLivreurFormGroup();

  constructor(
    protected livreurService: LivreurService,
    protected livreurFormService: LivreurFormService,
    protected cooperativeService: CooperativeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCooperative = (o1: ICooperative | null, o2: ICooperative | null): boolean => this.cooperativeService.compareCooperative(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ livreur }) => {
      this.livreur = livreur;
      if (livreur) {
        this.updateForm(livreur);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const livreur = this.livreurFormService.getLivreur(this.editForm);
    if (livreur.id !== null) {
      this.subscribeToSaveResponse(this.livreurService.update(livreur));
    } else {
      this.subscribeToSaveResponse(this.livreurService.create(livreur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILivreur>>): void {
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

  protected updateForm(livreur: ILivreur): void {
    this.livreur = livreur;
    this.livreurFormService.resetForm(this.editForm, livreur);

    this.cooperativesSharedCollection = this.cooperativeService.addCooperativeToCollectionIfMissing<ICooperative>(
      this.cooperativesSharedCollection,
      livreur.cooperative
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cooperativeService
      .query()
      .pipe(map((res: HttpResponse<ICooperative[]>) => res.body ?? []))
      .pipe(
        map((cooperatives: ICooperative[]) =>
          this.cooperativeService.addCooperativeToCollectionIfMissing<ICooperative>(cooperatives, this.livreur?.cooperative)
        )
      )
      .subscribe((cooperatives: ICooperative[]) => (this.cooperativesSharedCollection = cooperatives));
  }
}
