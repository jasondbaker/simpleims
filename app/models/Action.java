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
public class Action extends Model {
	
	@Id
	public int id;
	public Date startdate;
	@ManyToOne
	public Agent agent;
	public String description;
	@ManyToOne
	@JsonBackReference
	public Incident incident;
	
	// constructor methods
	public Action(Date startdate, Agent agent, String description, Incident incident){
		this.startdate = startdate;
		this.agent = agent;
		this.description = description;
		this.incident = incident;
	}
	
	public static Action create(String agentUsername, String description, int incidentId){
		Date startdate = Calendar.getInstance().getTime();
		Action action = new Action(
				startdate,
				Agent.find.ref(agentUsername), 
				description, 
				Incident.find.ref(incidentId));
		action.save();
		return action;
	}
	
	//create a find method for data queries
	public static Finder<Integer, Action> find = new Finder<Integer, Action>(
			Integer.class, Action.class
			);
	
}
