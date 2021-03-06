# emmet-sales
## Proforma Invoice

### Create a new Proforma Invoice Version By Proforma Invoice Version id
Example:
```json
curl -iX POST -H "Content-Type: application/json" -d '' 
http://api.mycompany.com/sales/proformaInvoice/proformaInvoices/versions/PI1604190016-1/copyFrom
```



### Create a new Proforma Invoice
Example:
```json
curl -iX POST -H "Content-Type: application/json" -d '
{
	"custPo":{"poNo":"EW2016-11-22-0001"},
	"proformaInvoiceVersion":{
	  "info" : {
	    "proformaInvoiceDate" : "2016-1-1",
	    "contact" : {"id":557088},
	    "shippingDate" : "2016-2-1",
	    "sales" : {"id" : "EM0000112"},
	    "dataEntryClerk" : {"id" : "EM0000112"},
	    "customer" : {"id":"AU006"},
	    "corporation" : {"id":98336},
	    "warranty" : "2 years, and bala...",
	    "currency" : {"id":"USD"},
	    "tax" : "Extra"
	  },
	  "extraCharges" : [ {	
	    "itemName" : "Foo",
	    "price" : 100.50,
	    "tax" : 5.00
	  } ],
	  "shipping" : {
	     "info" : "DHL, ...",
	     "fare" : 10.50,
	     "tax" : 0
	   },
	  "productItems" : [ {	
	    "product" : {
	    "id" : "0000-0001"
	    },
	    "quantity" : 1,
	    "unit" : "PCS",
	    "unitPrice" : 100.50,
	    "note1" : "123",
	    "note2" : "456",
	    "note3" : "{\"aaa\":\"ccc\"}",
	    "currency" : {"id":"USD"}
	  } ]
	}
}
' http://api.mycompany.com/sales/proformaInvoice/proformaInvoices
```
If you'd like to put the JSON to the notes fields, escape the quotes, make it like common string.
### Update
***PUT*** /sales/proformaInvoice/proformaInvoices/versions/{id}

The format is same to create. Every time update a proforma invoice there creates a new version instead, and it becomes the final version automatically. The old version could find and set it to final version again.

To modify a customer of a proforma invoice is not permitted.

#### Set version status

***PUT*** /sales/proformaInvoice/proformaInvoices/versions/{id}/setStatus

Example:
```
curl -iX PUT -H "Content-Type: application/json" -d '{"status":"INITIALIZED"}'
http://api.mycompany.com/sales/proformaInvoice/proformaInvoices/versions/PI1604140022-4/setStatus


the status could be 
* INITIALIZED
* PROCESSING
* CONFIRMED
* ABANDONED
* ORDERED

```




### Get data
#### Find one by its ID
***GET*** /sales/proformaInvoice/proformaInvoices/{id}

#### Get specific version of a proforma invoice
***GET*** /sales/proformaInvoice/proformaInvoices/{id}/versions/{versionId}


#### List by sales ID

***GET*** /sales/proformaInvoice/proformaInvoices/list/filterBySalesAndPiLike{?piId,salesId,size,page}

Example:
http://api.mycompany.com/sales/proformaInvoice/proformaInvoices/list/filterBySalesAndPiLike?piId=05&salesId=admin


#### Find by Order ID

***GET*** /sales/proformaInvoice/proformaInvoices/versions/search/findByOrder{?id}

## Sales Order
### Create a Sales Order by Proforma Invoice
Example:
```json
curl -iX POST -H "Content-Type: application/json" -d '{"piVersionId":"PI1603150002-2"}'
http://api.mycompany.com/sales/order/orders/createFromPI
```

### List sales orders by id like
***GET*** /sales/order/orders/search/findByIdLike{?id,page,size}

Example:
http://api.mycompany.com/sales/order/orders/search/findByIdLike?id=0&page=0&size=4


### List all sales order
***GET*** /sales/order/orders/{?,page,size}

Example:
http://api.mycompany.com/sales/order/orders/?page=0&size=4

### Get a sales order by id
***GET*** /sales/order/orders/{id}

Example:
http://api.mycompany.com/sales/order/orders/OD2016041502

