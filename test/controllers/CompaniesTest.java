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

public class CompaniesTest extends WithApplication {
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
    public void getCompanyTest() {
 	   
 	   // first step is to find an existing contact and then use that contact id for the test
 	   Company testCompany = Company.find.where().eq("name", "Acme Corporation").findUnique();
 	   
 	   Result result = callAction(
 			   controllers.routes.ref.Companies.get(testCompany.id),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("Acme Corporation");

    }
    
    @Test
    public void getAllCompaniesTest() {
 	   
 	   Result result = callAction(
 			   controllers.routes.ref.Companies.list(),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("Acme Corporation");
 	   assertThat(contentAsString(result)).contains("The Widget Factory");

    }

    @Test
    public void getCompanyContactsTest() {

  	   // first step is to find an existing contact and then use that contact id for the test
  	   Company testCompany = Company.find.where().eq("name", "Acme Corporation").findUnique();
  	   
 	   Result result = callAction(
 			   controllers.routes.ref.Companies.getContacts(testCompany.id),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("Bill Richards");
 	   assertThat(contentAsString(result)).doesNotContain("Tanya Goldberg");

    }
 
    @Test
    public void addCompanyContactTest() {
 	   
 	   // first step is to find an existing contact and then use that contact id for the update
 	   Company testCompany = Company.find.where().eq("name", "The Widget Factory").findUnique();
 	   
 	   ObjectNode json = Json.newObject();
 	   
 	   json.put("email", "ryan@thewidgetfactory.com");
 	   json.put("fullname", "Ryan Robertson");
 	   json.put("phone", "712-333-4523");
	   
 	   Result result = callAction(
 			   controllers.routes.ref.Companies.addContacts(testCompany.id),
 			   fakeRequest().withSession("username", "jacksmith")
 			   	.withJsonBody(json));
 	   
 	   
 	   assertEquals(200, status(result));
 	   
 	   Result result2 = callAction(
 			   controllers.routes.ref.Companies.getContacts(testCompany.id),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   assertEquals(200, status(result2));
 	   assertThat(contentAsString(result2)).contains("Ryan Robertson");

    }
    
    @Test
    public void updateCompanyTest() {
 	   
 	   // first step is to find an existing contact and then use that contact id for the update
 	   Company testCompany = Company.find.where().eq("name", "Acme Corporation").findUnique();
 	   
 	   ObjectNode json = Json.newObject();
 	   
 	   json.put("name", testCompany.name);
 	   json.put("notes", "Test note");
	   
 	   Result result = callAction(
 			   controllers.routes.ref.Companies.update(testCompany.id),
 			   fakeRequest().withSession("username", "jacksmith")
 			   	.withJsonBody(json));
 	   
 	   
 	   assertEquals(200, status(result));
 	   Company company = Company.find.where().eq("name", "Acme Corporation").findUnique();
 	   assertNotNull(company);
 	   assertEquals("Test note", company.notes);

    }


}