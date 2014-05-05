package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.Context;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import models.*;
import views.html.*;
import play.libs.Json;

@Security.Authenticated(Authenticated.class)
public class Categories extends Controller {

	
	// get a list of all categories
	public static Result list() {
		return ok(Json.toJson(Category.find.all()));
	}
	
	// get a specific category
	public static Result get(int id) {
		return ok(Json.toJson(Category.find.byId(id)));
	}
}