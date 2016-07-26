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
-- products
insert into product (product_no, name, description, rrp) values('PRD001', 'Fish oil', 'Black more oidless fish oil, 400 caples, 1000mg', 29.99)
insert into product (product_no, name, description, rrp) values('PRD002', 'Men multiple vitamin', 'Nature own multiple vitamin, 300 caples', 25.27)
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
