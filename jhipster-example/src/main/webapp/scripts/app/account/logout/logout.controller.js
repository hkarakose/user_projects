'use strict';

angular.module('istanbulhipsterApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
