package models;

import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;

// An Agent represents the system user responsible for documenting and managing incident requests.
@Entity
public class Agent extends Model {

	@Id
	public String username;
	public String password;
	public String fullname;
	public String email;
	
	// constructor for new Agent
	public Agent(String username, String password, String fullname, String email) {
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.email = email;
	}
	
	//create a find method for data queries
	public static Finder<String, Agent> find = new Finder<String, Agent>(
			String.class, Agent.class
			);
	
	public static Agent authenticate(String username, String password) {
		return find.where().eq("username", username).eq("password", password).findUnique();
	}
}
