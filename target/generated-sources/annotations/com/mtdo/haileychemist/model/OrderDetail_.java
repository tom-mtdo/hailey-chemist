package com.mtdo.haileychemist.model;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OrderDetail.class)
public abstract class OrderDetail_ {

	public static volatile SingularAttribute<OrderDetail, Product> product;
	public static volatile SingularAttribute<OrderDetail, Float> quantity;
	public static volatile SingularAttribute<OrderDetail, String> itemSerialNo;
	public static volatile SingularAttribute<OrderDetail, Purchase> purchase;
	public static volatile SingularAttribute<OrderDetail, String> quantityUnit;
	public static volatile SingularAttribute<OrderDetail, Integer> id;
	public static volatile SingularAttribute<OrderDetail, BigDecimal> pricePerUnit;

}

