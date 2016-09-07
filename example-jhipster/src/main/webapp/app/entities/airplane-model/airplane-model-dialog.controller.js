(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('AirplaneModelDialogController', AirplaneModelDialogController);

    AirplaneModelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AirplaneModel', 'Airplane', 'AirplaneModelSeat'];

    function AirplaneModelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AirplaneModel, Airplane, AirplaneModelSeat) {
        var vm = this;

        vm.airplaneModel = entity;
        vm.clear = clear;
        vm.save = save;
        vm.airplanes = Airplane.query();
        vm.airplanemodelseats = AirplaneModelSeat.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.airplaneModel.id !== null) {
                AirplaneModel.update(vm.airplaneModel, onSaveSuccess, onSaveError);
            } else {
                AirplaneModel.save(vm.airplaneModel, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ticketingApp:airplaneModelUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
