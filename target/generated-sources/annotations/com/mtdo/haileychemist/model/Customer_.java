package com.mtdo.haileychemist.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Customer.class)
public abstract class Customer_ {

	public static volatile SingularAttribute<Customer, String> firstName;
	public static volatile SingularAttribute<Customer, String> lastName;
	public static volatile ListAttribute<Customer, BillAddress> billAddresses;
	public static volatile SingularAttribute<Customer, String> phone;
	public static volatile SingularAttribute<Customer, Integer> id;
	public static volatile SingularAttribute<Customer, String> email;
	public static volatile ListAttribute<Customer, ShipAddress> shipAddresses;

}

