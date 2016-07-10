'use strict';

angular.module('istanbulhipsterApp').controller('EmployerDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Employer',
        function($scope, $stateParams, $modalInstance, entity, Employer) {

        $scope.employer = entity;
        $scope.load = function(id) {
            Employer.get({id : id}, function(result) {
                $scope.employer = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('istanbulhipsterApp:employerUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.employer.id != null) {
                Employer.update($scope.employer, onSaveFinished);
            } else {
                Employer.save($scope.employer, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
