package models;

import javax.persistence.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

// An address represents a geographical location
@Entity
public class Address extends Model {
	
	@Id
	public int id;
	public String address1;
	public String address2;
	public String city;
	public String state;
	public String zipcode;
	
	@ManyToOne
	@JsonBackReference
	public Company company;
	
	// constructor
	public Address(String address1, String address2, String city, String state, String zipcode, Company company){
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
		this.company = company;
	}
	
	public static Address create(String address1, String address2, String city, String state, String zipcode, int companyId){
		Address address = new Address(address1, address2, city, state, zipcode, Company.find.ref(companyId));
		address.save();
		return address;
	}
	
	//create a find method for data queries
	public static Finder<Integer, Address> find = new Finder<Integer, Address>(
			Integer.class, Address.class
			);
	
	// update
	public static Address update(int id, String address1, String address2, String city, String state, String zipcode) {
		Address address = Address.find.byId(id);
		address.address1 = address1;
		address.address2 = address2;
		address.city = city;
		address.state = state;
		address.zipcode = zipcode;
		address.update();
		return address;
	}
	
}
