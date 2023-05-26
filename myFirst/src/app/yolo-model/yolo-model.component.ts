import { Component, OnInit } from '@angular/core';
import { MyServiceService } from '../my-service.service';
import { Subscription, interval } from 'rxjs';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-yolo-model',
  templateUrl: './yolo-model.component.html',
  styleUrls: ['./yolo-model.component.scss']
})
export class YoloModelComponent implements OnInit {
 intervalSubscription: Subscription | undefined;
  
  constructor(private service : MyServiceService){
    ;
  }
  
  ngOnInit() {
    this.service.logine(true)
    this.getAllDetection()
    this.startInterval()

    
  }
  openModal(){
    this.service.runYoloModel().subscribe(
      (res:any) =>{
        if(res=='Hello World!')
        {
          
        }
      })
      Swal.fire('Model run Sucessfully')
      //this.startInterval()
}
startInterval() {
  this.intervalSubscription = interval(10000).subscribe(() => {
    this.getAllDetection();
  });
}
  detectionList = [];
  getAllDetection(){
    this.service.getAllDetection().subscribe(
      (res:any) =>{
        if(res['httpStatusCode']==200)
        {
            this.detectionList=res['data']
            this.detectionList = this.detectionList.filter(x => (x['traffic'] > 0 || x['fire'] > 0 || x['weapon'] > 0))
            console.log(this.detectionList)
        }
      }
    )
  }
  ngOnDestroy() {
    // Unsubscribe from the interval when the component is destroyed
    if (this.intervalSubscription) {
      this.intervalSubscription.unsubscribe();
    }
  }

}
