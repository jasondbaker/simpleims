ims.controller('getDashboard', function ($scope, $http) {
	
	// create an object to hold new incident requests
	$scope.newincident = {};
	
	function loadIncidentData(){
		
	
		// get the open incidents for the current agent
	    $http.get(remoteServer+'/incidents?status=open').
	        success(function(data) {
	            $scope.incident = data;
	        });
	};
	
	function loadUnassignedData() {
	    // get the unassigned incidents in the system
	    $http.get(remoteServer+'/incidents?status=unassigned').
	    success(function(data) {
	        $scope.unassignedIncident = data;
	    });
	};
	
    // get the current agent info
    $http.get(remoteServer+'/agent').
    success(function(data) {
    
    	$scope.currentAgent = data;
    	$scope.selectedAgent = data.username;
    });
    
    // get the list of agents
	$http.get(remoteServer+'/agents').
    success(function(data) {
        $scope.agents = data;
         
    });
	
    // get the list of categories
	$http.get(remoteServer+'/categories').
    success(function(data) {
        $scope.categories = data;
         
    });
	
	// retrieve contact list asynch
	  $scope.getContacts = function(val) {
	    return $http.get(remoteServer+'/contacts'+'?search='+val).
	    then(function(res){
	      var contacts = [];
	      var obj = {};
	      angular.forEach(res.data, function(item){
	    	  obj = {id: item.id, fullname: item.fullname};
	        contacts.push(obj);
	      });
	   
	      return contacts;
	    });
	  };
	  
	  
	   // create a new incident in the system
	   $scope.create = function() {
	    	
		   console.log("create incident");
		   var agent = "";
		   
		   // check to see if the agent was unassigned
		   agent = ($scope.unassigned) ? "unassigned" : $scope.selectedAgent;
		   
		    // create an object to hold the form values 
	    	var dataObj = { "username" : agent,
	    					"categoryId" : $scope.newincident.categoryId,
	    					"subject" : $scope.newincident.subject,
	    					"description" : $scope.newincident.description,
	    					"priority" : $scope.newincident.priority,
	    					"contactId" : $scope.selContactId.id};
	    	
	    	console.log(dataObj);
	    	
	    	// post the json object to the restful api
	    	$http.post( remoteServer+'/incidents', dataObj)
	    		.success(function(data) {
	    			console.log(data);
	    			
	    			// show notification
	    			$(function(){
	    				new PNotify({
	    					title: 'Success',
	    					text: 'Incident successfully updated.',
	    					type: 'success',
	    					styling: 'bootstrap3',
	    					delay: 3000
	    				});
	    				
	    				if (agent == "unassigned") {
	    					
	    				    loadUnassignedData();
	    				    
	    				} else {
		    				
	    					loadIncidentData();
	    			    
	    				};
	    				
	    			    // clear the form elements
	    			    $scope.selContactId = '';
	    			    $scope.newincident.subject = '';
	    			    $scope.newincident.description = '';
	    			    $scope.unassignedIncident= false;
	    			    
	    			});
	    		}).
	    		error(function(data,status,headers,config) {
	    			console.log(status);
	    			$(function(){
	    				new PNotify({
						    title: 'Error',
						    text: 'Unable to create incident.',
						    type: 'error',
						    styling: 'bootstrap3',
						    delay:3000
						});
	    			})
	    		});
	    
	   };
	 
	loadIncidentData();
	loadUnassignedData();
	
	// set default priority level for new incidents
	$scope.newincident.priority = 2;
	$scope.newincident.categoryId = 1;
    
});