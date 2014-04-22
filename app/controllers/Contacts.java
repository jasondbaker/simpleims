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
public class Contacts extends Controller {


	// delete an existing contact based on the contact id
	public static Result delete(int id) {

			if (Contact.delete(id)) {
				return ok();
			} else {
				return badRequest();
			}
	
	}
		
	// update an existing contact based on the contact id
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(int id) {

		// retrieve json from the request body
		JsonNode json = request().body().asJson();
	
		// slice up the json and store values in individual variables
		JsonNode jsonEmail = json.get("email");
		JsonNode jsonFullname = json.get("fullname");
		JsonNode jsonPhone = json.get("phone");
		
		// update contact based on the json values
		Contact updateContact = Contact.update(
				id,
				jsonEmail.asText(),
				jsonFullname.asText(),
				jsonPhone.asText()
				);
		
		return ok(Json.toJson(updateContact));
			
	}
	
	// get a list of all contacts (active)
	public static Result list() {
		return ok(Json.toJson(Contact.find.where().eq("active", true).findList()));
	}
	
	// get information for a specific contact
	public static Result get(Integer id) {
			
		return ok(Json.toJson(Contact.find.byId(id)));

	}
	
	// get incidents associated with a specific contact
	public static Result getIncidents(Integer id) {
			
		return ok(Json.toJson(Incident.find.where().eq("requester_id", id).findList()));

	}
}
