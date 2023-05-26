import { Component, OnInit } from '@angular/core';
import { MyServiceService } from './my-service.service';
import { Observable } from 'rxjs/internal/Observable';
import { Spinkit } from 'ng-http-loader';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit  {
  title = 'Blog';
  sidebarExpanded = true;
  public spinkit = Spinkit;
  username = localStorage.getItem('firstName');
  isLoggedIn$!: Observable<boolean>;
  
  constructor (private authservice :MyServiceService){
   
  }
  

  ngOnInit() {
   
    this.isLoggedIn$ = this.authservice.isLoggedIn;
  }
  

}
