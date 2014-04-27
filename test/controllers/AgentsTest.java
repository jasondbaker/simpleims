package controllers;

import org.junit.*;

import static org.junit.Assert.*;

import java.util.*;

import play.mvc.*;
import play.libs.*;
import play.test.*;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import models.*;
import models.Action;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;

public class AgentsTest extends WithApplication {
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
    public void getAgentTest() {
 	   
 	   // request information on a specific agent	   
 	   Result result = callAction(
 			   controllers.routes.ref.Agents.get("jacksmith"),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("Jack Smith");

    }
    
    @Test
    public void getAllAgentsTest() {
 	   
 	   Result result = callAction(
 			   controllers.routes.ref.Agents.list(),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("jacksmith");
 	   assertThat(contentAsString(result)).contains("benj");

    }
    
    @Test
    public void getCurrentAgentTest() {
  	   Result result = callAction(
 			   controllers.routes.ref.Agents.current(),
 			   fakeRequest().withSession("username", "jacksmith"));
  	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("Jack Smith");
    }
    
    @Test
    public void getAgentIncidentsTest() {
  	   Result result = callAction(
 			   controllers.routes.ref.Agents.getIncidents("jacksmith"),
 			   fakeRequest().withSession("username", "jacksmith"));
  	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("Application error");
 	   assertThat(contentAsString(result)).contains("Forgot password");
    }

}
