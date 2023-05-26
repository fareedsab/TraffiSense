import { Component } from '@angular/core';

@Component({
  selector: 'app-slip',
  templateUrl: './slip.component.html',
  styleUrls: ['./slip.component.scss']
})


export class SlipComponent {
  printSlip() {
    window.print();
  }
  orderId = '1234';
  customerName = 'John Doe';
  orderItems = [
    { name: 'Item 1', quantity: 2, price: 10.00 },
    { name: 'Item 2', quantity: 1, price: 5.00 },
    { name: 'Item 3', quantity: 3, price: 7.50 },
  ];
  total = this.orderItems.reduce((acc, item) => acc + item.quantity * item.price, 0);
  printPosSlip() {
    debugger
    // const printContent = document.getElementById('pos-slip');
    const printContent = document.getElementById('pos-slip')?.innerHTML 
    console.log(printContent)
    // const popupWindow = window.open('', '_blank', 'top=0,left=0,height=100%,width=auto');
    window.document.open();
    window.document.write(`
      <html>
        <head>
          <title>POS Slip</title>
          <style>
            /* Add any additional styles here */
           body{
            font-size=10px !important;
           }
           
          </style>
        </head>
        <body onload="window.print();">${printContent}</body>
      </html>
   
    `);
    window.document.close();
    // onload="window.print();"
  }

}

