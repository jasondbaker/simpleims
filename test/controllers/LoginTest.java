package controllers;

import org.junit.*;

import static org.junit.Assert.*;

import java.util.*;

import play.mvc.*;
import play.libs.*;
import play.test.*;
import static play.test.Helpers.*;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

public class LoginTest extends WithApplication {
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
    public void authenticateValidLogin() {
    	Result result = callAction(
    			controllers.routes.ref.Application.authenticate(),
    			fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
    					"username", "jacksmith",
    					"password", "group123"))		
    );
    
    	assertEquals("jacksmith", session(result).get("username"));
    }
    
    @Test
    public void authenticateInvalidLogin() {
        Result result = callAction(
            controllers.routes.ref.Application.authenticate(),
            fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
                "username", "jacksmith",
                "password", "pass1234"))
        );
        assertEquals(400, status(result));
        assertNull(session(result).get("username"));
    }

    // try to access the index page using a (faked) authenticated session = should succeed
    @Test
    public void authenticatedAccount() {
        Result result = callAction(
            controllers.routes.ref.Application.index(),
            fakeRequest().withSession("username", "jacksmith")
        );
        assertEquals(200, status(result));
    }    
    
    // try to access the index page without authenticating first = should fail
    @Test
    public void notAuthenticatedAccount() {
        Result result = callAction(
            controllers.routes.ref.Application.index(),
            fakeRequest()
        );
        assertEquals(303, status(result));
        assertEquals("/login", header("Location", result));
    }
    
}
