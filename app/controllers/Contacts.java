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
public class Contacts extends Controller {


	// delete an existing contact based on the contact email
	public static Result delete(int id) {

			Contact.find.byId(id).delete();
			return ok();
	}
	
	// update an existing contact based on the contact email
	public static Result update(int id) {
			return ok(
					Contact.update(
						id,
						form().bindFromRequest().get("email"),
						form().bindFromRequest().get("fullname"),
						form().bindFromRequest().get("phone")
							)
					);

	}
	
	// get a list of all contacts
	public static Result list() {
		return ok(Json.toJson(Contact.find.all()));
	}
	
	// get information for a specific contact
	public static Result get(Integer id) {
			
		return ok(Json.toJson(Contact.find.byId(id)));

	}
	
}
