package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;

import models.*;

public class Authenticated extends Security.Authenticator {

	// get the username of the user from the current session variable
    @Override
    public String getUsername(Context ctx) {
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