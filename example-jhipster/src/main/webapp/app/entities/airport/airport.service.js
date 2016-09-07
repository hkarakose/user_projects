(function() {
    'use strict';
    angular
        .module('ticketingApp')
        .factory('Airport', Airport);

    Airport.$inject = ['$resource'];

    function Airport ($resource) {
        var resourceUrl =  'api/airports/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
