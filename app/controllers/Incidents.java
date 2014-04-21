package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;
import views.html.*;
import play.libs.Json;

@Security.Authenticated(Authenticated.class)
public class Incidents extends Controller {

	// add a new incident
	public static Result add() {
		
		Incident newIncident = Incident.create(
				form().bindFromRequest().get("username"),
				form().bindFromRequest().get("subject"),
				form().bindFromRequest().get("description"),
				Integer.parseInt(form().bindFromRequest().get("priority")),
				form().bindFromRequest().get("status"),
				Integer.parseInt(form().bindFromRequest().get("contactId")));
		
		return ok();
	}

	// delete an existing incident based on the incident id
	public static Result delete(int incident) {
		if (Authenticated.isOwnerOf(incident)) {
			Incident.find.ref(incident).delete();
			return ok();
				
		} else {
			return forbidden();
		}
	}
	
	// update an existing incident based on the incident id
	public static Result update(int incident) {
		if (Authenticated.isOwnerOf(incident)) {
			return ok(
					Incident.update(
						incident,
						form().bindFromRequest().get("username"),
						form().bindFromRequest().get("subject"),
						form().bindFromRequest().get("description"),
						Integer.parseInt(form().bindFromRequest().get("priority")),
						Integer.parseInt(form().bindFromRequest().get("contactId"))
							)
					);
					
		} else {
			return forbidden();
			
		}
	}
	
	// get a list of all incidents owned by the user
	public static Result getOwned() {
		return ok(Json.toJson(Incident.findByOwner(request().username())));
	}
	
	// get information for a specific incident owned by the user
	public static Result get(Integer id) {
		if (Authenticated.isOwnerOf(id)) {
			
		return ok(Json.toJson(Incident.find.byId(id)));
		} else {
			return forbidden();
		}
	}
	
	// get all incidents that have no owner
	public static Result getUnassigned() {
		return ok(Json.toJson(Incident.findUnassigned()));
	}
}