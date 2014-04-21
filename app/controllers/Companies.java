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
public class Companies extends Controller {


	// delete an existing company based on the company id
	public static Result delete(int id) {

			Company.find.byId(id).delete();
			return ok();
	}
	
	// update an existing company based on the company id
	public static Result update(int id) {
			return ok(
					Company.update(
						id,
						form().bindFromRequest().get("name"),
						form().bindFromRequest().get("notes")
							)
					);

	}
	
	// get a list of all companies
	public static Result list() {
		return ok(Json.toJson(Company.find.all()));
	}
	
	// get information for a specific company
	public static Result get(Integer id) {
			
		return ok(Json.toJson(Company.find.byId(id)));

	}
	
}
