var ims = angular.module('ims', ['ngRoute']);

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
    
	$http.get('http://localhost:9000/incidents/'+ $routeParams.incidentId).
        success(function(data) {
            $scope.incident = data;
        });
});