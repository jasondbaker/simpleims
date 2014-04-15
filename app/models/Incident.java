package models;

import javax.persistence.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import java.util.*;

// An Incident represents a problem experienced by a contact.
@Entity
public class Incident extends Model {

	@Id
	public int id;
	@ManyToOne
	public Agent owner;
	
	@Constraints.Required
	public String subject;
	
	public String description;
	public Date startdate;
	public Date enddate;
	public int priority = 3;
	public String status = "Open";
	@ManyToOne
	public Contact requester;
	
	// constructor for new Agent
	public Incident(Agent owner, String subject, String description, 
			Date startdate, Date enddate, int priority, String status, Contact requester) {
			this.owner = owner;
			this.subject = subject;
			this.description = description;
			this.startdate = startdate;
			this.enddate = enddate;
			this.priority = priority;
			this.status = status;
			this.requester = requester;
	}
	
	// creator method
	public static Incident create(String username, String subject, String description, int priority, String status, String email) {
		Date startdate = Calendar.getInstance().getTime();
		Incident incident = new Incident(Agent.find.ref(username), 
				subject, 
				description, 
				startdate,
				startdate,
				priority,
				status,
				Contact.find.ref(email));
		incident.save();
		return incident;
	}
	
	//create a find method for data queries
	public static Finder<Integer, Incident> find = new Finder<Integer, Incident>(
			Integer.class, Incident.class
			);
	
	public static List<Incident> findByOwner(String username) {
        return find.where()
                .eq("owner.username", username)
                .orderBy("priority asc, startdate asc")
                .findList();
	}
	
	public static List<Incident> findUnassigned() {
		return find.where()
				.eq("owner", null)
				.orderBy("priority asc, startdate asc")
				.findList();
	}
	
}
