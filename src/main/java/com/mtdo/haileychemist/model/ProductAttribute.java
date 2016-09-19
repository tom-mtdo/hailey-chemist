package com.mtdo.haileychemist.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Entity implementation class for Entity: ProductAttribute
 *
 */
@Entity
@Table(name="product_attribute")
@JsonIgnoreProperties({"product"})
public class ProductAttribute implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private int id;
	
	@ManyToOne
	private Product product;
	
	@ManyToOne
	private Attribute attribute;
	
	private String attribute_value;

	public ProductAttribute() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	public String getAttribute_value() {
		return attribute_value;
	}

	public void setAttribute_value(String attribute_value) {
		this.attribute_value = attribute_value;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
