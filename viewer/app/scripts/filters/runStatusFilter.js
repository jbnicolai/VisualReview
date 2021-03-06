angular.module('visualDiffViewerApp')
  .filter('runStatus', function() {
    return function(input) { // expects an array of imageRunData objects
      var result = {
        nrOfPending: 0,
        nrOfAccepted: 0,
        nrOfRejected: 0
      };

      angular.forEach(input, function(value, key) {
        switch(value.status) {
          case 'ACCEPTED':
            result.nrOfAccepted++;
            break;

          case 'REJECTED':
            result.nrOfRejected++;
            break;

          case 'PENDING':
            result.nrOfPending++;
            break;
        }
      }, this);

      return result;
    };


  });
