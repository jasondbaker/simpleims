//get the data for the company view
ims.controller('getCompany', function ($scope, $routeParams, $http) {
	// get the contact information
    $http.get(remoteServer+'/companies/' + $routeParams.companyId).
        success(function(data) {
            $scope.company = data;
        });
    
    // get all the incidents associated with the company
    $http.get(remoteServer+'/companies/' + $routeParams.companyId + '/incidents').
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
     	
     	console.log(companyObj);
     	
     	// post the json object to the restful api
     	$http.post( remoteServer+'/companies/' + $scope.company.id, companyObj)
     		.success(function(data) {
     			console.log(data);
     			
     			// show notification
     			$(function(){
     				new PNotify({
     					title: 'Success',
     					text: 'Company successfully updated.',
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
 					    text: 'Unable to update company.',
 					    type: 'error',
 					    styling: 'bootstrap3',
 					    delay:3000
 					});
     			})
     		});
     	
     	console.log(companyObj);
     	
     	// post the json object to the restful api
     	$http.post( remoteServer+'/companies/' + $scope.company.id + '/addresses/' + $scope.company.addresses[0].id, addrObj)
     		.success(function(data) {
     			console.log(data);
     			
     			// show notification
     			$(function(){
     				new PNotify({
     					title: 'Success',
     					text: 'Address successfully updated.',
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
 					    text: 'Unable to update address.',
 					    type: 'error',
 					    styling: 'bootstrap3',
 					    delay:3000
 					});
     			})
     		});
     
    };
    
});