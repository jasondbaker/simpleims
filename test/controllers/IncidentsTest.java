package controllers;

import org.junit.*;

import static org.junit.Assert.*;

import java.util.*;

import play.mvc.*;
import play.libs.*;
import play.test.*;
import static play.test.Helpers.*;

import models.*;

import com.avaje.ebean.Ebean;
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

    	// Insert incidents 
    	Ebean.save(all.get("incidents"));
    	
    	// Insert incidents 
    	Ebean.save(all.get("actions"));
    }
    
   @Test
   public void newIncident() {
	   Result result = callAction(
			   controllers.routes.ref.Incidents.add(),
			   fakeRequest().withSession("username", "jacksmith")
			   	.withFormUrlEncodedBody(
			   			new ImmutableMap.Builder<String, String>()
			   			.put("username", "jacksmith")
			   			.put("subject", "Storage system alert")
			   			.put("description", "The storage system is emitting an alert sound.")
			   			.put("priority", "2")
			   			.put("status", "Open")
			   			.put("email", "brichards@acme.com")
			   			.build())
			   );
			   assertEquals(200, status(result));
			   Incident incident = Incident.find.where().eq("subject", "Storage system alert").findUnique();
			   assertNotNull(incident);
			   assertEquals("The storage system is emitting an alert sound.", incident.description);
			   assertEquals("jack@bigsoftware.com", incident.owner.email);
   }
    
}