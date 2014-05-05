package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;

import models.*;
import models.Action;
import views.html.*;
import play.libs.Json;

@Security.Authenticated(Authenticated.class)
public class Incidents extends Controller {

	
	@BodyParser.Of(BodyParser.Json.class)
	public static Result add() {
		// retrieve json from the request body
		JsonNode json = request().body().asJson();
	
		// slice up the json and store values in individual variables
		JsonNode jsonUsername = json.get("username");
		JsonNode jsonCategory = json.get("categoryId");
		JsonNode jsonSubject = json.get("subject");
		JsonNode jsonDescription = json.get("description");
		JsonNode jsonPriority = json.get("priority");
		JsonNode jsonContactId = json.get("contactId");
		
		// create a new incident based on the json values
		Incident newIncident = Incident.create(
				jsonUsername.asText(),
				jsonCategory.asInt(),
				jsonSubject.asText(),
				jsonDescription.asText(),
				jsonPriority.asInt(),
				jsonContactId.asInt()
				);
		
		// return the created incident back to the user in json
		return ok(Json.toJson(newIncident));
		
	}
	
	// add an action to an incident
	@BodyParser.Of(BodyParser.Json.class)
	public static Result addAction(int incidentId) {
		JsonNode json = request().body().asJson();
		
		// slice up the json and store values in individual variables
		JsonNode jsonDescription = json.get("description");
				
		Action newAction = Action.create(request().username(), jsonDescription.asText(), incidentId);
		return ok(Json.toJson(newAction));
	}
	
	
	// close an incident based on the incident id
	public static Result close(int incidentId) {
		if (Authenticated.isOwnerOf(incidentId)) {
			Incident.close(incidentId, request().username());
			return ok();
		} else {
			return forbidden();
		}
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

	// get a list of all actions associated with an incident
	public static Result getActions(Integer id) {
		return ok(Json.toJson(Action.find.where().eq("incident_id",id).findList()));
	}
	
	// get a list of all incidents owned by the user
	public static Result getOwned(String status) {
		
		// modify the incident list query based on the status request parameter
		if (status.equals("all")) {
			return ok(Json.toJson(Incident.find.where()
	                .eq("owner.username", request().username())
	                .orderBy("priority asc, startdate asc")
	                .findList()));
			
		} else if (status.equals("open")) {
		
			return ok(Json.toJson(Incident.find.where().eq("owner.username", request().username()).eq("status", "Open").orderBy("priority asc, startdate asc").findList()));
		} else if (status.equals("closed")) {
			return ok(Json.toJson(Incident.find.where().eq("owner.username", request().username()).eq("status", "Closed").orderBy("startdate asc").findList()));		
		} else if (status.equals("unassigned")) {
			return ok(Json.toJson(Incident.find.where()
					.eq("owner.username", "unassigned")
					.orderBy("priority asc, startdate asc")
					.findList()));
		}
			else {
			return badRequest();
		}
		
	}
	
	// get information for a specific incident owned by the user
	public static Result get(Integer id) {
			
		return ok(Json.toJson(Incident.find.byId(id)));
	}
	
	// close an incident based on the incident id
	public static Result reopen(int incidentId) {
		if (Authenticated.isOwnerOf(incidentId)) {
			Incident.reopen(incidentId, request().username());
			return ok();
		} else {
			return forbidden();
		}
	}
	
	// update an existing incident based on the incident id
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(int incident) {
		// determine the owner of the incident
		Agent agent = Incident.find.byId(incident).owner;
		
		//user is allowed to update the incident if the user is the owner (agent)
		//or the incident is unassigned
		if (Authenticated.isOwnerOf(incident) || agent.username.equals("unassigned")) {
			
			// retrieve json from the request body
			JsonNode json = request().body().asJson();
		
			// slice up the json and store values in individual variables
			JsonNode jsonUsername = json.get("username");
			JsonNode jsonCategory = json.get("categoryId");
			JsonNode jsonSubject = json.get("subject");
			JsonNode jsonDescription = json.get("description");
			JsonNode jsonPriority = json.get("priority");
			JsonNode jsonContactId = json.get("contactId");
			
			// update incident based on the json values
			Incident updateIncident = Incident.update(
					incident,
					jsonUsername.asText(),
					jsonCategory.asInt(),
					jsonSubject.asText(),
					jsonDescription.asText(),
					jsonPriority.asInt(),
					jsonContactId.asInt()
					);
			
			return ok(Json.toJson(updateIncident));
			
					
		} else {
			return forbidden();
			
		}
	}
	
}