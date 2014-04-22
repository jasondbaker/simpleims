package models;

import javax.persistence.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

// A Contact represents a person that uses the provider's product or service
@Entity
public class Contact extends Model {
	
	@Id
	public int id;
	public String email;
	public String fullname;
	public String phone;
	public Boolean active;
	
	@ManyToOne
	@JsonBackReference
	public Company company;
	
	//constructor
	public Contact(String email, String fullname, String phone, Company company){
		this.email = email;
		this.fullname = fullname;
		this.phone = phone;
		this.company = company;
		this.active = true;
	}
	
	public static Contact create(String email, String fullname, String phone, int companyId) {
		Company company = Company.find.byId(companyId);
		Contact contact = new Contact(email, fullname, phone, company);
		contact.save();
		return contact;
	}
	
	//create a find method for data queries
	public static Finder<Integer, Contact> find = new Finder<Integer, Contact>(
			Integer.class, Contact.class
			);
	
	// delete contact
	// change the active property to false, don't actually delete the data because we need it
	public static Boolean delete(int id) {
		Contact contact = find.byId(id);
		contact.active = false;
		contact.update();
		return true;
	}
	
	//update
	public static Contact update(int id, String email, String fullname, String phone) {
		Contact contact = find.byId(id);
		contact.email = email;
		contact.fullname = fullname;
		contact.phone = phone;
		contact.update();
		return contact;
	}
}
