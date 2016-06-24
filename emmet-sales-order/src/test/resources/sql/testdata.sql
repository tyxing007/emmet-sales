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
DELETE FROM core_customer;
DELETE FROM core_customer_purchase_order;
DELETE FROM core_partner;
DELETE FROM common_country;
DELETE FROM core_employee;
DELETE FROM core_warehouse;
DELETE FROM core_organization;
DELETE FROM core_material_stock;
DELETE FROM core_batch_number;
DELETE FROM core_form_number;   

COMMIT;

--core_organization	
INSERT INTO core_organization(id, name, parent_organization)
    VALUES ('org_factory', 'mobie-factory', null);
	
--core_warehouse
insert into core_warehouse (id,organization_id,is_available) values ('0049','org_factory',true);  
insert into core_warehouse (id,organization_id,is_available) values ('0050','org_factory',true);  


INSERT INTO core_partner (id) VALUES ('PA00000001');
INSERT INTO core_customer (id, partner_id) VALUES ('AU00000001', 'PA00000001');
INSERT INTO core_partner (id) VALUES ('PA00000002');
INSERT INTO core_customer (id, partner_id) VALUES ('AU00000002', 'PA00000002');

INSERT INTO core_customer_purchase_order (id, customer_id,po_no) VALUES (1,'AU00000001', 'PO881');
INSERT INTO core_customer_purchase_order (id, customer_id,po_no) VALUES (2,'AU00000001', 'PO882');

INSERT INTO common_country(id, iso3166_two_letter_code, iana_country_codetld) VALUES ('9', 'AU', '.au');

INSERT INTO partner_corporation (id, formal_name, common_name, partner_id, country_id, valid_date) 
	VALUES (10, 'AU001 Corporation','AU1', 'PA00000001', '9', '2016-1-29');

	
	
INSERT INTO core_employee (id) values ('EM001');
INSERT INTO partner_contact (id, last_name, first_name) values (1, 'Doe', 'John');

insert into core_material(id,name,batch_no_ctr) values('0000-0001','Bar',true);
insert into core_material(id,name,batch_no_ctr) values('0000-0002','Super Bar',true);

INSERT INTO core_product (id,name,material_id) VALUES ('0000-0001','Bar','0000-0001');
INSERT INTO core_product (id,name,material_id) VALUES ('0000-0002','Super Bar','0000-0002');

--core_batch_number
INSERT INTO core_batch_number(
            id, code, material_id)
    VALUES (112, 'OD'||to_char(now(),'yyyyMMdd')||'01', '0000-0001');	
INSERT INTO core_batch_number(
            id, code, material_id)
    VALUES (113, 'OD'||to_char(now(),'yyyyMMdd')||'01', '0000-0002');	    

--core_form_number
INSERT INTO core_form_number(id) values('OD'||to_char(now(),'yyyyMMdd')||'01');    
    
--core_material_stock
INSERT INTO core_material_stock(
            id, create_date, enabled, form_date, io_qty, batch_number_id, 
            form_number_id, material_id, warehouse_id)
    VALUES (10001, now(), true, '2016-5-12', 3, 112, 
            'OD'||to_char(now(),'yyyyMMdd')||'01', '0000-0001', '0049');
INSERT INTO core_material_stock(
            id, create_date, enabled, form_date, io_qty, batch_number_id, 
            form_number_id, material_id, warehouse_id)
    VALUES (10002, now(), true, '2016-5-12', -2, 112, 
            'OD'||to_char(now(),'yyyyMMdd')||'01', '0000-0001', '0049');            
INSERT INTO core_material_stock(
            id, create_date, enabled, form_date, io_qty, batch_number_id, 
            form_number_id, material_id, warehouse_id)
    VALUES (10003, now(), true, '2016-5-12', 30, 112, 
            'OD'||to_char(now(),'yyyyMMdd')||'01', '0000-0001', '0050');
INSERT INTO core_material_stock(
            id, create_date, enabled, form_date, io_qty, batch_number_id, 
            form_number_id, material_id, warehouse_id)
    VALUES (10004, now(), true, '2016-5-12', -5, 112, 
            'OD'||to_char(now(),'yyyyMMdd')||'01', '0000-0001', '0050');
