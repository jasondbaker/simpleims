package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;

import models.*;
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
		JsonNode jsonSubject = json.get("subject");
		JsonNode jsonDescription = json.get("description");
		JsonNode jsonPriority = json.get("priority");
		JsonNode jsonContactId = json.get("contactId");
		
		// create a new incident based on the json values
		Incident newIncident = Incident.create(
				jsonUsername.asText(),
				jsonSubject.asText(),
				jsonDescription.asText(),
				jsonPriority.asInt(),
				jsonContactId.asInt()
				);
		
		// return the created incident back to the user in json
		return ok(Json.toJson(newIncident));
		
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
		if (Authenticated.isOwnerOf(incident)) {
			
			// retrieve json from the request body
			JsonNode json = request().body().asJson();
		
			// slice up the json and store values in individual variables
			JsonNode jsonUsername = json.get("username");
			JsonNode jsonSubject = json.get("subject");
			JsonNode jsonDescription = json.get("description");
			JsonNode jsonPriority = json.get("priority");
			JsonNode jsonContactId = json.get("contactId");
			
			// update incident based on the json values
			Incident updateIncident = Incident.update(
					incident,
					jsonUsername.asText(),
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