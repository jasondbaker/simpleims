// get all the information associated with a specific incident
ims.controller('getIncident', function ($scope, $routeParams, $http, $location) {
    
	// get the incident information
	$http.get(remoteServer+'/incidents/'+ $routeParams.incidentId).
        success(function(data) {
            $scope.incident = data;
            
        	// get the company associated with the incident
        	$http.get(remoteServer+'/contacts/'+ $scope.incident.requester.id + '/companies').
            success(function(data) {
                $scope.company = data;
                
            	// get the list of agents
            	$http.get(remoteServer+'/agents').
                success(function(data) {
                    $scope.agents = data;
                    
                    $scope.selectedAgent = "";
                    
                    // need to figure out which agent owns the incident to display a name in the form selector
                    // but don't try to figure it out if the incident has no owner (unassigned)
                    if ($scope.incident.owner.username != 'unassigned') {
	                    

	                	for (var i = 0; i < $scope.agents.length; i++) {
	                		if ($scope.agents[i].username == $scope.incident.owner.username) {
	                			$scope.selectedAgent = $scope.agents[i].username;
	                		}
	                	
	                	}
                    } else {
                    	// set an attribute if the incident does not have an assigned agent
                    	$scope.unassigned = true;
                    }
                    
                });
            	
                // get the list of categories
            	$http.get(remoteServer+'/categories').
                success(function(data) {
                    $scope.categories = data;
                    
                    $scope.selectedCategory = $scope.incident.category.id;
                });
            	
            	
            });
        });
	
		
	   // create a new action associated with the incident
	   $scope.create = function() {
	    	
		   console.log("create action");
		   
		    // create an object to hold the form values 
	    	var dataObj = { "description" : $scope.newaction.description }
	    	
	    	console.log(dataObj);
	    	
	    	// post the json object to the restful api
	    	$http.post( remoteServer+'/incidents/'+ $scope.incident.id + '/actions', dataObj)
	    		.success(function(data) {
	    			console.log(data);
	    			
	    			// show notification
	    			$(function(){
	    				new PNotify({
	    					title: 'Success',
	    					text: 'Action successfully added.',
	    					type: 'success',
	    					styling: 'bootstrap3',
	    					delay: 3000
	    				});
	    				
	    				// repopulate the data in the view
	    				$http.get(remoteServer+'/incidents/'+ $routeParams.incidentId).
	    		        success(function(data) {
	    		            $scope.incident = data;
	    			        });
	    			    
	    			    // clear the form elements
	    			 
	    			    $scope.newaction.description = '';
	    			    
	    			});
	    		}).
	    		error(function(data,status,headers,config) {
	    			console.log(status);
	    			$(function(){
	    				new PNotify({
						    title: 'Error',
						    text: 'Unable to add action.',
						    type: 'error',
						    styling: 'bootstrap3',
						    delay:3000
						});
	    			})
	    		});
	    
	   };
	   
	   // update the system using the incident form values
	   $scope.close = function() {
	    	
		   console.log("close incident");
		   
		// get the current agent
       	$http.get(remoteServer+'/agent').
           success(function(data) {
               var agent = data;
               	
               	if (agent.username == $scope.selectedAgent) {
				    // create an object to hold the form values 
			    	var dataObj = { };
			    	
			    	console.log(dataObj);
			    	
			    	// post the json object to the restful api
			    	$http.post( remoteServer+'/incidents/' + $scope.incident.id + '/close', dataObj)
			    		.success(function(data) {
			    			console.log(data);
			    			
			    			// show notification
			    			$(function(){
			    				new PNotify({
			    					title: 'Success',
			    					text: 'Incident closed.',
			    					type: 'success',
			    					styling: 'bootstrap3',
			    					delay: 3000
			    				});
			    			});
			    			$location.path('/');
			    			
			    		}).
			    		error(function(data,status,headers,config) {
			    			console.log(status);
			    			$(function(){
			    				new PNotify({
								    title: 'Error',
								    text: 'Unable to close incident.',
								    type: 'error',
								    styling: 'bootstrap3',
								    delay:3000
								});
			    			})
			    		});
               	} else {
               		
               		// report that the current agent does not own the incident
               		$(function(){
	    				new PNotify({
						    title: 'Warning',
						    text: 'Unable to close incident because the agent is not the owner.',
						    type: 'error',
						    styling: 'bootstrap3',
						    delay:5000
						});
	    			})	
               	};
           });
	    
	   };
	   
	   // update the system using the incident form values
	   $scope.update = function() {
	    	
		   console.log("update incident");
		   
		    // create an object to hold the form values 
	    	var dataObj = { "username" : $scope.selectedAgent,
	    					"categoryId" : $scope.selectedCategory,
	    					"subject" : $scope.incident.subject,
	    					"description" : $scope.incident.description,
	    					"priority" : $scope.incident.priority,
	    					"contactId" : $scope.incident.requester.id};
	    	
	    	console.log(dataObj);
	    	
	    	// post the json object to the restful api
	    	$http.post( remoteServer+'/incidents/' + $scope.incident.id, dataObj)
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
	    			});
	    			
	    			// repopulate the data in the view
    				$http.get(remoteServer+'/incidents/'+ $routeParams.incidentId).
    		        success(function(data) {
    		            $scope.incident = data;
    			        });
    				
	    		}).
	    		error(function(data,status,headers,config) {
	    			console.log(status);
	    			$(function(){
	    				new PNotify({
						    title: 'Error',
						    text: 'Unable to update incident.',
						    type: 'error',
						    styling: 'bootstrap3',
						    delay:3000
						});
	    			})
	    		});
	    
	   };
	   
});