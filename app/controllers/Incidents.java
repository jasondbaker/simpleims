package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;
import views.html.*;
import views.html.incidents.*;
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
				form().bindFromRequest().get("email"));
		
		return ok(incident.render(newIncident));
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
						form().bindFromRequest().get("email")
							)
					);
					
		} else {
			return forbidden();
			
		}
	}
	
	public static Result getOwned() {
		return ok(Json.toJson(Incident.findByOwner(request().username())));
	}
	
	public static Result get(Integer id) {
		return ok(Json.toJson(Incident.find.byId(id)));
	}
	
	public static Result getUnassigned() {
		return ok(Json.toJson(Incident.findUnassigned()));
	}
}