(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .factory('FlightSearch', FlightSearch);

    FlightSearch.$inject = ['$resource'];

    function FlightSearch($resource) {
        var resourceUrl =  'api/_search/flights/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
