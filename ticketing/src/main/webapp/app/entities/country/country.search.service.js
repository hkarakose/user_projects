(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .factory('CountrySearch', CountrySearch);

    CountrySearch.$inject = ['$resource'];

    function CountrySearch($resource) {
        var resourceUrl =  'api/_search/countries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
