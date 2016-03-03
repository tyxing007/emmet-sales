# emmet-sales
##Profoma Invoice
### Create
Example:
```json
curl -iX POST -H "Content-Type: application/json" -d '
{
  "info" : {
    "customerDocumentId" : "8888"
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
    "currency" : "USD"
  } ]
}
' http://api.mycompany.com/sales/proformaInvoice/proformaInvoices
```
