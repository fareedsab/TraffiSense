import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MyServiceService } from '../my-service.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent {

  myForm !: FormGroup
  modalTitle = 'Add User Details';
  constructor(private fb: FormBuilder,  private service: MyServiceService, private router: Router) {

  }
  display = "none";

  ngOnInit() {
    if (this.service.isLoggedIn) {
      this.initializeForm();
      this.getAllUser();
      this.service.logine(true)
    }
    else {
      this.router.navigate([''])
    }


  }

  initializeForm() {
    this.myForm = this.fb.group({
      username: [''],
      id: [''],
      password: [''],
      firstName: [''],
      lastName: [''],
      verified: [''],
    })
  }

  userList = [];
  getAllUser() {
    this.service.getAllUser().subscribe((res: any) => {
      if (res['httpStatusCode'] == 200) {
        this.userList = res['data']
        console.log(this.userList)
      }
    })
  }
  submitDetails(userobj: any) {
    debugger
    if (this.myForm.valid) {
      if (userobj.verified === true) {
        userobj.verified = 'Approved'
        this.service.registerUser(JSON.stringify(userobj)).subscribe((res: any) => {
          if (res['httpStatusCode'] == 200) {
            Swal.fire('User Registeted!', 'Success');
            this.myForm.reset();
            this.getAllUser();
            this.onCloseHandled();
          }
        })
      } else {
        userobj.verified = 'notApproved'
        this.service.registerUser(JSON.stringify(userobj)).subscribe((res: any) => {
          if (res['httpStatusCode'] == 200) {
            Swal.fire('User Registeted!', 'Success');
            this.myForm.reset();
            this.getAllUser();
            this.onCloseHandled();
          }
        });
      }
    } else {
      Swal.fire('User details empty!', 'Error');


    }
  }
  updatDetails(userobj: any) {
    debugger
    if (this.myForm.valid) {
      if (userobj.verified === true) {
        userobj.verified = 'Approved'
        this.service.updateUser(JSON.stringify(userobj)).subscribe((res: any) => {
          if (res['httpStatusCode'] == 200) {
            Swal.fire('User Updated!', 'Success');

            this.myForm.reset();
            this.getAllUser();
            this.onCloseHandled();
          }
        })
      } else {
        userobj.verified = 'notApproved'
        this.service.updateUser(JSON.stringify(userobj)).subscribe((res: any) => {
          if (res['httpStatusCode'] == 200) {
            Swal.fire('User Updated!', 'Success');

            this.myForm.reset();
            this.getAllUser();
            this.onCloseHandled();
          }
        });
      }
    } else {
      Swal.fire('User details can not be empty!', 'Error');
    }
  }


  deleteUser(id: any) {
    this.service.deleteUser(id).subscribe((res: any) => {
      if (res['httpStatusCode'] == 200) {
        // this.userList = res['data']
        console.log(this.userList)
        this.getAllUser();
        Swal.fire('User Deleted !', 'Success');


      }
    })
  }




  openModal() {
    this.display = "block";
  }
  editModal(obj: any) {
    this.modalTitle = "Update User Details"
    this.display = "block";
    if (obj) {
      this.myForm.controls['id'].setValue(obj.id)
      this.myForm.controls['firstName'].setValue(obj.firstName)
      this.myForm.controls['lastName'].setValue(obj.lastName)
      this.myForm.controls['username'].setValue(obj.username)
      this.myForm.controls['password'].setValue(obj.password)
      this.myForm.controls['verified'].setValue(obj.verified === 'Approved' ? true : false)
    }
  }
  onCloseHandled() {
    this.display = "none";
    this.myForm.reset();
  }
}
