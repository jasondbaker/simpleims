var ims = angular.module('ims', ['ngRoute', 'ngAnimate']);

// directive to set bootstrap nav to reflect current route
angular.module('ims').directive('bsNavbar', ['$location', function ($location) {
	  return {
	    restrict: 'A',
	    link: function postLink(scope, element) {
	      scope.$watch(function () {
	        return $location.path();
	      }, function (path) {
	        angular.forEach(element.children(), (function (li) {
	          var $li = angular.element(li),
	            regex = new RegExp('^' + $li.attr('data-match-route') + '$', 'i'),
	            isActive = regex.test(path);
	          $li.toggleClass('active', isActive);
	        }));
	      });
	    }
	  };
	}]);

ims.config(function ($routeProvider) {
	$routeProvider
		.when('/',
			{
				controller: 'getDashboard',
				templateUrl: '/assets/js/views/dashboard.html'
			})
		.when('/incident/:incidentId',
			{
				controller: 'getIncident',
				templateUrl: '/assets/js/views/incident.html'
			})
		.when('/companies/:companyId',
			{
				controller: 'getCompany',
				templateUrl: '/assets/js/views/company.html'
			})				
		.when('/companies',
			{
				controller: 'getCompanies',
				templateUrl: '/assets/js/views/companies.html'
			})			
		.when('/contacts',
			{
				controller: 'getContacts',
				templateUrl: '/assets/js/views/contacts.html'
			})
		.when('/contacts/:contactId',
			{
				controller: 'getContact',
				templateUrl: '/assets/js/views/contact.html'
			})
		.when('/new',
			{
				controller: 'newIncident',
				templateUrl: '/assets/js/views/newincident.html'
			})
		.otherwise({ redirectTo: '/' });	
});

ims.controller('getDashboard', function ($scope, $http) {
	
	// get the incidents for the current agent
    $http.get('http://localhost:9000/incidents').
        success(function(data) {
            $scope.incident = data;
        });
    
    // get the unassigned incidents in the system
    $http.get('http://localhost:9000/incidents/unassigned').
    success(function(data) {
        $scope.unassignedIncident = data;
    });
    
});

//get the data for the company view
ims.controller('getCompany', function ($scope, $routeParams, $http) {
	// get the contact information
    $http.get('http://localhost:9000/companies/' + $routeParams.companyId).
        success(function(data) {
            $scope.company = data;
        });
    
    // get all the incidents associated with the company
    $http.get('http://localhost:9000/companies/' + $routeParams.companyId + '/incidents').
    success(function(data) {
        $scope.companyIncidents = data;
    });
    
    // update the system using the company form values
    $scope.update = function() {
     	
 	   console.log("update company");
 	   
 	    // create objects to hold the form values 
     	var companyObj = { "name" : $scope.company.name,
     					"notes" : $scope.company.notes,
     					"website" : $scope.company.website};
     	
     	var addrObj = { "address1" : $scope.company.addresses[0].address1,
					"address2" : $scope.company.addresses[0].address2,
					"city" : $scope.company.addresses[0].city,
					"state" : $scope.company.addresses[0].state,
					"zipcode" : $scope.company.addresses[0].zipcode
					};
     	
     	console.log(dataObj);
     	
     	// post the json object to the restful api
     	$http.post( 'http://localhost:9000/companies/' + $scope.company.id, companyObj)
     		.success(function(data) {
     			console.log(data);
     			
     			// show notification
     			$(function(){
     				new PNotify({
     					title: 'Success',
     					text: 'Contact successfully updated.',
     					type: 'success',
     					styling: 'bootstrap3',
     					delay: 3000
     				});
     			});
     		}).
     		error(function(data,status,headers,config) {
     			console.log(status);
     			$(function(){
     				new PNotify({
 					    title: 'Error',
 					    text: 'Unable to update contact.',
 					    type: 'error',
 					    styling: 'bootstrap3',
 					    delay:3000
 					});
     			})
     		});
     
    };
    
});

// get a list of all the companies in the system
ims.controller('getCompanies', function ($scope, $http) {
	
    $http.get('http://localhost:9000/companies').
        success(function(data) {
            $scope.company = data;
        });
});

// get the data for the contact view
ims.controller('getContact', function ($scope, $routeParams, $http) {
	// get the contact information
    $http.get('http://localhost:9000/contacts/' + $routeParams.contactId).
        success(function(data) {
            $scope.contact = data;
        });
    
    // get all the incidents associated with the contact
    $http.get('http://localhost:9000/contacts/' + $routeParams.contactId + '/incidents').
    success(function(data) {
        $scope.contactIncidents = data;
    });
    
    // get all the companies associated with the contact
    $http.get('http://localhost:9000/contacts/' + $routeParams.contactId + '/companies').
    success(function(data) {
        $scope.contactCompanies = data;
    });  

   // update the system using the contact form values
   $scope.update = function() {
    	
	   console.log("update contact");
	   
	    // create an object to hold the form values 
    	var dataObj = { "email" : $scope.contact.email,
    					"fullname" : $scope.contact.fullname,
    					"phone" : $scope.contact.phone };
    	
    	console.log(dataObj);
    	
    	// post the json object to the restful api
    	$http.post( 'http://localhost:9000/contacts/' + $scope.contact.id, dataObj)
    		.success(function(data) {
    			console.log(data);
    			
    			// show notification
    			$(function(){
    				new PNotify({
    					title: 'Success',
    					text: 'Contact successfully updated.',
    					type: 'success',
    					styling: 'bootstrap3',
    					delay: 3000
    				});
    			});
    		}).
    		error(function(data,status,headers,config) {
    			console.log(status);
    			$(function(){
    				new PNotify({
					    title: 'Error',
					    text: 'Unable to update contact.',
					    type: 'error',
					    styling: 'bootstrap3',
					    delay:3000
					});
    			})
    		});
    
   };
   
});

// get a list of all the contacts in the system
ims.controller('getContacts', function ($scope, $http) {
	
    $http.get('http://localhost:9000/contacts').
        success(function(data) {
            $scope.contacts = data;
        });
});

// get all the information associated with a specific incident
ims.controller('getIncident', function ($scope, $routeParams, $http) {
    
	// get the incident information
	$http.get('http://localhost:9000/incidents/'+ $routeParams.incidentId).
        success(function(data) {
            $scope.incident = data;
            
        	// get the company associated with the incident
        	$http.get('http://localhost:9000/contacts/'+ $scope.incident.requester.id + '/companies').
            success(function(data) {
                $scope.company = data;
                
            	// get the list of agents
            	$http.get('http://localhost:9000/agents').
                success(function(data) {
                    $scope.agents = data;
                    
                    $scope.selectedAgent = "";
                    
                    // need to figure out which agent owns the incident to display a name in the form selector
                    // but don't try to figure it out if the incident has no owner (unassigned)
                    if ($scope.incident.owner != null) {
	                    

	                	for (var i = 0; i < $scope.agents.length; i++) {
	                		if ($scope.agents[i].username == $scope.incident.owner.username) {
	                			$scope.selectedAgent = $scope.agents[i].username;
	                		}
	                	
	                	}
                    }
                    
                });
            	
            });
        });
	
});