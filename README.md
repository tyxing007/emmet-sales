# emmet-sales
##Profoma Invoice
### Create
Example:
```json
curl -iX POST -H "Content-Type: application/json" -d '
{
  "info" : {
    "customerDocumentId" : "8888",
    "proformaInvoiceDate" : "2016-1-1",
    "customerFormalName" : "A Company",
    "customerCommonName" : "AP",
    "contactName" : "Jonh Doe",
    "shippingDate" : "2016-2-1",
    "sales" : {"id" : "EM0000112"
     },
    "dataEntryClerk" : {"id" : "EM0000112"
     },
    "customer" : {"id":"AU006"},
    "corporation" : {"id":"98336"},
    "warrantyYear" : 1,
    "currency" : null,
    "taxType" : null
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
    "currency" : null
  } ]
}
' http://api.mycompany.com/sales/proformaInvoice/proformaInvoices
```
