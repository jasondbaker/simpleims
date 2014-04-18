function getIncidents($scope, $http) {
    $http.get('http://localhost:9000/incidents').
        success(function(data) {
            $scope.incident = data;
        });
}

function getUnassignedIncidents($scope, $http) {
    $http.get('http://localhost:9000/incidents/unassigned').
        success(function(data) {
            $scope.unassignedIncident = data;
        });
}