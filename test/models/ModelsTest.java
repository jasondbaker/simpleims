package models;

import models.*;
import org.junit.*;
import static org.junit.Assert.*;
import play.test.WithApplication;
import static play.test.Helpers.*;

public class ModelsTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase()));
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
        new Company("Acme Corp", "Great customer").save();
        Company acme = Company.find.where().eq("name", "Acme Corp").findUnique();
        assertNotNull(acme);
        assertEquals("Acme Corp", acme.name);
    }
    
    @Test
    public void createAndVerifyAddress() {
        new Company("Acme Corp", "Great customer").save();
        Company acme = Company.find.where().eq("name", "Acme Corp").findUnique();
        new Address("123 Way Lane", "Suite 100", "Minneapolis", "MN", "55401", acme).save();
        
        Address addr = Address.find.where().eq("address1", "123 Way Lane").eq("city", "Minneapolis").findUnique();
        
        assertNotNull(addr);
        assertEquals("55401", addr.zipcode);
    }
    
    @Test
    public void createAndVerifyContact() {
    	Company acme = new Company("Acme Corp", "Great customer");
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
        
    	Company company = new Company("Acme Corp", "Great customer");
        company.save();
        new Address("123 Way Lane", "Suite 100", "Minneapolis", "MN", "55401", company).save();
        
        Contact contact = new Contact("bob@jones.com", "Bob Jones", "612-222-3333", company);
        contact.save();
        
        Incident incident = Incident.create(agent.username, "Reported problem with software", "Cannot start software", 2, "Open", contact.email);
        incident.save();
    	
        Incident test = Incident.find.where().eq("owner_username", agent.username).eq("description", incident.description).findUnique();
        
        assertNotNull(test);
        assertEquals("Reported problem with software", test.subject);
        
    }
    
    @Test
    public void createAndVerifyAction() {
    	Agent agent = new Agent("jsmith", "group123", "John Smith", "john@smith.com");
        agent.save();
        
    	Company company = new Company("Acme Corp", "Great customer");
        company.save();
        new Address("123 Way Lane", "Suite 100", "Minneapolis", "MN", "55401", company).save();
        
        Contact contact = new Contact("bob@jones.com", "Bob Jones", "612-222-3333", company);
        contact.save();
        
        Incident incident = Incident.create(agent.username, "Reported problem with software", "Cannot start software", 2, "Open", contact.email);
    	
        Action.create(agent.username, "Contacted customer for more information", incident.id);
        
        Action test = Action.find.where().eq("description", "Contacted customer for more information").findUnique();
        
        assertNotNull(test);
        assertEquals("Contacted customer for more information", test.description);
        
    }
    
}
