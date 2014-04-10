package controllers;

import models.Incident;
import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render(
        		Incident.find.orderBy("priority asc, startdate asc").findList()
        		
        		));
    }

}
