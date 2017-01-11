(function() {
    'use strict';
    angular
        .module('ticketingApp')
        .factory('AirplaneModelSeat', AirplaneModelSeat);

    AirplaneModelSeat.$inject = ['$resource'];

    function AirplaneModelSeat ($resource) {
        var resourceUrl =  'api/airplane-model-seats/:id';

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
