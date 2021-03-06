DELETE FROM sales_proforma_invoice_shipping;
DELETE FROM sales_proforma_invoice_products;
DELETE FROM sales_proforma_invoice_info;
DELETE FROM sales_proforma_invoice_extra_charge;
DELETE FROM sales_proforma_invoice_versions;
DELETE FROM sales_proforma_invoice;

DELETE FROM core_employee;
DELETE FROM core_product;
DELETE FROM common_currency;
DELETE FROM partner_contact;

DELETE FROM partner_corporation;
DELETE FROM core_customer_purchase_order;
DELETE FROM core_customer;
DELETE FROM core_partner;
DELETE FROM common_country;
DELETE FROM core_employee;


COMMIT;

INSERT INTO core_partner (id) VALUES ('PA00000001');
INSERT INTO core_customer (id, partner_id) VALUES ('AU00000001', 'PA00000001');
INSERT INTO core_partner (id) VALUES ('PA00000002');
INSERT INTO core_customer (id, partner_id) VALUES ('AU00000002', 'PA00000002');

INSERT INTO common_country(id, iso3166_two_letter_code, iana_country_codetld) VALUES ('9', 'AU', '.au');

INSERT INTO partner_corporation (id, formal_name, common_name, partner_id, country_id, valid_date) VALUES (10, 'AU001 Corporation','AU1', 'PA00000001', '9', '2016-1-29');



INSERT INTO core_employee (id) values ('EM001');
INSERT INTO partner_contact (id, last_name, first_name) values (1, 'Doe', 'John');
INSERT INTO core_product (id,name) VALUES ('0000-0001','Bar');
INSERT INTO core_product (id,name) VALUES ('0000-0002','Super Bar');
INSERT INTO common_currency (id,name) VALUES ('USD','USD');
