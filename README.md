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
    "warrantyYear" : 1,
    "currency" : {"id":"USD"},
    "taxType" : "Extra"
  },
  "extraCharges" : [ {

    "itemName" : "Foo",
    "price" : 100.50,
    "tax" : 5.00
  } ],
  "shipping" : null,
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
***PUT*** /sales/proformaInvoices/{id}

The format is same to create. Every time update a proforma invoice there creates a new version instead, and it becomes the final version automatically. The old version could find and set it to final version again.
### Get One by its ID
***GET*** /sales/proformaInvoices/{id}

### Get Versions
***GET*** /sales/proformaInvoices/{id}/versions

### Find By Sales ID
***GET*** /sales/proformaInvoice/proformaInvoices/search/findBySales{id}
