package models;

import javax.persistence.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import com.avaje.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import java.util.*;

// An Action represents a unit of work spent by an Agent on a specific incident (ticket)
@Entity
public class Action extends Model {
	
	@Id
	public int id;
	public Date startdate;
	@ManyToOne
	public Agent agent;
	public String description;
	@ManyToOne
	public Ticket ticket;
	
	//create a find method for data queries
	public static Finder<Integer, Action> find = new Finder<Integer, Action>(
			Integer.class, Action.class
			);
	
}
