package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.*;
import models.Action;
import views.html.*;
import play.libs.Json;

@Security.Authenticated(Authenticated.class)
public class Reports extends Controller {
	
	// get a list of all tickets created in past X days
	public static Result getIncidents(int days) {
		
		Calendar reportdate = Calendar.getInstance();
		reportdate.add(Calendar.DATE, -days);
			
		return ok(Json.toJson(
				
				Incident.find.where()
                .gt("startdate", reportdate.getTime())
                .orderBy("startdate asc")
                .findList()
                
				));
			
		
	}
	
	// get some basic current statistics
	public static Result getStats(){
		
		Calendar reportdate = Calendar.getInstance();
		reportdate.add(Calendar.DATE, -1);

		int closedIncidentsNew = Incident.find.where()
				.eq("status", "Closed")
				.gt("startdate", reportdate.getTime())
				.findRowCount();
		
		int openIncidentsNew = Incident.find.where()
				.eq("status", "Open")
				.gt("startdate", reportdate.getTime())
				.findRowCount();
		
		int openIncidentsTotal = Incident.find.where()
				.eq("status", "Open")
				.findRowCount();
		
		int priority1 = Incident.find.where()
				.eq("status", "Open")
				.eq("priority", "1")
				.findRowCount();
		
		int priority2 = Incident.find.where()
				.eq("status", "Open")
				.eq("priority", "2")
				.findRowCount();
		
		int priority3 = Incident.find.where()
				.eq("status", "Open")
				.eq("priority", "3")
				.findRowCount();
		
		// store all the stats in JSON
		ObjectNode json = Json.newObject();
		json.put("closedNew", closedIncidentsNew);
		json.put("openNew", openIncidentsNew);
		json.put("openTotal", openIncidentsTotal);
		json.put("priority1", priority1);
		json.put("priority2", priority2);
		json.put("priority3", priority3);
		
		   
		return ok(json);
	}
	
}
