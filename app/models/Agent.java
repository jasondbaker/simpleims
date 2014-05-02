package models;

import javax.persistence.*;

import play.data.validation.Constraints;
import play.db.ebean.*;
import org.mindrot.jbcrypt.BCrypt;

import com.avaje.ebean.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

// An Agent represents the system user responsible for documenting and managing incident requests.
@Entity
public class Agent extends Model {

	@Id
	@Constraints.Required
	public String username;
	@JsonIgnore
	public String password;
	public String fullname;
	public String email;
	public Boolean active;
	
	// constructor for new Agent
	public Agent(String username, String password, String fullname, String email) {
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.email = email;
		this.active = true;
	}
	
	//create a find method for data queries
	public static Finder<String, Agent> find = new Finder<String, Agent>(
			String.class, Agent.class
			);
	
	public static Agent authenticate(String username, String password) {
		Agent agent = find.where().eq("username", username).eq("active",  "true").findUnique();
		if (agent != null && BCrypt.checkpw(password, agent.password)){
			return agent;
		} else {
			return null;
		}
	}
}
