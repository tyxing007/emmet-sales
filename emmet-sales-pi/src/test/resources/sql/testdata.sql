DELETE FROM core_employee;
DELETE FROM core_product;
COMMIT;

INSERT INTO core_employee (id) values ('EM001');
INSERT INTO core_product (id,name) VALUES ('0000-0001','Bar');
INSERT INTO core_product (id,name) VALUES ('0000-0002','Super Bar');
