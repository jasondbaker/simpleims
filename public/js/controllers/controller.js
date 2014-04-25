var ims = angular.module('ims', ['ngRoute']);

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

// get a list of all the companies in the system
ims.controller('getCompanies', function ($scope, $http) {
    $http.get('http://localhost:9000/companies').
        success(function(data) {
            $scope.company = data;
        });
});

// get a list of all the contacts in the system
ims.controller('getContacts', function ($scope, $http) {
    $http.get('http://localhost:9000/contacts').
        success(function(data) {
            $scope.contact = data;
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