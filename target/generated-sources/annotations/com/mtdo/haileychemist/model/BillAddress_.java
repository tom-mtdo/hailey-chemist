package com.mtdo.haileychemist.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BillAddress.class)
public abstract class BillAddress_ {

	public static volatile SingularAttribute<BillAddress, String> country;
	public static volatile SingularAttribute<BillAddress, String> street;
	public static volatile SingularAttribute<BillAddress, String> postcode;
	public static volatile SingularAttribute<BillAddress, String> suburb;
	public static volatile SingularAttribute<BillAddress, Integer> id;
	public static volatile SingularAttribute<BillAddress, String> state;
	public static volatile SingularAttribute<BillAddress, Customer> customer;

}

