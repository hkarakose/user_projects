(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('AirplaneDialogController', AirplaneDialogController);

    AirplaneDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Airplane', 'AirplaneModel', 'Airlines'];

    function AirplaneDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Airplane, AirplaneModel, Airlines) {
        var vm = this;

        vm.airplane = entity;
        vm.clear = clear;
        vm.save = save;
        vm.airplanemodels = AirplaneModel.query();
        vm.airlines = Airlines.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.airplane.id !== null) {
                Airplane.update(vm.airplane, onSaveSuccess, onSaveError);
            } else {
                Airplane.save(vm.airplane, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ticketingApp:airplaneUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
