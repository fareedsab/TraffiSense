import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MyServiceService } from '../my-service.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent implements OnInit {
  // username!: string;
  password!: string;
  myForm !: FormGroup

  username = localStorage.getItem('firstName');
  constructor(private fb: FormBuilder, private service: MyServiceService, private router: Router,) {

  }


  ngOnInit() {
    this.initializeForm();
    this.service.logine(false)
  }




  login(username: any, password: any) {
    this.service.loginApi(username, password).subscribe((res: any) => {

      if (res.httpStatusCode == 200) {
        Swal.fire('Logged In Successfully !', 'Success')
        this.service.logine(true)
        localStorage.setItem('userName', res.data[0].userName)
        localStorage.setItem('firstName', res.data[0].firstName)
        localStorage.setItem('lastName', res.data[0].lastName)
        localStorage.setItem('verified', res.data[0].verified)
        if (res.data[0].verified === "Approved") {
          this.router.navigate(['/users']);
          this.myForm.reset();
          // location.reload();
        }
      }
      else {
        Swal.fire('Login Failed !', 'Error')
      }
    }
    );


  }

  initializeForm() {
    this.myForm = this.fb.group({
      username: [''],
      password: [''],
    })
  }



}
