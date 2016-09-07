(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .factory('AirlinesSearch', AirlinesSearch);

    AirlinesSearch.$inject = ['$resource'];

    function AirlinesSearch($resource) {
        var resourceUrl =  'api/_search/airlines/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
