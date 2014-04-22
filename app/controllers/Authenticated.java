package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import models.*;
import views.html.*;

import play.mvc.Http.*;



public class Authenticated extends Security.Authenticator {

	// get the username of the user from the current session variable
    @Override  
    public String getUsername(Context ctx) {
    	
    	//see if user is logged in
    	if (ctx.session().get("username") == null) return null;
        return ctx.session().get("username");
    }

    // check to see if the current user is an owner of an incident
    public static boolean isOwnerOf(int incidentId) {
    	return Incident.isOwner(
    			incidentId,
    			Context.current().request().username()
    			);
    }
    
    // route the user to a login page if they are not authorized
    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Application.login());
    }
    
}