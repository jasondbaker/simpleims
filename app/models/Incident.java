package models;

import javax.persistence.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import play.data.format.*;
import play.data.validation.*;
import play.mvc.Http.Context;

import java.util.*;

// An Incident represents a problem experienced by a contact.
@Entity
public class Incident extends Model {

	@Id
	public int id;
	@ManyToOne
	public Agent owner;
	
	@ManyToOne
	public Category category;
	
	@Constraints.Required
	public String subject;
	
	public String description;
	public Date startdate;
	public Date enddate;
	public int priority = 3;
	public String status = "Open";
	@ManyToOne
	public Contact requester;
	
	@OneToMany(mappedBy="incident", cascade=CascadeType.ALL) 
	@JsonManagedReference
	public List<Action> actions;
	
	// constructor for new incident
	public Incident(Agent owner, Category category, String subject, String description, 
			Date startdate, Date enddate, int priority, String status, Contact requester) {
			this.owner = owner;
			this.category = category;
			this.subject = subject;
			this.description = description;
			this.startdate = startdate;
			this.enddate = enddate;
			this.priority = priority;
			this.status = status;
			this.requester = requester;
			this.actions = new ArrayList<Action>();
	}
	
	// close incident by changing the status to closed and setting the enddate
	public static Incident close(int incidentId, String username) {
		Incident incident = find.ref(incidentId);
		incident.status = "Closed";
		incident.enddate = Calendar.getInstance().getTime();
		incident.update();
		Action.create(
				username,
				"The incident was closed.", 
				incidentId);
		return incident;
	}
	
	// creator method
	public static Incident create(String username, int categoryId, String subject, String description, int priority, int contactId) {
		Date startdate = Calendar.getInstance().getTime();
		Incident incident = new Incident(Agent.find.ref(username), 
				Category.find.byId(categoryId),
				subject, 
				description, 
				startdate,
				startdate,
				priority,
				"Open",
				Contact.find.ref(contactId));
		incident.save();
		return incident;
	}
	
	//create a find method for data queries
	public static Finder<Integer, Incident> find = new Finder<Integer, Incident>(
			Integer.class, Incident.class
			);
	
	
	public static boolean isOwner(int incidentId, String username) {
		return find.where()
				.eq("id", incidentId)
				.eq("owner.username", username)
				.findRowCount() > 0;
				
	}
	
	// re-open incident
	public static Incident reopen(int incidentId, String username) {
		Incident incident = find.ref(incidentId);
		incident.status = "Open";
		// set re-opened incidents to the highest priority
		incident.priority = 1;
		incident.update();
		Action.create(
				username,
				"The incident was re-opened.", 
				incidentId);
		return incident;
	}
	
	public static Incident update(int incidentId, String username, int categoryId, String subject, String description, int priority, int contactId) {
		Incident incident = find.ref(incidentId);
		incident.owner = Agent.find.ref(username);
		incident.category = Category.find.byId(categoryId);
		incident.subject = subject;
		incident.description = description;
		incident.priority = priority;
		incident.requester = Contact.find.ref(contactId);
		incident.update();
		return incident;
	}
}
