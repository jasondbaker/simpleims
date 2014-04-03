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
    public void createAndVerifyCompany() {
        new Company("Acme Corp", "123 Plum Way", "Suite 100", "Minneapolis", "Minnesota", "55401", "Great customer").save();
        Company acme = Company.find.where().eq("name", "Acme Corp").findUnique();
        assertNotNull(acme);
        assertEquals("Acme Corp", acme.name);
    }
    
    @Test
    public void createAndVerifyContact() {
    	new Company("Acme Corp", "123 Plum Way", "Suite 100", "Minneapolis", "Minnesota", "55401", "Great customer").save();
        Company acme = Company.find.where().eq("name", "Acme Corp").findUnique();
        
        new Contact("john@smith.com", "John Smith", "612-222-3333", acme).save();
        Contact john = Contact.find.where().eq("email", "john@smith.com").findUnique();
        assertNotNull(john);
        assertEquals("john@smith.com", john.email);
        assertEquals("John Smith", john.fullname);
    }
    
}
