import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MyServiceService {
  private loggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  get isLoggedIn() {
    return this.loggedIn.asObservable();
  }
  logine(getlog:boolean){
    this.loggedIn.next(getlog);

  }
  constructor(private http: HttpClient) { }
  baseurl: string = 'http://ahsankhaan47-001-site1.htempurl.com/api/'
  loginApi(username: any, password: any) {
    return this.http.get(this.baseurl + 'User/' + 'LoginUser' + '?userName=' + username + '&password=' + password);
  }
  getAllUser() {
    return this.http.get(this.baseurl + 'User/GetAllUser')
  }
  deleteUser(id: any) {
    return this.http.delete(this.baseurl + 'User/GetUserDelete' + '?userId=' + id)
  }
  getAllDetection(){
    return this.http.get(this.baseurl+'TrafficAnomlyDetection/GetAllDetections')
  }
  runYoloModel()
  {  const headerDict = {
    
    'Access-Control-Allow-Origin': 'http://localhost:4200',
  }
  let headers = new HttpHeaders(headerDict);
    
    return this.http.get('https://e05e-43-246-221-237.ngrok-free.app/execute_commands',{headers})
  }

  registerUser(obj: any) {
    const headerDict = {
      'Content-Type': 'application/json',
      'accept': 'text/plain'
      // 'Access-Control-Allow-Headers': 'Content-Type',
    }
    let headers = new HttpHeaders(headerDict);
    return this.http.post(this.baseurl + 'User/' + 'GetUserRegister', obj, { headers });
  }


  updateUser(obj: any) {
    const headerDict = {
      'Content-Type': 'application/json',
      'accept': 'text/plain'
      // 'Access-Control-Allow-Headers': 'Content-Type',
    }
    let headers = new HttpHeaders(headerDict);
    return this.http.put(this.baseurl + 'User/' + 'GetUserUpdate', obj, { headers });
  }

}
