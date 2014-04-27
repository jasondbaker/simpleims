package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.Context;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;
import views.html.*;
import play.libs.Json;

@Security.Authenticated(Authenticated.class)
public class Agents extends Controller {

	// get info on the currently logged in agent
	public static Result current() {
		return ok(Json.toJson(Agent.find.byId(Context.current().request().username())));
	}
	
	// get info for a specific agent
	public static Result get(String username) {
		return ok(Json.toJson(Agent.find.byId(username)));
	}

	// get incidents associated with a specific agent
	public static Result getIncidents(String username) {
		return ok(Json.toJson(Incident.find.where().eq("owner_username", username).findList()));
	}
	
	// get a list of all active agents
	public static Result list() {
		return ok(Json.toJson(Agent.find.where().eq("active", true).findList()));
	}
	
}
