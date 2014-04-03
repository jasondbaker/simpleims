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
        new Company("Acme Corp", "123 Plum Way", "Suite 100", "Minneapolis", "Minnesota", "55401", "Great customer").save();
        Company acme = Company.find.where().eq("name", "Acme Corp").findUnique();
        assertNotNull(acme);
        assertEquals("Acme Corp", acme.name);
    }
    
    @Test
    public void createAndVerifyContact() {
    	Company acme = new Company("Acme Corp", "123 Plum Way", "Suite 100", "Minneapolis", "Minnesota", "55401", "Great customer");
        acme.save();
        
        new Contact("john@smith.com", "John Smith", "612-222-3333", acme).save();
        Contact john = Contact.find.where().eq("fullname", "John Smith").findUnique();
        
        assertNotNull(john);
        assertEquals("john@smith.com", john.email);
        assertEquals("John Smith", john.fullname);
    }
    
    @Test
    public void createAndVerifyTicket() {
    	Agent agent = new Agent("jsmith", "group123", "John Smith", "john@smith.com");
        agent.save();
        
    	Company company = new Company("Acme Corp", "123 Plum Way", "Suite 100", "Minneapolis", "Minnesota", "55401", "Great customer");
        company.save();
        
        Contact contact = new Contact("bob@jones.com", "Bob Jones", "612-222-3333", company);
        contact.save();
        
        Ticket ticket = Ticket.create(agent.username, "Reported problem with software", "Cannot start software", 2, "Open", contact.email);
        ticket.save();
    	
        Ticket test = Ticket.find.where().eq("owner_username", agent.username).eq("description", ticket.description).findUnique();
        
        assertNotNull(test);
        assertEquals("Reported problem with software", test.subject);
        
    }
    
}
