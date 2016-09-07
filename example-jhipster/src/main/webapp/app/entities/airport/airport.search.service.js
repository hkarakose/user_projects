(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .factory('AirportSearch', AirportSearch);

    AirportSearch.$inject = ['$resource'];

    function AirportSearch($resource) {
        var resourceUrl =  'api/_search/airports/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
