import play.*;
import play.libs.*;

import com.avaje.ebean.Ebean;

import models.*;

import java.util.*;

public class Global extends GlobalSettings {
    @Override
    public void onStart(Application app) {
        // Check if the database is empty
        if (Agent.find.findRowCount() == 0) {
        	
        	// load the data from a yaml file
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
    } 
}