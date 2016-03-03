DELETE FROM core_employee;
DELETE FROM core_product;
DELETE FROM common_currency;
COMMIT;

INSERT INTO core_employee (id) values ('EM001');
INSERT INTO core_product (id,name) VALUES ('0000-0001','Bar');
INSERT INTO core_product (id,name) VALUES ('0000-0002','Super Bar');
INSERT INTO common_currency (id,name) VALUES ('USD','USD');
