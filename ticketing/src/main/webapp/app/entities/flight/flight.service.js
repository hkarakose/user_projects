(function() {
    'use strict';
    angular
        .module('ticketingApp')
        .factory('Flight', Flight);

    Flight.$inject = ['$resource', 'DateUtils'];

    function Flight ($resource, DateUtils) {
        var resourceUrl =  'api/flights/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
