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
public class Companies extends Controller {


	// delete an existing company based on the company id
	public static Result delete(int id) {

			Company.find.byId(id).delete();
			return ok();
	}
	
	// get a list of all companies
	public static Result list() {
		return ok(Json.toJson(Company.find.all()));
	}
	
	// get information for a specific company
	public static Result get(Integer id) {
			
		return ok(Json.toJson(Company.find.byId(id)));

	}
	
	// get contacts for a specific company
	public static Result getContacts(Integer id) {
		return ok(Json.toJson(Contact.find.where().eq("company_id", id).findList()));
	}
	
	// update an existing company based on the company id
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(int id) {

		// retrieve json from the request body
		JsonNode json = request().body().asJson();
	
		// slice up the json and store values in individual variables
		JsonNode jsonName = json.get("name");
		JsonNode jsonNotes = json.get("notes");
		
		// update company based on the json values
		Company updateCompany = Company.update(
				id,
				jsonName.asText(),
				jsonNotes.asText()
				);
		
		return ok(Json.toJson(updateCompany));
			
	}
}
