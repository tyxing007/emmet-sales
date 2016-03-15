# emmet-sales
## Proforma Invoice
### Create a new Proforma Invoice
Example:
```json
curl -iX POST -H "Content-Type: application/json" -d '
{
  "info" : {
    "customerDocumentId" : "8888",
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
' http://api.mycompany.com/sales/proformaInvoice/proformaInvoices
```
If you'd like to put the JSON to the notes fields, escape the quotes, make it like common string.
### Update
***PUT*** /sales/proformaInvoice/proformaInvoices/{id}

The format is same to create. Every time update a proforma invoice there creates a new version instead, and it becomes the final version automatically. The old version could find and set it to final version again.

To modify a customer of a proforma invoice is not permitted.

#### Set final version
***PUT*** /sales/proformaInvoice/proformaInvoices/{id}/setFinalVersion?{version}

Example:
```
curl -iX PUT \
http://api.mycompany.com/sales/proformaInvoice/proformaInvoices/PI0603020001/setFinalVersion?version=PI0603020001-1
```
#### Set confirmed

***PUT*** /sales/proformaInvoice/proformaInvoices/{id}/setConfirmed

Example:
```
curl -iX PUT \
http://api.mycompany.com/sales/proformaInvoice/proformaInvoices/PI0603020001/setConfirmed
```

### Get data
#### Find one by its ID
***GET*** /sales/proformaInvoice/proformaInvoices/{id}

#### Get specific version of a proforma invoice
***GET*** /sales/proformaInvoice/proformaInvoices/{id}/versions/{versionId}


#### Find by sales ID

***GET*** /sales/proformaInvoice/proformaInvoices/search/findBySales{?id}

#### Find by sales ID within status
***GET*** /sales/proformaInvoice/proformaInvoices/search/findBySales{?id, status}

the status could be empty or
* PROCESSING
* CONFIRMED
* ABANDONED
