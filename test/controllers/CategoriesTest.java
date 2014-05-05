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

public class CategoriesTest extends WithApplication {
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
    public void getCategoryTest() {
 	   
 	   // request information on a specific agent	   
 	   Result result = callAction(
 			   controllers.routes.ref.Categories.get(2),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("Service unavailable");

    }
    
    @Test
    public void getAllCategoriesTest() {
 	   
 	   Result result = callAction(
 			   controllers.routes.ref.Categories.list(),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("Bug report");
 	   assertThat(contentAsString(result)).contains("Feature request");

    }

}
