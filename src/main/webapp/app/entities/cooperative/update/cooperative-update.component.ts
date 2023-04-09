import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CooperativeFormService, CooperativeFormGroup } from './cooperative-form.service';
import { ICooperative } from '../cooperative.model';
import { CooperativeService } from '../service/cooperative.service';

@Component({
  selector: 'jhi-cooperative-update',
  templateUrl: './cooperative-update.component.html',
})
export class CooperativeUpdateComponent implements OnInit {
  isSaving = false;
  cooperative: ICooperative | null = null;

  editForm: CooperativeFormGroup = this.cooperativeFormService.createCooperativeFormGroup();

  constructor(
    protected cooperativeService: CooperativeService,
    protected cooperativeFormService: CooperativeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cooperative }) => {
      this.cooperative = cooperative;
      if (cooperative) {
        this.updateForm(cooperative);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cooperative = this.cooperativeFormService.getCooperative(this.editForm);
    if (cooperative.id !== null) {
      this.subscribeToSaveResponse(this.cooperativeService.update(cooperative));
    } else {
      this.subscribeToSaveResponse(this.cooperativeService.create(cooperative));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICooperative>>): void {
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

  protected updateForm(cooperative: ICooperative): void {
    this.cooperative = cooperative;
    this.cooperativeFormService.resetForm(this.editForm, cooperative);
  }
}
