(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('AirplaneModelSeatDialogController', AirplaneModelSeatDialogController);

    AirplaneModelSeatDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AirplaneModelSeat', 'AirplaneModel'];

    function AirplaneModelSeatDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AirplaneModelSeat, AirplaneModel) {
        var vm = this;

        vm.airplaneModelSeat = entity;
        vm.clear = clear;
        vm.save = save;
        vm.airplanemodels = AirplaneModel.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.airplaneModelSeat.id !== null) {
                AirplaneModelSeat.update(vm.airplaneModelSeat, onSaveSuccess, onSaveError);
            } else {
                AirplaneModelSeat.save(vm.airplaneModelSeat, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ticketingApp:airplaneModelSeatUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
