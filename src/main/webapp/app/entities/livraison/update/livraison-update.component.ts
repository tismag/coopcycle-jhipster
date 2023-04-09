import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { LivraisonFormService, LivraisonFormGroup } from './livraison-form.service';
import { ILivraison } from '../livraison.model';
import { LivraisonService } from '../service/livraison.service';
import { ILivreur } from 'app/entities/livreur/livreur.model';
import { LivreurService } from 'app/entities/livreur/service/livreur.service';

@Component({
  selector: 'jhi-livraison-update',
  templateUrl: './livraison-update.component.html',
})
export class LivraisonUpdateComponent implements OnInit {
  isSaving = false;
  livraison: ILivraison | null = null;

  livreursSharedCollection: ILivreur[] = [];

  editForm: LivraisonFormGroup = this.livraisonFormService.createLivraisonFormGroup();

  constructor(
    protected livraisonService: LivraisonService,
    protected livraisonFormService: LivraisonFormService,
    protected livreurService: LivreurService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareLivreur = (o1: ILivreur | null, o2: ILivreur | null): boolean => this.livreurService.compareLivreur(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ livraison }) => {
      this.livraison = livraison;
      if (livraison) {
        this.updateForm(livraison);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const livraison = this.livraisonFormService.getLivraison(this.editForm);
    if (livraison.id !== null) {
      this.subscribeToSaveResponse(this.livraisonService.update(livraison));
    } else {
      this.subscribeToSaveResponse(this.livraisonService.create(livraison));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILivraison>>): void {
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

  protected updateForm(livraison: ILivraison): void {
    this.livraison = livraison;
    this.livraisonFormService.resetForm(this.editForm, livraison);

    this.livreursSharedCollection = this.livreurService.addLivreurToCollectionIfMissing<ILivreur>(
      this.livreursSharedCollection,
      livraison.livreur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.livreurService
      .query()
      .pipe(map((res: HttpResponse<ILivreur[]>) => res.body ?? []))
      .pipe(map((livreurs: ILivreur[]) => this.livreurService.addLivreurToCollectionIfMissing<ILivreur>(livreurs, this.livraison?.livreur)))
      .subscribe((livreurs: ILivreur[]) => (this.livreursSharedCollection = livreurs));
  }
}
