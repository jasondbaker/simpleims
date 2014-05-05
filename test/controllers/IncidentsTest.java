package controllers;

import org.junit.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.*;

import play.mvc.*;
import play.libs.*;
import play.test.*;
import static play.test.Helpers.*;
import models.*;
import models.Action;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;

public class IncidentsTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        
    	Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("testdata.yml");
    	// Insert agents first
    	Ebean.save(all.get("agents"));
    	
    	// Insert companies 
    	Ebean.save(all.get("companies"));
 
    	// Insert addresses 
    	Ebean.save(all.get("addresses"));
    	
    	// Insert contacts 
    	Ebean.save(all.get("contacts"));

    	// Insert categories 
    	Ebean.save(all.get("categories"));
    	
    	// Insert incidents 
    	Ebean.save(all.get("incidents"));
    	
    	// Insert incidents 
    	Ebean.save(all.get("actions"));
    }

    @Test
    public void getIncidentTest() {
 	   
 	   // first step is to find an existing incident and then use that incident id for the update
 	   Incident testIncident = Incident.find.where().eq("owner_username", "jacksmith").eq("subject", "Forgot password").findUnique();
 	     
 	   Result result = callAction(
 			   controllers.routes.ref.Incidents.get(testIncident.id),
 			   fakeRequest().withSession("username", "jacksmith")); 
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("Forgot password");

    }
  
    @Test
    public void getAllIncidentsTest() {
 	   
 	   Result result = callAction(
 			   controllers.routes.ref.Incidents.getOwned("all"),
 			   fakeRequest().withSession("username", "jacksmith")); 
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("Forgot password");
 	   assertThat(contentAsString(result)).contains("Application error");

    }
    
    @Test
    public void getUnassignedIncidentsTest() {
 	   
 	   Result result = callAction(
 			   controllers.routes.ref.Incidents.getOwned("unassigned"),
 			   fakeRequest().withSession("username", "jacksmith")); 
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("Application database queries are responding slowly");

    }
    
    @Test
    public void getIncidentActionsTest() {

  	   // first step is to find an existing incident and then use that incident id for the update
  	   Incident testIncident = Incident.find.where().eq("owner_username", "jacksmith").eq("subject", "Application error").findUnique();
  
 	   Result result = callAction(
 			   controllers.routes.ref.Incidents.getActions(testIncident.id),
 			   fakeRequest().withSession("username", "jacksmith")); 
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("process to reproduce the application failure");
 	   assertThat(contentAsString(result)).contains("now seeing a different error");
    }
   
   @Test
   public void addIncidentActionTest() {

  	   // first step is to find an existing incident and then use that incident id for the update
  	   Incident testIncident = Incident.find.where().eq("owner_username", "jacksmith").eq("subject", "Application error").findUnique();
  
  	   // add the new action
  	   
  	 ObjectNode json = Json.newObject();
	   
	   json.put("description", "I will reboot the system shortly.");
	   
	   Result result = callAction(
			   controllers.routes.ref.Incidents.addAction(testIncident.id),
			   fakeRequest().withSession("username", "jacksmith")
			   	.withJsonBody(json));
	   
 	   assertEquals(200, status(result));
 	   
 	   // retrieve the list of actions for the test incident
 	   Result result2 = callAction(
 			   controllers.routes.ref.Incidents.getActions(testIncident.id),
 			   fakeRequest().withSession("username", "jacksmith")); 
 	   
 	  assertThat(contentAsString(result2)).contains("reboot the system");
 	   
   }
   
   @Test
   public void newIncidentTest() {
	   
	   ObjectNode json = Json.newObject();
	   json.put("username", "jacksmith");
	   json.put("categoryId", "1");
	   json.put("subject","Storage system alert");
	   json.put("description", "The storage system is emitting an alert sound.");
	   json.put("priority", "2");
	   json.put("contactId", "1");

	   Result result = callAction(
			   controllers.routes.ref.Incidents.add(),
			   fakeRequest().withSession("username", "jacksmith")
			   	.withJsonBody(json)
			   );
	   
			   assertEquals(200, status(result));
			   Incident incident = Incident.find.where().eq("subject", "Storage system alert").findUnique();
			   assertNotNull(incident);
			   assertEquals("The storage system is emitting an alert sound.", incident.description);
			   assertEquals("jack@bigsoftware.com", incident.owner.email);
   }
   
   @Test
   public void updateIncidentTest() {
	   
	   // first step is to find an existing incident and then use that incident id for the update
	   Incident testIncident = Incident.find.where().eq("owner_username", "jacksmith").eq("subject", "Application error").findUnique();
	   
	   
	   ObjectNode json = Json.newObject();
	   
	   json.put("username", testIncident.owner.username);
	   json.put("categoryId", "1");
	   json.put("subject", testIncident.subject);
	   json.put("description", "The storage system is smoking.");
	   json.put("priority", "1");
	   json.put("contactId", testIncident.requester.id);
	   
	   
	   Result result = callAction(
			   controllers.routes.ref.Incidents.update(testIncident.id),
			   fakeRequest().withSession("username", "jacksmith")
			   	.withJsonBody(json));
	   
	   
	   assertEquals(200, status(result));
	   Incident incident = Incident.find.where().eq("subject", "Application error").findUnique();
	   assertNotNull(incident);
	   assertEquals("The storage system is smoking.", incident.description);
	   assertEquals(1, incident.priority);
   }
   
   
   @Test
   public void closeIncident() {
	   int testIncidentId = Incident.find.where().eq("owner.username", "jacksmith").eq("subject", "Application error").findUnique().id;
	    
	   Result result = callAction(
			   controllers.routes.ref.Incidents.close(testIncidentId),
			   fakeRequest().withSession("username", "jacksmith")
			   );
	   
	   Incident closedIncident = Incident.find.byId(testIncidentId);
	   assertEquals("Closed", closedIncident.status);
   }
   
   @Test
   public void reopenIncident() {
	   int testIncidentId = Incident.find.where().eq("owner.username", "jacksmith").eq("subject", "Application error").findUnique().id;
	   
	   // request to close the incident
	   Result result = callAction(
			   controllers.routes.ref.Incidents.close(testIncidentId),
			   fakeRequest().withSession("username", "jacksmith")
			   );
	   
	   assertEquals(200, status(result));
	   
	   Incident closedIncident = Incident.find.byId(testIncidentId);
	   assertEquals("Closed", closedIncident.status);
	   
	   // request to reopen incident
	   result = callAction(
			   controllers.routes.ref.Incidents.reopen(testIncidentId),
			   fakeRequest().withSession("username", "jacksmith")
			   );

	   assertEquals(200, status(result));
	   	
	   Incident reopenedIncident = Incident.find.byId(testIncidentId);
	   assertEquals("Open", reopenedIncident.status);
	   assertEquals(1, reopenedIncident.priority);
	   
   }
    
   @Test
   public void deleteIncidentTest() {
	   
	   // first step is to find an existing incident and then use that incident id for the delete
	   int testIncidentId = Incident.find.where().eq("owner.username", "jacksmith").eq("subject", "Application error").findUnique().id;
	     
	   Result result = callAction(
			   controllers.routes.ref.Incidents.delete(testIncidentId),
			   fakeRequest().withSession("username", "jacksmith")
			   );
	   assertEquals(200, status(result));
	   Incident incident = Incident.find.where().eq("id", testIncidentId).findUnique();
	   assertNull(incident);
	   
	   Action action = Action.find.where().eq("incident.id", testIncidentId).findUnique();
	   assertNull(action);
   }
   
}