var ims = angular.module('ims', ['ngRoute']);

ims.config(function ($routeProvider) {
	$routeProvider
		.when('/',
			{
				controller: 'getIncidents',
				templateUrl: '/assets/js/views/dashboard.html'
			})
		.when('/new',
			{
				controller: 'newIncident',
				templateUrl: '/assets/js/views/newincident.html'
			})
		.otherwise({ redirectTo: '/' });	
});

ims.controller('getIncidents', function ($scope, $http) {
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

ims.controller('getUnassignedIncidents', function ($scope, $http) {
    $http.get('http://localhost:9000/incidents/unassigned').
        success(function(data) {
            $scope.unassignedIncident = data;
        });
});