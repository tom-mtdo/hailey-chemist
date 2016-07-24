package com.mtdo.haileychemist.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Media.class)
public abstract class Media_ {

	public static volatile SingularAttribute<Media, String> itemSerial;
	public static volatile SingularAttribute<Media, Product> product;
	public static volatile SingularAttribute<Media, Integer> id;
	public static volatile SingularAttribute<Media, String> type;
	public static volatile SingularAttribute<Media, String> url;

}

