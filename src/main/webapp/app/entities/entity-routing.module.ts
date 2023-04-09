import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'livreur',
        data: { pageTitle: 'coopcycleApp.livreur.home.title' },
        loadChildren: () => import('./livreur/livreur.module').then(m => m.LivreurModule),
      },
      {
        path: 'commercant',
        data: { pageTitle: 'coopcycleApp.commercant.home.title' },
        loadChildren: () => import('./commercant/commercant.module').then(m => m.CommercantModule),
      },
      {
        path: 'cooperative',
        data: { pageTitle: 'coopcycleApp.cooperative.home.title' },
        loadChildren: () => import('./cooperative/cooperative.module').then(m => m.CooperativeModule),
      },
      {
        path: 'livraison',
        data: { pageTitle: 'coopcycleApp.livraison.home.title' },
        loadChildren: () => import('./livraison/livraison.module').then(m => m.LivraisonModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
