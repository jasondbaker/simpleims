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
public class Agents extends Controller {
	
	// get a list of all agents
	public static Result list() {
		return ok(Json.toJson(Agent.find.all()));
	}
		
}
