// get a list of all the contacts in the system
ims.controller('getContacts', function ($scope, $http) {
	
    $http.get(remoteServer+'/contacts').
        success(function(data) {
            $scope.contacts = data;
        });
    
	// retrieve company list asynchronously
	  $scope.getCompanies = function(val) {
	    return $http.get(remoteServer+'/companies?search='+val).
	    then(function(res){
	    
	      var companies = [];
	      var obj = {};
	      angular.forEach(res.data, function(item){
	    	  obj = {id: item.id, name: item.name};
	        companies.push(obj);
	      });
	      console.log(companies);
	      return companies;
	    });
	  };
	  
	// create a new contact in the system
    $scope.create = function() {
     	
 	   console.log("create contact");
 	   
 	    // create an object to hold the form values 
     	var dataObj = { "fullname" : $scope.newcontact.fullname,
     					"email" : $scope.newcontact.email,
     					"phone" : $scope.newcontact.phone
     					};
     	
     	console.log(dataObj);
     	
     	// post the json object to the restful api
     	$http.post( remoteServer+'/companies/'+ $scope.selCompanyId.id + '/contacts', dataObj)
     		.success(function(data) {
     			console.log(data);
     			
     			// show notification
     			$(function(){
     				new PNotify({
     					title: 'Success',
     					text: 'Contact successfully created.',
     					type: 'success',
     					styling: 'bootstrap3',
     					delay: 3000
     				});
     				
     				// repopulate the data in the view
     			    $http.get(remoteServer+'/contacts').
     			        success(function(data) {
     			            $scope.contacts = data;
     			        });
     			    
     			    // clear the form elements
     			    $scope.selCompanyId = '';
     			    $scope.newcontact.fullname = '';
     			    $scope.newcontact.email = '';
     			    $scope.newcontact.phone = '';
     			    
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