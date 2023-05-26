import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MyServiceService } from '../my-service.service';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  constructor(private router: Router,private authservice :MyServiceService) {

  }


  ngOnInit() {
    
  }
  logout(){
    
    localStorage.clear();
    
    this.router.navigate([''])
    // location.reload();

  }

}
