package com.mtdo.haileychemist.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the customer database table.
 * 
 */
@Entity
@NamedQuery(name="Customer.findAll", query="SELECT c FROM Customer c")
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private int id;

	private String email;

	@Column(name="first_name")
	private String firstName;

	@Column(name="last_name")
	private String lastName;

	private String phone;

	//bi-directional many-to-one association to BillAddress
	@OneToMany(mappedBy="customer")
	private List<BillAddress> billAddresses;

	//bi-directional many-to-one association to ShipAddress
	@OneToMany(mappedBy="customer")
	private List<ShipAddress> shipAddresses;

	public Customer() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<BillAddress> getBillAddresses() {
		return this.billAddresses;
	}

	public void setBillAddresses(List<BillAddress> billAddresses) {
		this.billAddresses = billAddresses;
	}

	public BillAddress addBillAddress(BillAddress billAddress) {
		getBillAddresses().add(billAddress);
		billAddress.setCustomer(this);

		return billAddress;
	}

	public BillAddress removeBillAddress(BillAddress billAddress) {
		getBillAddresses().remove(billAddress);
		billAddress.setCustomer(null);

		return billAddress;
	}

	public List<ShipAddress> getShipAddresses() {
		return this.shipAddresses;
	}

	public void setShipAddresses(List<ShipAddress> shipAddresses) {
		this.shipAddresses = shipAddresses;
	}

	public ShipAddress addShipAddress(ShipAddress shipAddress) {
		getShipAddresses().add(shipAddress);
		shipAddress.setCustomer(this);

		return shipAddress;
	}

	public ShipAddress removeShipAddress(ShipAddress shipAddress) {
		getShipAddresses().remove(shipAddress);
		shipAddress.setCustomer(null);

		return shipAddress;
	}

}