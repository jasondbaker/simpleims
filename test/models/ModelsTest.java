package models;

import models.*;


import play.*;
import play.libs.*;
import com.avaje.ebean.Ebean;
import java.util.*;

import org.junit.*;

import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;


public class ModelsTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
    }
    
    @Test
    public void createAndVerifyAgent() {
        new Agent("jsmith", "group123", "John Smith", "john@smith.com").save();
        Agent john = Agent.find.where().eq("username", "jsmith").findUnique();
        assertNotNull(john);
        assertEquals("John Smith", john.fullname);
    }
    
    @Test
    public void createAndVerifyCompany() {
        new Company("Acme Corp", "Great customer", "www.acmecorporation.com").save();
        Company acme = Company.find.where().eq("name", "Acme Corp").findUnique();
        assertNotNull(acme);
        assertEquals("Acme Corp", acme.name);
    }
    
    @Test
    public void createAndVerifyAddress() {
        new Company("Acme Corp", "Great customer", "www.acmecorporation.com").save();
        Company acme = Company.find.where().eq("name", "Acme Corp").findUnique();
        new Address("123 Way Lane", "Suite 100", "Minneapolis", "MN", "55401", acme).save();
        
        Address addr = Address.find.where().eq("address1", "123 Way Lane").eq("city", "Minneapolis").findUnique();
        
        assertNotNull(addr);
        assertEquals("55401", addr.zipcode);
    }
    
    @Test
    public void createAndVerifyContact() {
    	Company acme = new Company("Acme Corp", "Great customer", "www.acmecorporation.com");
        acme.save();
        new Address("123 Way Lane", "Suite 100", "Minneapolis", "MN", "55401", acme).save();
        
        new Contact("john@smith.com", "John Smith", "612-222-3333", acme).save();
        Contact john = Contact.find.where().eq("fullname", "John Smith").findUnique();
        
        assertNotNull(john);
        assertEquals("john@smith.com", john.email);
        assertEquals("John Smith", john.fullname);
    }
    
    @Test
    public void createAndVerifyIncident() {
    	Agent agent = new Agent("jsmith", "group123", "John Smith", "john@smith.com");
        agent.save();
        
    	Company company = new Company("Acme Corp", "Great customer", "www.acmecorporation.com");
        company.save();
        new Address("123 Way Lane", "Suite 100", "Minneapolis", "MN", "55401", company).save();
        
        Contact contact = new Contact("bob@jones.com", "Bob Jones", "612-222-3333", company);
        contact.save();
        
    	Category category = new Category("Test category");
    	category.save();
    	
        Incident incident = Incident.create(agent.username, category.id, "Reported problem with software", "Cannot start software", 2, contact.id);
        incident.save();
    	
        Incident test = Incident.find.where().eq("owner_username", agent.username).eq("description", incident.description).findUnique();
        
        assertNotNull(test);
        assertEquals("Reported problem with software", test.subject);
        
    }
    
    @Test
    public void closeAndReopenIncident() {
    	Agent agent = new Agent("jsmith", "group123", "John Smith", "john@smith.com");
        agent.save();
        
    	Company company = new Company("Acme Corp", "Great customer", "www.acmecorporation.com");
        company.save();
        new Address("123 Way Lane", "Suite 100", "Minneapolis", "MN", "55401", company).save();
        
        Contact contact = new Contact("bob@jones.com", "Bob Jones", "612-222-3333", company);
        contact.save();
        
    	Category category = new Category("Test category");
    	category.save();
    	
        Incident incident = Incident.create(agent.username, category.id, "Datacenter alarm", "Alarm siren is going off in the datacenter.", 2, contact.id);
        incident.save();
    	
        Incident test = Incident.find.where().eq("owner_username", agent.username).eq("description", incident.description).findUnique();
        
        assertNotNull(test);
        assertEquals("Datacenter alarm", test.subject);
        
        // close incident
        Incident closedIncident = Incident.close(test.id, "jsmith");
        
        assertNotNull(closedIncident);
        assertEquals("Closed", closedIncident.status);
        
        Action action = Action.find.where().eq("incident_id", closedIncident.id).findUnique();
        
        assertNotNull(action);
        assertEquals("The incident was closed.", action.description);
       
        // reopen incident
        Incident reopenIncident = Incident.reopen(test.id, "jsmith");
        
        assertNotNull(reopenIncident);
        assertEquals("Open", reopenIncident.status);
    }
    
    @Test
    public void createAndVerifyAction() {
    	Agent agent = new Agent("jsmith", "group123", "John Smith", "john@smith.com");
        agent.save();
        
    	Company company = new Company("Acme Corp", "Great customer", "www.acmecorporation.com");
        company.save();
        new Address("123 Way Lane", "Suite 100", "Minneapolis", "MN", "55401", company).save();
        
        Contact contact = new Contact("bob@jones.com", "Bob Jones", "612-222-3333", company);
        contact.save();
        
    	Category category = new Category("Test category");
    	category.save();
    	
        Incident incident = Incident.create(agent.username, category.id, "Reported problem with software", "Cannot start software", 2, contact.id);
    	
        Action.create(agent.username, "Contacted customer for more information", incident.id);
        
        Action test = Action.find.where().eq("description", "Contacted customer for more information").findUnique();
        
        assertNotNull(test);
        assertEquals("Contacted customer for more information", test.description);
        
    }

    @Test
    public void createAndVerifyCategory() {
    
    	Category category = new Category("Test category");
    	category.save();
    	
    	Category test = Category.find.where().eq("type", "Test category").findUnique();
    	assertNotNull(test);
    	assertEquals("Test category", test.type);
    	
    }
    
    @Test
    public void loadFullTestdata() {
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
    	
    	// Count number of rows
        assertEquals(5, Agent.find.findRowCount());
        assertEquals(5, Company.find.findRowCount());
        assertEquals(5, Address.find.findRowCount());
        assertEquals(8, Contact.find.findRowCount());
        assertEquals(7, Category.find.findRowCount());
        assertEquals(14, Incident.find.findRowCount());    
        assertEquals(6, Action.find.findRowCount());    
    }
    	
    	
}
