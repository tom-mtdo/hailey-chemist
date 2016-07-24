package com.mtdo.haileychemist.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ShipAddress.class)
public abstract class ShipAddress_ {

	public static volatile SingularAttribute<ShipAddress, String> country;
	public static volatile SingularAttribute<ShipAddress, String> street;
	public static volatile SingularAttribute<ShipAddress, String> postcode;
	public static volatile SingularAttribute<ShipAddress, String> suburb;
	public static volatile SingularAttribute<ShipAddress, Integer> id;
	public static volatile SingularAttribute<ShipAddress, String> state;
	public static volatile SingularAttribute<ShipAddress, Customer> customer;

}

