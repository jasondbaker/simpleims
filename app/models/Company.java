package models;

import javax.persistence.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

// A Company represents an organization that uses the provider's product or service
@Entity
public class Company extends Model {
	
	@Id
	public int id;
	public String name;
	public String notes;
	
	// constructor
	public Company(String name, String notes){
		this.name = name;
		this.notes = notes;
	}
	
	//create a find method for data queries
	public static Finder<Integer, Company> find = new Finder<Integer, Company>(
			Integer.class, Company.class
			);

	//update
	public static String update(int id, String name, String notes) {
		Company company = find.byId(id);
		company.name = name;
		company.notes = notes;
		return name;
	}
	
}
