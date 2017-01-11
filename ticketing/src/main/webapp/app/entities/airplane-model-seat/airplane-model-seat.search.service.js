(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .factory('AirplaneModelSeatSearch', AirplaneModelSeatSearch);

    AirplaneModelSeatSearch.$inject = ['$resource'];

    function AirplaneModelSeatSearch($resource) {
        var resourceUrl =  'api/_search/airplane-model-seats/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
