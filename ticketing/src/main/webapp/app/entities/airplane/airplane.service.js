(function() {
    'use strict';
    angular
        .module('ticketingApp')
        .factory('Airplane', Airplane);

    Airplane.$inject = ['$resource'];

    function Airplane ($resource) {
        var resourceUrl =  'api/airplanes/:id';

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
