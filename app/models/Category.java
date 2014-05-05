package models;

import javax.persistence.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

import play.data.format.*;
import play.data.validation.*;

import java.util.*;

// An Action represents a unit of work spent by an Agent on a specific incident 
@Entity
public class Category extends Model {
	
	@Id
	public int id;
	public String type;
	
	
	// constructor methods
	public Category(String type){
		this.type = type;
	}
		
	//create a find method for data queries
	public static Finder<Integer, Category> find = new Finder<Integer, Category>(
			Integer.class, Category.class
			);
	
}

