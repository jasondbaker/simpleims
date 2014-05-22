var ims = angular.module('ims', ['ngRoute', 'ngAnimate', 'ui.bootstrap', 'googlechart']);

var remoteServer = 'http://localhost:9000';

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
		.when('/reports',
			{
				controller: 'showReports',
				templateUrl: '/assets/js/views/reports.html'
			})			
		.otherwise({ redirectTo: '/' });	
});
