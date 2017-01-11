(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .factory('AirplaneSearch', AirplaneSearch);

    AirplaneSearch.$inject = ['$resource'];

    function AirplaneSearch($resource) {
        var resourceUrl =  'api/_search/airplanes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
