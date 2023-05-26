import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UsersComponent } from './users/users.component';
import { LoginComponent } from './login/login.component';
import { YoloModelComponent } from './yolo-model/yolo-model.component';

const routes: Routes = [
  {
    path : "",
    component: LoginComponent,
  },
  {
    path : "users",
    component: UsersComponent,
  },
  {
    path: "model",
    component : YoloModelComponent,
  }  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { 

  

}
