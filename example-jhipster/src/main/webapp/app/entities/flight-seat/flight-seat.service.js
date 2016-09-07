(function() {
    'use strict';
    angular
        .module('ticketingApp')
        .factory('FlightSeat', FlightSeat);

    FlightSeat.$inject = ['$resource'];

    function FlightSeat ($resource) {
        var resourceUrl =  'api/flight-seats/:id';

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
