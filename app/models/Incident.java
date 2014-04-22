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
			this.actions = new ArrayList<Action>();
	}
	
	public Incident addAction(String username, String description) {
		Action newAction = Action.create(username, description, this.id);
		this.actions.add(newAction);
		this.save();
		return this;
		
	}
	
	// close incident by changing the status to closed and setting the enddate
	public static Incident close(int incidentId) {
		Incident incident = find.ref(incidentId);
		incident.status = "Closed";
		incident.enddate = Calendar.getInstance().getTime();
		incident.update();
		Action.create(
				Context.current().request().username(),
				"The ticket was closed", 
				incidentId);
		return incident;
	}
	
	// creator method
	public static Incident create(String username, String subject, String description, int priority, int contactId) {
		Date startdate = Calendar.getInstance().getTime();
		Incident incident = new Incident(Agent.find.ref(username), 
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
	
	public static boolean isOwner(int incidentId, String username) {
		return find.where()
				.eq("id", incidentId)
				.eq("owner.username", username)
				.findRowCount() > 0;
				
	}
	
	// re-open incident
	public static Incident reopen(int incidentId) {
		Incident incident = find.ref(incidentId);
		incident.status = "Open";
		incident.update();
		Action.create(
				Context.current().request().username(),
				"The ticket was re-opened.", 
				incidentId);
		return incident;
	}
	
	public static Incident update(int incidentId, String username, String subject, String description, int priority, int contactId) {
		Incident incident = find.ref(incidentId);
		incident.owner = Agent.find.ref(username);
		incident.subject = subject;
		incident.description = description;
		incident.priority = priority;
		incident.requester = Contact.find.ref(contactId);
		incident.update();
		return incident;
	}
}
