(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .factory('FlightSeatSearch', FlightSeatSearch);

    FlightSeatSearch.$inject = ['$resource'];

    function FlightSeatSearch($resource) {
        var resourceUrl =  'api/_search/flight-seats/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
