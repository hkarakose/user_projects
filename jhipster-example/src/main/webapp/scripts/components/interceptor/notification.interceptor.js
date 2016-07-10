 'use strict';

angular.module('istanbulhipsterApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-istanbulhipsterApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-istanbulhipsterApp-params')});
                }
                return response;
            },
        };
    });