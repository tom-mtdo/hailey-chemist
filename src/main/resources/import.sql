--
-- JBoss, Home of Professional Open Source
-- Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
-- contributors by the @authors tag. See the copyright.txt in the
-- distribution for a full listing of individual contributors.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
-- http://www.apache.org/licenses/LICENSE-2.0
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- You can use this file to load seed data into the database using SQL statements
insert into Member (id, name, email, phone_number) values (0, 'John Smith', 'john.smith@mailinator.com', '2125551212') 

-- Categories
INSERT INTO category (id, lft, name, rgt) VALUES(1,	1,	'ConsumingChemist',	16)
INSERT INTO category (id, lft, name, rgt) VALUES(8,	2,	'General',			 5)
INSERT INTO category (id, lft, name, rgt) VALUES(9,	3,	'Other',			 4)
INSERT INTO category (id, lft, name, rgt) VALUES(2,	6,	'Vitamin',			11)
INSERT INTO category (id, lft, name, rgt) VALUES(4,	7,	'MenVitamin',		 8)
INSERT INTO category (id, lft, name, rgt) VALUES(5,	9,	'WomenVitamin',		10)
INSERT INTO category (id, lft, name, rgt) VALUES(3,	12,	'BathShower',		17)
INSERT INTO category (id, lft, name, rgt) VALUES(6,	13,	'BodyWash',			14)
INSERT INTO category (id, lft, name, rgt) VALUES(7,	15,	'HairCare',			16)

--INSERT INTO category (id, name, lft, rgt) VALUES(4,'MultipleVitamin',5)
--INSERT INTO category (id, name, lft, rgt) VALUES(8,'MultipleVitamin',9)
--INSERT INTO category (id, name, lft, rgt) VALUES(10,'Boost',11)
--INSERT INTO category (id, name, lft, rgt) VALUES(16,'HoneySoap',17)
--INSERT INTO category (id, name, lft, rgt) VALUES(18,'CoconutSoap',19)
--INSERT INTO category (id, name, lft, rgt) VALUES(22,'VitaminEConditioner',23)

-- products
--insert into product (product_no, name, description, rrp) values('PRD003', 'Swisse Women''s Ultivite 120 Tablets', 'Swisse Women''s Ultivite Formula 1 contains 50 premium quality vitamins, minerals, antioxidants and herbs to help support women''s nutritional needs and maintain general wellbeing.', 69.95)
insert into product (id, category_id, product_no, name, description, rrp) values(1, 9, 'PRD001', 'Fish oil', 'Black more oidless fish oil, 400 caples, 1000mg', 29.99)
insert into product (id, category_id, product_no, name, description, rrp) values(2, 4, 'PRD002', 'Men multiple vitamin', 'Nature own multiple vitamin, 300 caples', 25.27)
insert into product (id, category_id, product_no, name, description, rrp) values(3, 7, 'PRD003', 'Aloe Shampo', 'Aloe vera cream', 13.27)
insert into product (id, category_id, product_no, name, description, rrp) values(4, 5, 'PRD004', 'Women Swisse Cranberry', 'Swisse Cranberry', 36.95)
insert into product (id, category_id, product_no, name, description, rrp) values(5, 9, 'PRD005', 'Healthy Care Grape Seed Extract 12000 Gold Jar 300 Capsules', 'Assists in the maintenance of blood flow in the hands, feet and legs. ', 25.99)
insert into product (id, category_id, product_no, name, description, rrp) values(6, 6, 'PRD006', 'Dove body wash', 'Dove body wash innovative', 5.35)

-- media
insert into media (type, url, product_id) values('image', './resources/img/product/fish-oil.png', 1)
insert into media (type, url, product_id) values('image', './resources/img/product/multiple-vitamins.png', 2)
insert into media (type, url, product_id) values('image', './resources/img/product/aloe.png', 3)
insert into media (type, url, product_id) values('image', './resources/img/product/swisse-cranberry.png', 4)
insert into media (type, url, product_id) values('image', './resources/img/product/grape-seed.png', 5)

-- sale
insert into sale (product_id, price, start_date, end_date) values(1, 19.99, '2016-07-24 19:00:00', '2016-08-24 19:00:00')
insert into sale (product_id, price, start_date, end_date) values(2, 15.27, '2016-07-24 19:00:00', '2016-08-24 19:00:00')
-- customers
insert into customer (first_name, last_name, email, phone) values('Tom', 'Do', 'tom.mtdo@gmail.com', '1111111')
insert into customer (first_name, last_name, email, phone) values('John', 'Smith', 'john.smith@gmail.com', '2222222')
-- bill address
insert into bill_address (customer_id, street, suburb, state, postcode, country) values (1, '17 Lovely Street', 'Mill Park', 'VIC', '3082', 'Australia')
-- ship address
insert into ship_address (customer_id, street, suburb, state, postcode, country) values (1, '17 Lovely Street', 'Mill Park', 'VIC', '3082', 'Australia')
-- purchase
insert into purchase (customer_id, staff_id, date, status) values(1, 1, '2016-07-24 21:03:00', 'completed')
-- order_details
insert into order_details (purchase_id, product_id, quantity, quantity_unit, price_per_unit) values(1, 2, 3, 'each', 15.27)
