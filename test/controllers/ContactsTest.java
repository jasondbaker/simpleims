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

public class ContactsTest extends WithApplication {
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
    public void getContactTest() {
 	   
 	   // first step is to find an existing contact and then use that contact id for the test
 	   Contact testContact = Contact.find.where().eq("fullname", "Bill Richards").findUnique();
 	   
 	   Result result = callAction(
 			   controllers.routes.ref.Contacts.get(testContact.id),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("Bill Richards");

    }
    
    @Test
    public void getAllContactsTest() {
 	   
 	   Result result = callAction(
 			   controllers.routes.ref.Contacts.list("_all"),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("Bill Richards");
 	  assertThat(contentAsString(result)).contains("Beth Goodwin");

    }
    
    @Test
    public void getSearchContactsTest() {
 	   
 	   Result result = callAction(
 			   controllers.routes.ref.Contacts.list("beth"),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).doesNotContain("Bill Richards");
 	  assertThat(contentAsString(result)).contains("Beth Goodwin");

    }
    
    @Test
    public void getContactIncidentsTest() {
 	   
 	   // first step is to find an existing contact and then use that contact id for the test
 	   Contact testContact = Contact.find.where().eq("fullname", "Beth Goodwin").findUnique();
 	   
 	   Result result = callAction(
 			   controllers.routes.ref.Contacts.getIncidents(testContact.id, "any"),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("Forgot password");

    }
    
    @Test
    public void getContactCompaniesTest() {
 	   
 	   // first step is to find an existing contact and then use that contact id for the test
 	   Contact testContact = Contact.find.where().eq("fullname", "Tanya Goldberg").findUnique();
 	   
 	   Result result = callAction(
 			   controllers.routes.ref.Contacts.getCompanies(testContact.id),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("The Widget Factory");

    }
    
    @Test
    public void updateContactTest() {
 	   
 	   // first step is to find an existing contact and then use that contact id for the update
 	   Contact testContact = Contact.find.where().eq("fullname", "Bill Richards").findUnique();
 	   
 	   ObjectNode json = Json.newObject();
 	   
 	   json.put("email", testContact.email);
 	   json.put("fullname", "William Richards");
 	   json.put("phone", testContact.phone);
	   
 	   Result result = callAction(
 			   controllers.routes.ref.Contacts.update(testContact.id),
 			   fakeRequest().withSession("username", "jacksmith")
 			   	.withJsonBody(json));
 	   
 	   
 	   assertEquals(200, status(result));
 	   Contact contact = Contact.find.where().eq("fullname", "William Richards").findUnique();
 	   assertNotNull(contact);

    }

    @Test
    public void deleteContactTest() {
 	   
 	   // first step is to find an existing contact and then use that contact id for the update
 	   Contact testContact = Contact.find.where().eq("fullname", "Bill Richards").findUnique();
	   
 	   // request to delete the contact
 	   Result result = callAction(
 			   controllers.routes.ref.Contacts.delete(testContact.id),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   assertEquals(200, status(result));
 	  
 	   // request the list of all contacts
 	   Result result2 = callAction(
			   controllers.routes.ref.Contacts.list("_all"),
			   fakeRequest().withSession("username", "jacksmith"));
	   
	   // check to see if this contact was removed (de-activated)
	   assertEquals(200, status(result2));
	   assertThat(contentAsString(result2)).doesNotContain("Bill Richards");
 	   
    }

}