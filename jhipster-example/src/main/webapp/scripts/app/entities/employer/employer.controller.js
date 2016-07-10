'use strict';

angular.module('istanbulhipsterApp')
    .controller('EmployerController', function ($scope, Employer, ParseLinks) {
        $scope.employers = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Employer.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.employers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Employer.get({id: id}, function(result) {
                $scope.employer = result;
                $('#deleteEmployerConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Employer.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEmployerConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.employer = {title: null, establishDate: null, id: null};
        };
    });
