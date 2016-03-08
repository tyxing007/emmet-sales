DELETE FROM sales_proforma_invoice_products;
DELETE FROM sales_proforma_invoice_info;
DELETE FROM sales_proforma_invoice_extra_charge;
UPDATE sales_proforma_invoice SET final_version_id = null;
DELETE FROM sales_proforma_invoice_versions;
DELETE FROM sales_proforma_invoice;

DELETE FROM core_employee;
DELETE FROM core_product;
DELETE FROM common_currency;
DELETE FROM partner_contact;
COMMIT;

INSERT INTO core_employee (id) values ('EM001');
INSERT INTO partner_contact (id, last_name, first_name) values (1, 'Doe', 'John');
INSERT INTO core_product (id,name) VALUES ('0000-0001','Bar');
INSERT INTO core_product (id,name) VALUES ('0000-0002','Super Bar');
INSERT INTO common_currency (id,name) VALUES ('USD','USD');
