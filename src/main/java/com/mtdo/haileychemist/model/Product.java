package com.mtdo.haileychemist.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * The persistent class for the product database table.
 * 
 */
@Entity
@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private int id;

	private String description;

	private String name;

	@Column(name="product_no")
	private String productNo;

	private BigDecimal rrp;

	//bi-directional many-to-one association to attribute
	@OneToMany(cascade = ALL, fetch = EAGER, mappedBy="product")
	private List<ProductAttribute> productAttributes = new ArrayList<ProductAttribute>();

	//bi-directional many-to-one association to Media
	@OneToMany(cascade = ALL, fetch = EAGER, mappedBy="product")
	private List<Media> medias = new ArrayList<Media>();

	//bi-directional many-to-one association to Sale
//	@OneToMany(cascade = ALL, fetch = EAGER, mappedBy="product")
//	private List<Sale> sales = new ArrayList<Sale>();

	//bi-directional one-to-one association to Sale
	@OneToOne
	private Sale sale;

	//one-directional many-to-one association to Product
	@ManyToOne
	private Category category;
	
	public Product() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductNo() {
		return this.productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public BigDecimal getRrp() {
		return this.rrp;
	}

	public void setRrp(BigDecimal rrp) {
		this.rrp = rrp;
	}

	public List<Media> getMedias() {
		return this.medias;
	}

	public void setMedias(List<Media> medias) {
		this.medias = medias;
	}

	public Media addMedia(Media media) {
		getMedias().add(media);
		media.setProduct(this);

		return media;
	}

	public Media removeMedia(Media media) {
		getMedias().remove(media);
		media.setProduct(null);

		return media;
	}

//	public List<Sale> getSales() {
//		return this.sales;
//	}
//
//	public void setSales(List<Sale> sales) {
//		this.sales = sales;
//	}
//
//	public Sale addSale(Sale sale) {
//		getSales().add(sale);
//		sale.setProduct(this);
//
//		return sale;
//	}
//
//	public Sale removeSale(Sale sale) {
//		getSales().remove(sale);
//		sale.setProduct(null);
//		
//		return sale;
//	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<ProductAttribute> getProductAttributes() {
		return productAttributes;
	}

	public void setProductAttributes(List<ProductAttribute> productAttributes) {
		this.productAttributes = productAttributes;
	}

	
}