package com.mtdo.haileychemist.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Purchase.class)
public abstract class Purchase_ {

	public static volatile SingularAttribute<Purchase, Date> date;
	public static volatile SetAttribute<Purchase, OrderDetail> orderDetails;
	public static volatile SingularAttribute<Purchase, BigDecimal> total;
	public static volatile SingularAttribute<Purchase, String> notes;
	public static volatile SingularAttribute<Purchase, Integer> id;
	public static volatile SingularAttribute<Purchase, Integer> staffId;
	public static volatile SingularAttribute<Purchase, String> status;
	public static volatile SingularAttribute<Purchase, Customer> customer;

}

