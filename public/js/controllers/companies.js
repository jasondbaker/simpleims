// get a list of all the companies in the system
ims.controller('getCompanies', function ($scope, $http) {
	
    $http.get(remoteServer+'/companies').
        success(function(data) {
            $scope.company = data;
        });
    
	   // create a new company
	   $scope.create = function() {
	    	
		   console.log("create company");
		   
		    // create an object to hold the form values 
		   if ($scope.newcompany.address2 == null) $scope.newcompany.address2 = '';
		   if ($scope.newcompany.website == null) $scope.newcompany.website = '';
		   if ($scope.newcompany.notes == null) $scope.newcompany.notes = '';
		   
	    	var dataObj = { 
	    			"name" : $scope.newcompany.name,
	    			"address1" : $scope.newcompany.address1,
	    			"address2" : $scope.newcompany.address2,
	    			"city" : $scope.newcompany.city,
	    			"state" : $scope.newcompany.state,
	    			"zipcode" : $scope.newcompany.zipcode,
	    			"website" : $scope.newcompany.website,
	    			"notes" : $scope.newcompany.notes
	    			};
	    	
	    	console.log(dataObj);
	    	
	    	// post the json object to the restful api
	    	$http.post( remoteServer+'/companies', dataObj)
	    		.success(function(data) {
	    			console.log(data);
	    			
	    			// show notification
	    			$(function(){
	    				new PNotify({
	    					title: 'Success',
	    					text: 'Company successfully created.',
	    					type: 'success',
	    					styling: 'bootstrap3',
	    					delay: 3000
	    				});
	    				
	    				// repopulate the data in the view
	    				$http.get(remoteServer+'/companies').
	    		        success(function(data) {
	    		            $scope.company = data;
	    		        });
	    			    
	    			    // clear the form elements
	    			 
	    			    $scope.newcompany.name = '';
	    			    $scope.newcompany.address1 = '';
	    			    $scope.newcompany.address2 = '';
	    			    $scope.newcompany.city = '';
	    			    $scope.newcompany.state = '';
	    			    $scope.newcompany.zipcode = '';
	    			    $scope.newcompany.website = '';
	    			    $scope.newcompany.notes = '';
	    			    
	    			    
	    			});
	    		}).
	    		error(function(data,status,headers,config) {
	    			console.log(status);
	    			$(function(){
	    				new PNotify({
						    title: 'Error',
						    text: 'Unable to create company.',
						    type: 'error',
						    styling: 'bootstrap3',
						    delay:3000
						});
	    			})
	    		});
	    
	   };
});