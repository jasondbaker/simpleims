package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import models.*;
import views.html.*;

public class Application extends Controller {

	public static Result authenticate() {
		Form<Login> req = form(Login.class).bindFromRequest();
		if (req.hasErrors()) {
			return badRequest(login.render(req));
		} else {
			session().clear();
			session("username", req.get().username);
			return redirect(routes.Application.index());
		}
	}
	
	// display the main page
	@Security.Authenticated(Authenticated.class)
    public static Result index() {
        return ok(index.render(
        		Agent.find.byId(request().username())
        		));
    }
    
	// display the login page
    public static Result login() {
    	return ok(
    			login.render(form(Login.class))
    			);
    }

   // logout user session
    public static Result logout() {
    	session().clear();
    	flash("success", "You have been logged out.");
    	return redirect(routes.Application.login());
    }
    
    public static class Login {
    	public String username;
    	public String password;
    	
    	public String validate() {
    		if (Agent.authenticate(username, password) == null) {
    			return "Invalid username or password";
    		}
    		return null;
    	}
    }
    
}