INSERT INTO core_material_stock(
            id, create_date, enabled, form_date, io_qty, batch_number_id, 
            form_number_id, material_id, warehouse_id)
    VALUES (10005, now(), true, '2016-5-12', 34, 113, 
            'OD'||to_char(now(),'yyyyMMdd')||'01', '0000-0002', '0050');
INSERT INTO core_material_stock(
            id, create_date, enabled, form_date, io_qty, batch_number_id, 
            form_number_id, material_id, warehouse_id)
    VALUES (10006, now(), true, '2016-5-12', -25, 113, 
            'OD'||to_char(now(),'yyyyMMdd')||'01', '0000-0002', '0050');
INSERT INTO core_material_stock(
            id, create_date, enabled, form_date, io_qty, batch_number_id, 
            form_number_id, material_id, warehouse_id)
    VALUES (10007, now(), true, '2016-5-12', 500, 113, 
            'OD'||to_char(now(),'yyyyMMdd')||'01', '0000-0002', '0049');            
            
INSERT INTO common_currency (id,name) VALUES ('USD','USD');

INSERT INTO sales_proforma_invoice(id,cust_po_id) VALUES ('PI1604090030',1);
INSERT INTO sales_proforma_invoice(id,cust_po_id) VALUES ('PI1604200001',2);

INSERT INTO sales_proforma_invoice_versions(id, create_date_time, snapshot, version_sequence, order_id, 
	proforma_invoice_id,status)
    VALUES ('PI1604090030-1', '2016-04-19 17:23:05.035', 'snapshot1231', 1, null, 'PI1604090030','CONFIRMED');
INSERT INTO sales_proforma_invoice_versions(id, create_date_time, snapshot, version_sequence, order_id, 
	proforma_invoice_id,status)
    VALUES ('PI1604090030-2', '2016-04-20 17:23:05.035', 'snapshot1232', 2, null, 'PI1604090030','PROCESSING');
INSERT INTO sales_proforma_invoice_versions(id, create_date_time, snapshot, version_sequence, order_id, 
	proforma_invoice_id,status)
    VALUES ('PI1604090030-3', '2016-04-21 17:23:05.035', 'snapshot1233', 3, null, 'PI1604090030','CONFIRMED');
INSERT INTO sales_proforma_invoice_versions(id, create_date_time, snapshot, version_sequence, order_id, 
	proforma_invoice_id,status)
    VALUES ('PI1604200001-1', '2016-04-22 17:23:05.035', 'snapshot1234', 1, null, 'PI1604200001','CONFIRMED');

    
INSERT INTO sales_proforma_invoice_info(
            id, create_date, discount, proforma_invoice_date, 
            shipping_date, tax, warranty, contact_id, corporation_id, currency_id, 
            customer_id, data_entry_clerk_id, sales_id, proforma_invoice_version_id)
    VALUES (1,'2016-04-20',100.00,'2016-04-19','2016-05-01','{"name":"零稅"}','1 years, and bala...1',1,10,
    'USD','AU00000001','EM001','EM001','PI1604090030-1');
INSERT INTO sales_proforma_invoice_info(
            id, create_date, discount, proforma_invoice_date, 
            shipping_date, tax, warranty, contact_id, corporation_id, currency_id, 
            customer_id, data_entry_clerk_id, sales_id, proforma_invoice_version_id)
    VALUES (2,'2016-04-21',100.00,'2016-04-19','2016-05-02','{"name":"零稅"}','2 years, and bala...2',1,10,
    'USD','AU00000001','EM001','EM001','PI1604090030-2');
INSERT INTO sales_proforma_invoice_info(
            id, create_date, discount, proforma_invoice_date, 
            shipping_date, tax, warranty, contact_id, corporation_id, currency_id, 
            customer_id, data_entry_clerk_id, sales_id, proforma_invoice_version_id)
    VALUES (3,'2016-04-22',100.00,'2016-04-19','2016-05-03','{"name":"零稅"}','3 years, and bala...3',1,10,
    'USD','AU00000001','EM001','EM001','PI1604090030-3');    
