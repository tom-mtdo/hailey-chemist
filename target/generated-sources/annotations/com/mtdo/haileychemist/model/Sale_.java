package com.mtdo.haileychemist.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Sale.class)
public abstract class Sale_ {

	public static volatile SingularAttribute<Sale, Date> endDate;
	public static volatile SingularAttribute<Sale, BigDecimal> price;
	public static volatile SingularAttribute<Sale, String> description;
	public static volatile SingularAttribute<Sale, Integer> id;
	public static volatile SingularAttribute<Sale, Date> startDate;

}

