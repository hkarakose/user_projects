(function() {
    'use strict';
    angular
        .module('ticketingApp')
        .factory('AirplaneModel', AirplaneModel);

    AirplaneModel.$inject = ['$resource'];

    function AirplaneModel ($resource) {
        var resourceUrl =  'api/airplane-models/:id';

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
