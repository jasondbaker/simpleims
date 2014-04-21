var ims = angular.module('ims', ['ngRoute']);

ims.controller('getIncidents', function ($scope, $http) {
    $http.get('http://localhost:9000/incidents').
        success(function(data) {
            $scope.incident = data;
        });
});

ims.controller('getUnassignedIncidents', function ($scope, $http) {
    $http.get('http://localhost:9000/incidents/unassigned').
        success(function(data) {
            $scope.unassignedIncident = data;
        });
});