ims.controller('showReports', function ($scope, $http) {
	
	// get the current incident stats
    $http.get(remoteServer+'/reports/stats').
        success(function(data) {
            $scope.stats = data;
            
            $scope.openPriority = {
            		  "type": "PieChart",
            		  "displayed": true,
            		  "data": {
            		    "cols": [
            		      {
            		        "id": "priority",
            		        "label": "Priority",
            		        "type": "string",
            		        "p": {}
            		      },
            		      {
            		        "id": "incidents",
            		        "label": "Incidents",
            		        "type": "number",
            		        "p": {}
            		      }
            		    ],
            		    "rows": [
            		      {
            		        "c": [
            		          {
            		            "v": "Priority 1"
            		          },
            		          {
            		            "v": data.priority1,
            		            "f": data.priority1 +" incidents"
            		          }
            		        ]
            		      },
            		      {
              		        "c": [
              		          {
              		            "v": "Priority 2"
              		          },
              		          {
              		            "v": data.priority2,
              		            "f": data.priority2 +" incidents"
              		          }
              		        ]
              		      },
            		      {
              		        "c": [
              		          {
              		            "v": "Priority 3"
              		          },
              		          {
              		            "v": data.priority3,
              		            "f": data.priority3 +" incidents"
              		          }
              		        ]
              		      }              		      
            		    ]
            		  },
            		  "options": {
            			"colors": ['red', 'orange', 'blue'],
            		    "title": "Open Incidents By Priority",
            		    "isStacked": "true",
            		    "fill": 20,
            		    "displayExactValues": true
            		    
            		  },
            		  "formatters": {}
            		}
            
            
        });
	
    
});