INSERT INTO sales_proforma_invoice_info(
            id, create_date, discount, proforma_invoice_date, 
            shipping_date, tax, warranty, contact_id, corporation_id, currency_id, 
            customer_id, data_entry_clerk_id, sales_id, proforma_invoice_version_id)
    VALUES (4,'2016-04-23',100.00,'2016-04-19','2016-05-04','{"name":"零稅"}','4 years, and bala...4',1,10,
    'USD','AU00000001','EM001','EM001','PI1604200001-1');    
    
INSERT INTO sales_proforma_invoice_shipping(id, fare, info, tax, proforma_invoice_version_id)
    VALUES (1, 11, 'aaa', 5, 'PI1604090030-1');
INSERT INTO sales_proforma_invoice_shipping(id, fare, info, tax, proforma_invoice_version_id)
    VALUES (2, 22, 'bbb', 5, 'PI1604090030-2');
INSERT INTO sales_proforma_invoice_shipping(id, fare, info, tax, proforma_invoice_version_id)
    VALUES (3, 33, 'ccc', 5, 'PI1604090030-3');
INSERT INTO sales_proforma_invoice_shipping(id, fare, info, tax, proforma_invoice_version_id)
    VALUES (4, 44, 'ddd', 5, 'PI1604200001-1');    
    
    
INSERT INTO sales_proforma_invoice_products(
            id, note1, note2, note3, quantity, unit, unit_price, currency_id, 
            proforma_invoice_version_id, product_id)
    VALUES (1, 'note1', 'note2', 'note3', 5, 'PCS', 50, 'USD', 'PI1604090030-1', '0000-0001');    
INSERT INTO sales_proforma_invoice_products(
            id, note1, note2, note3, quantity, unit, unit_price, currency_id, 
            proforma_invoice_version_id, product_id)
    VALUES (2, 'note1', 'note2', 'note3', 10, 'PCS', 50, 'USD', 'PI1604090030-1', '0000-0002'); 
INSERT INTO sales_proforma_invoice_products(
            id, note1, note2, note3, quantity, unit, unit_price, currency_id, 
            proforma_invoice_version_id, product_id)
    VALUES (3, 'note1', 'note2', 'note3', 6, 'PCS', 50, 'USD', 'PI1604090030-2', '0000-0001');
INSERT INTO sales_proforma_invoice_products(
            id, note1, note2, note3, quantity, unit, unit_price, currency_id, 
            proforma_invoice_version_id, product_id)
    VALUES (4, 'note1', 'note2', 'note3', 7, 'PCS', 50, 'USD', 'PI1604090030-3', '0000-0001');    
INSERT INTO sales_proforma_invoice_products(
            id, note1, note2, note3, quantity, unit, unit_price, currency_id, 
            proforma_invoice_version_id, product_id)
    VALUES (5, 'note1', 'note2', 'note3', 12, 'PCS', 50, 'USD', 'PI1604200001-1', '0000-0001');
INSERT INTO sales_proforma_invoice_products(
            id, note1, note2, note3, quantity, unit, unit_price, currency_id, 
            proforma_invoice_version_id, product_id)
    VALUES (6, 'note1', 'note2', 'note3', 36, 'PCS', 50, 'USD', 'PI1604200001-1', '0000-0002');        
    
    
INSERT INTO sales_proforma_invoice_extra_charge(id, item_name, price, tax, proforma_invoice_version_id)
    VALUES (1, 'q11 yyhhj', 24, 5, 'PI1604090030-1');   
INSERT INTO sales_proforma_invoice_extra_charge(id, item_name, price, tax, proforma_invoice_version_id)
    VALUES (2, 'q22 yyhhj', 24, 5, 'PI1604090030-2');  
INSERT INTO sales_proforma_invoice_extra_charge(id, item_name, price, tax, proforma_invoice_version_id)
    VALUES (3, 'q33 yyhhj', 24, 5, 'PI1604090030-3');  
INSERT INTO sales_proforma_invoice_extra_charge(id, item_name, price, tax, proforma_invoice_version_id)
    VALUES (4, 'q44t yyhhj', 24, 5, 'PI1604200001-1');      
    
    
    
    
    
    
    
    
    
    
    
    
    
    