### Update a Sales Order
Example:
```json
curl -iX PUT -H "Content-Type: application/json" -d '{
  "info" : {
    "createDate":"2016-04-15T06:30:11.665+0000",
    "contact" : "{\"id\":557714,\"firstName\":\"Wayne Whiteley\",\"lastName\":null}",
    "shippingDate" : "2016-2-1",
    "sales" : {"id" : "EM0000112"},
    "dataEntryClerk" : {"id" : "EM0000112"},
    "customer" : {"id":"AU006"},
    "corporation" : "{\"id\":98964,\"formalName\":\"ClearOne Communications \"}",
    "warranty" : "2 years, and bala...",
    "currency" : {"id":"USD"},
    "tax" : "Extra"
  },
  "extraCharges" : [ {
    "itemName" : "Foo",
    "price" : 100.50,
    "tax" : 5.00
  } ],
  "shipping" : {
     "info" : "DHL, ...",
     "fare" : 10.50,
     "tax" : 0
   },
  "productItems" : [ {
    "product" : {
    "id" : "0000-0001"
    },
    "quantity" : 11,
    "unit" : "PCS",
    "unitPrice" : 100.50,
    "note1" : "12333",
    "note2" : "42256",
    "note3" : "{\"aaa\":\"ccc\"}",
    "currency" : {"id":"USD"}
  } ]
}'
http://api.mycompany.com/sales/order/orders/OD2016041502
```

#### Set order status

***PUT*** /sales/order/orders/{id}/setStatus

Example:
```
curl -iX PUT -H "Content-Type: application/json" -d '{"status":"INITIALIZED"}'
http://api.mycompany.com/sales/order/orders/PI1604140022-4/setStatus


the status could be 
* INITIALIZED
* PROCESSING
* CONFIRMED
* ABANDONED

```


## Sales Slip

### Create a Sales Slip by order Item List
Example:
```json
curl -iX POST -H "Content-Type: application/json" -d '
{
  "userId":"EM0000112",
  "orderItemList":[
    {"id":1540108},
    {"id":1540109},
    {"id":1540110}
  ]
}
'
http://api.mycompany.com/sales/order/salesSlips/createSalesSlip
```

### Updare  Sales Slip
Example:
```json
curl -iX POST -H "Content-Type: application/json" -d '
{
    "formDate" : "2016-06-13",
    "note" : "2232",
    "salesSlipDetails" : [
      {
        "materialStock" : {
          "ioQty" : 5.00,
          "warehouse" : {"id" : "0050"}          
        },
        "orderItem" : {"id" : 1540108}
      },
      {
        "materialStock" : {
          "ioQty" : 15.00,
          "warehouse" : {"id" : "0049"}          
        },
        "orderItem" : {"id" : 1540108}
      }
    ]
}
'
http://api.mycompany.com/sales/order/salesSlips/IN1604140022
```


#### Set order slip status

***PUT*** /sales/order/salesSlips/{id}/setStatus

Example:
```
curl -iX PUT -H "Content-Type: application/json" -d '{"status":"CONFIRMED"}'
http://api.mycompany.com/sales/order/salesSlips/IN1604140022/setStatus


the status could be 
* INITIALIZED
* PROCESSING
* CONFIRMED
* ABANDONED

```


### Get a Order Item List that can be used to create Sales Slip
***GET*** /sales/order/salesSlips/orderItemList/search/findByCustIdAndOrdId{?custId,ordId}

Example:
http://api.mycompany.com/sales/order/salesSlips/orderItemList/search/findByCustIdAndOrdId?custId=US001&ordId=OD2016

### Get a Customer List for creatting Sales Slip
***GET*** /sales/order/salesSlips/customerList/search/findByNameLike{?id}

Example:
http://api.mycompany.com/sales/order/salesSlips/customerList/search/findByIdLike?id=US


### Find Sales Slip by its ID

GET /sales/order/salesSlips/{id}


### List sales Slip by id like

GET /sales/order/salesSlips/search/findByIdLike{?id,page,size}

Example: http://api.mycompany.com/sales/order/salesSlips/search/findByIdLike?id=0&page=0&size=4
