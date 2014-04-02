package models;

import javax.persistence.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.*;

import java.util.*;

// A Ticket represents a single support incident.
@Entity
public class Ticket extends Model {

	@Id
	public int id;
	@ManyToOne
	public Agent owner;
	public String subject;
	public String description;
	public Date startdate;
	public Date enddate;
	public int priority;
	public String status;
	@ManyToOne
	public Contact requester;
	
	// constructor for new Agent
	public Ticket(Agent owner, String subject, String description, 
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
	public static Ticket create(String agentname, String subject, String description, int priority, String status, String requestername) {
		Date startdate = Calendar.getInstance().getTime();
		Ticket ticket = new Ticket(Agent.find.ref(agentname), 
				subject, 
				description, 
				startdate,
				startdate,
				priority,
				status,
				Contact.find.ref(requestername));
		ticket.save();
		return ticket;
	}
	
	//create a find method for data queries
	public static Finder<Integer, Ticket> find = new Finder<Integer, Ticket>(
			Integer.class, Ticket.class
			);
	
	
}
