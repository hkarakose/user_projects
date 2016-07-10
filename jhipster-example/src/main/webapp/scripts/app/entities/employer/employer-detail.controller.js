'use strict';

angular.module('istanbulhipsterApp')
    .controller('EmployerDetailController', function ($scope, $rootScope, $stateParams, entity, Employer) {
        $scope.employer = entity;
        $scope.load = function (id) {
            Employer.get({id: id}, function(result) {
                $scope.employer = result;
            });
        };
        $rootScope.$on('istanbulhipsterApp:employerUpdate', function(event, result) {
            $scope.employer = result;
        });
    });
