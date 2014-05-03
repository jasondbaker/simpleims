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
	public static Result list(String search) {
		
		// modify the query based on the search string
		if (search.equals("_all")) {
			return ok(Json.toJson(Contact.find.where().eq("active", true).orderBy("fullname").findList()));
		} else {
			return ok(Json.toJson(Contact.find.where().eq("active", true).icontains("fullname", search).findList()));
		}
		
	}
	
	// get information for a specific contact
	public static Result get(Integer id) {
			
		return ok(Json.toJson(Contact.find.byId(id)));

	}

	// get companies associated with a specific contact
	public static Result getCompanies(Integer id) {
			
		return ok(Json.toJson(Company.find.where().eq("contacts.id", id).findList()));

	}
	
	// get incidents associated with a specific contact based on status
	public static Result getIncidents(Integer id, String status) {
		
		if (status.equals("any")) {
			
			return ok(Json.toJson(Incident.find.where().eq("requester_id", id).orderBy("status desc, priority asc, startdate asc").findList()));
		} else {
			return ok(Json.toJson(Incident.find.where().eq("requester_id", id).eq("status", status).orderBy("priority asc, startdate asc").findList()));
		}

	}
}
