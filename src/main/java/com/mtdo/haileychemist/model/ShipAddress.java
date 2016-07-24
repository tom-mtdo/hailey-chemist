package com.mtdo.haileychemist.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The persistent class for the ship_address database table.
 * 
 */
@Entity
@Table(name="ship_address")
@NamedQuery(name="ShipAddress.findAll", query="SELECT s FROM ShipAddress s")
@JsonIgnoreProperties("customer")
public class ShipAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String country;

	private String postcode;

	private String state;

	private String street;

	private String suburb;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	private Customer customer;

	public ShipAddress() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostcode() {
		return this.postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getSuburb() {
		return this.suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}