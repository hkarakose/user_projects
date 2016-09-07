(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .factory('AirplaneModelSearch', AirplaneModelSearch);

    AirplaneModelSearch.$inject = ['$resource'];

    function AirplaneModelSearch($resource) {
        var resourceUrl =  'api/_search/airplane-models/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
