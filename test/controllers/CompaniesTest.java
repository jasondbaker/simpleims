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

    	// Insert categories 
    	Ebean.save(all.get("categories"));
    	
    	// Insert incidents 
    	Ebean.save(all.get("incidents"));
    	
    	// Insert incidents 
    	Ebean.save(all.get("actions"));
    }
    
    @Test
    public void getCompanyTest() {
 	   
 	   // first step is to find an existing company and then use that company id for the test
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
 			   controllers.routes.ref.Companies.list("_all"),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("Acme Corporation");
 	   assertThat(contentAsString(result)).contains("The Widget Factory");

    }

    @Test
    public void getCompanyContactsTest() {

  	   // first step is to find an existing company and then use that company id for the test
  	   Company testCompany = Company.find.where().eq("name", "Acme Corporation").findUnique();
  	   
 	   Result result = callAction(
 			   controllers.routes.ref.Companies.getContacts(testCompany.id),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("Bill Richards");
 	   assertThat(contentAsString(result)).doesNotContain("Tanya Goldberg");

    }

    @Test
    public void getCompanyAddressesTest() {

  	   // first step is to find an existing company and then use that company id for the test
  	   Company testCompany = Company.find.where().eq("name", "Acme Corporation").findUnique();
  	   
 	   Result result = callAction(
 			   controllers.routes.ref.Companies.getAddresses(testCompany.id),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("123 Way Lane");
 	   assertThat(contentAsString(result)).doesNotContain("Starry Road");

    }

    @Test
    public void getCompanyIncidentsTest() {

  	   // first step is to find an existing company and then use that company id for the test
  	   Company testCompany = Company.find.where().eq("name", "The Widget Factory").findUnique();
  	   
 	   Result result = callAction(
 			   controllers.routes.ref.Companies.getIncidents(testCompany.id),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   
 	   assertEquals(200, status(result));
 	   assertThat(contentAsString(result)).contains("reporting a database failure");
 	   assertThat(contentAsString(result)).doesNotContain("Unexpected application behavior");

    }
    
    @Test
    public void addCompanyContactTest() {
 	   
 	   // first step is to find an existing company and then use that company id for the update
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
    public void addCompanyTest() {
 	   
 	   ObjectNode json = Json.newObject();
 	   
 	   json.put("name", "Standard Products Corp");
 	   json.put("notes", "We sell lots of different products.");
 	   json.put("website", "www.standardproducts.com");
	   json.put("address1", "1234 Door Place");
	   json.put("address2", "");
	   json.put("city", "Kettle");
	   json.put("state", "Montana");
	   json.put("zipcode", "34253");
	   
 	   Result result = callAction(
 			   controllers.routes.ref.Companies.add(),
 			   fakeRequest().withSession("username", "jacksmith")
 			   	.withJsonBody(json));
 	   
 	   
 	   assertEquals(200, status(result));

 	   Company testCompany = Company.find.where().eq("name", "Standard Products Corp").findUnique();
 	   assertNotNull(testCompany);
 	   assertEquals(testCompany.name, "Standard Products Corp");
 	   assertThat(contentAsString(result)).contains("1234 Door Place");
    }
    
    @Test
    public void updateCompanyTest() {
 	   
 	   // first step is to find an existing company and then use that company id for the update
 	   Company testCompany = Company.find.where().eq("name", "Acme Corporation").findUnique();
 	   
 	   ObjectNode json = Json.newObject();
 	   
 	   json.put("name", testCompany.name);
 	   json.put("notes", "Test note");
 	   json.put("website", "www.standardproducts.com");
	   
 	   Result result = callAction(
 			   controllers.routes.ref.Companies.update(testCompany.id),
 			   fakeRequest().withSession("username", "jacksmith")
 			   	.withJsonBody(json));
 	   
 	   
 	   assertEquals(200, status(result));
 	   Company company = Company.find.where().eq("name", "Acme Corporation").findUnique();
 	   assertNotNull(company);
 	   assertEquals("Test note", company.notes);

    }

    @Test
    public void updateCompanyAddressTest() {
 	   
 	   // first step is to find an existing address and then use that address id for the update
 	   Address testAddress = Address.find.byId(1);
 	   
 	   ObjectNode json = Json.newObject();
 	   
 	   json.put("id", testAddress.id);
 	   json.put("address1", "200 Frobel Avenue");
 	   json.put("address2", testAddress.address2);
 	   json.put("city", testAddress.city);
 	   json.put("state", testAddress.state);
 	   json.put("zipcode", testAddress.zipcode);
 	   
 	   Result result = callAction(
 			   controllers.routes.ref.Companies.updateAddress(1, testAddress.id),
 			   fakeRequest().withSession("username", "jacksmith")
 			   	.withJsonBody(json));
 	   
 	   
 	   assertEquals(200, status(result));
 	   Address address = Address.find.byId(1);
 	   assertNotNull(address);
 	   assertEquals("200 Frobel Avenue", address.address1);

    }
    
    @Test
    public void deleteCompanyTest() {
    	
  	   // first step is to find an existing company and then use that company id for the update
  	   Company testCompany = Company.find.where().eq("name", "Standard Products Corporation").findUnique();
  	   
  	   // request to delete the company
 	   Result result = callAction(
 			   controllers.routes.ref.Companies.delete(testCompany.id),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   assertEquals(200, status(result));
 	   
 	   // request a list of active companies
 	   Result result2 = callAction(
 			   controllers.routes.ref.Companies.list("_all"),
 			   fakeRequest().withSession("username", "jacksmith"));
 	   
 	   assertEquals(200, status(result2));
 	   assertThat(contentAsString(result2)).doesNotContain("Standard Products Corporation");
    }

}