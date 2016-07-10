'use strict';

angular.module('istanbulhipsterApp')
    .factory('Employer', function ($resource, DateUtils) {
        return $resource('api/employers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.establishDate = DateUtils.convertDateTimeFromServer(data.establishDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
