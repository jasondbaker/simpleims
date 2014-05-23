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
          		        "id": "Priority",
          		        "label": "Priority",
          		        "type": "string",
          		        "p": {}
          		      },
          		      {
          		        "id": "Incidents",
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
          			"fontSize": 14,
          		    "title": "Open Incidents By Priority",
          		    "isStacked": "true",
          		    "fill": 20,
          		    "displayExactValues": true
          		    
          		  },
          		  "formatters": {}
          		}            	
            
            
        });
    
   	// get the recent incident count (90 days)
    $http.get(remoteServer+'/reports/incidentcount').
        success(function(data) {
            $scope.incidents = data;
            
            $scope.calendarIncidents = {
    		  "type": "Calendar",
    		  "displayed": true,
    		  "data": {
    		    "cols": [
    		      {
    		        "id": "date",
    		        "label": "Date",
    		        "type": "date",
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
    		               		      
    		    ]
    		  },
    		  "options": {
    			"fontSize": 14,
    		    "title": "Open Incidents By Month",
    		    "fill": 20,
    		    "displayExactValues": true
    		    
    		  },
    		  "formatters": {}
    		}
	
           // add incidents from db to calendar chart object
            var newrow;
            var parts;
            
            // loop through the json array objects and populate the new chart config object
            for (var i=0; i < data.length; i++) {
            
            	// need to convert the sql query date string to a js date object
            	parts = data[i].date.split('-');
            
                newrow = {
            			"c": [
            			      {
            			    	  "v": new Date(parts[0], parts[1]-1, parts[2] )
            			      },
            			      {
            			    	  "v": data[i].incidents
            			      }
            			      ]
            			};
            
                $scope.calendarIncidents.data.rows.push(newrow);
               
            };
     
            
        });
});