package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

import models.*;
import views.html.*;
import play.libs.Json;

@Security.Authenticated(Authenticated.class)
public class Companies extends Controller {

	// add a new company
	// note: a new company request must include one address
	@BodyParser.Of(BodyParser.Json.class)
	public static Result add() {
		// retrieve json from the request body
		JsonNode json = request().body().asJson();
	
		// slice up the json and store values in individual variables
		JsonNode jsonName = json.get("name");
		JsonNode jsonNotes = json.get("notes");
		JsonNode jsonWebsite = json.get("website");
		JsonNode jsonAddress1 = json.get("address1");
		JsonNode jsonAddress2 = json.get("address2");
		JsonNode jsonCity = json.get("city");
		JsonNode jsonState = json.get("state");
		JsonNode jsonZipcode = json.get("zipcode");
		
		// create a new company based on the json values
		Company newCompany = Company.create(
				jsonName.asText(),
				jsonNotes.asText(),
				jsonWebsite.asText()
				);
		
		//create an address associated with the new company
		Address newAddress = Address.create(
				jsonAddress1.asText(), 
				jsonAddress2.asText(), 
				jsonCity.asText(), 
				jsonState.asText(), 
				jsonZipcode.asText(), 
				newCompany.id);
		
		// retrieve the new company record to present both company and address via json
		Company finalCompany = Company.find.byId(newCompany.id);
		
		// return the created incident back to the user in json
		if (finalCompany == null) {
			return badRequest();
		} else {
			return ok(Json.toJson(finalCompany));
		}
		
	}
	
	// add a contact associated with a company
	@BodyParser.Of(BodyParser.Json.class)
	public static Result addContacts(Integer id) {
		// retrieve json from the request body
		JsonNode json = request().body().asJson();
	
		// slice up the json and store values in individual variables
		JsonNode jsonEmail = json.get("email");
		JsonNode jsonFullname = json.get("fullname");
		JsonNode jsonPhone = json.get("phone");
		
		// create a new contact based on the json values
		Contact newContact = Contact.create(
				jsonEmail.asText(),
				jsonFullname.asText(),
				jsonPhone.asText(),
				id
				);
		
		// return the created incident back to the user in json
		return ok(Json.toJson(newContact));
		
	}
	
	// delete an existing company based on the company id
	// disable the record, don't delete the data because we need it for reporting
	// also, disable the associated contacts
	public static Result delete(int id) {

			// get the list of contacts associated with the company and delete (de-active) them
			List<Contact> contacts = Contact.find.where().eq("company_id", id).findList();
			for (Contact contact : contacts) {
				Contact.delete(contact.id);
			}
			
			if(Company.delete(id)) {
				return ok();
			} else {
				return badRequest();
			}
	}
	
	// get a list of all active companies
	public static Result list(String search) {
		
		// modify the query based on the search string
		if (search.equals("_all")) {
			return ok(Json.toJson(Company.find.where().eq("active", true).orderBy("name").findList()));
		} else {
			return ok(Json.toJson(Company.find.where().eq("active", true).icontains("name", search).findList()));
		}
				
	}
	
	// get information for a specific company
	public static Result get(Integer id) {
			
		return ok(Json.toJson(Company.find.byId(id)));

	}
	
	// get contacts for a specific company
	public static Result getContacts(Integer id) {
		return ok(Json.toJson(Contact.find.where().eq("company_id", id).findList()));
	}
	
	// get incidents associated with a specific company
	public static Result getIncidents(Integer id) {
		
		Company company = Company.find.byId(id);
		
		List<Incident> incidents = Ebean.find(Incident.class)
				.fetch("requester")
				.where()
				.eq("company_id", id)
				.orderBy("status desc, priority asc, startdate asc")
				.findList();
				
		
		return ok(Json.toJson(incidents));
	}
	
	// get addresses associated with a specific company
	public static Result getAddresses(Integer id) {
		return ok(Json.toJson(Address.find.where().eq("company_id", id).findList()));
	}
	
	// update an existing company based on the company id
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(int id) {

		// retrieve json from the request body
		JsonNode json = request().body().asJson();
	
		// slice up the json and store values in individual variables
		JsonNode jsonName = json.get("name");
		JsonNode jsonNotes = json.get("notes");
		JsonNode jsonWebsite = json.get("website");
		
		// update company based on the json values
		Company updateCompany = Company.update(
				id,
				jsonName.asText(),
				jsonNotes.asText(),
				jsonWebsite.asText()
				);
		
		return ok(Json.toJson(updateCompany));
			
	}
	
	// update an existing company address based on the company id & address id
	@BodyParser.Of(BodyParser.Json.class)
	public static Result updateAddress(int id, int id2) {

		// retrieve json from the request body
		JsonNode json = request().body().asJson();
	
		// slice up the json and store values in individual variables
		JsonNode jsonAddress1 = json.get("address1");
		JsonNode jsonAddress2 = json.get("address2");
		JsonNode jsonCity = json.get("city");
		JsonNode jsonState = json.get("state");
		JsonNode jsonZipcode = json.get("zipcode");
		
		// update company based on the json values
		Address updateAddress = Address.update(
				id2,
				jsonAddress1.asText(),
				jsonAddress2.asText(),
				jsonCity.asText(),
				jsonState.asText(),
				jsonZipcode.asText()
				);
		
		return ok(Json.toJson(updateAddress));
			
	}
}
