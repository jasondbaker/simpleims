package models;

import javax.persistence.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.data.format.*;
import play.data.validation.*;
import com.avaje.ebean.*;

// A Contact represents a person that uses the provider's product or service
@Entity
public class Contact extends Model {
	
	@Id
	public String email;
	public String fullname;
	public String phone;
	@ManyToOne
	public Company company;
	
	//constructor
	public Contact(String email, String fullname, String phone, Company company){
		this.email = email;
		this.fullname = fullname;
		this.phone = phone;
		this.company = company;
	}
	
	//create a find method for data queries
	public static Finder<String, Contact> find = new Finder<String, Contact>(
			String.class, Contact.class
			);
	
}