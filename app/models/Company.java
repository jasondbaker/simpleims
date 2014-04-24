package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

// A Company represents an organization that uses the provider's product or service
@Entity
public class Company extends Model {
	
	@Id
	public int id;
	public String name;
	public String notes;
	public String website;
	public Boolean active;
	
	@OneToMany(mappedBy="company", cascade=CascadeType.ALL) 
	@JsonManagedReference
	public List<Address> addresses;
	
	@OneToMany(mappedBy="company", cascade=CascadeType.ALL) 
	@JsonManagedReference
	public List<Contact> contacts;
	
	// constructor
	public Company(String name, String notes, String website){
		this.name = name;
		this.notes = notes;
		this.website = website;
		this.active = true;
		this.addresses = new ArrayList<Address>();
		this.contacts = new ArrayList<Contact>();
	}
	
	// creator
	public static Company create(String name, String notes, String website){
		Company company = new Company(name, notes, website);
		company.save();
		return company;
	}
	
	//create a find method for data queries
	public static Finder<Integer, Company> find = new Finder<Integer, Company>(
			Integer.class, Company.class
			);
	
	// delete
	public static Boolean delete(int id) {
		Company company = find.byId(id);
		company.active = false;
		company.update();
		return true;
	}
	
	//update
	public static Company update(int id, String name, String notes, String website) {
		Company company = find.byId(id);
		company.name = name;
		company.notes = notes;
		company.website = website;
		company.update();
		return company;
	}
	
}
