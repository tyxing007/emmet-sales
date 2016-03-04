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
It can also put the JSON that has escaped the quotes to the notes fields.
