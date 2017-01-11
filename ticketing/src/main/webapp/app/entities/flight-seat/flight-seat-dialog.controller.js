(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('FlightSeatDialogController', FlightSeatDialogController);

    FlightSeatDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FlightSeat', 'Flight', 'User'];

    function FlightSeatDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FlightSeat, Flight, User) {
        var vm = this;

        vm.flightSeat = entity;
        vm.clear = clear;
        vm.save = save;
        vm.flights = Flight.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.flightSeat.id !== null) {
                FlightSeat.update(vm.flightSeat, onSaveSuccess, onSaveError);
            } else {
                FlightSeat.save(vm.flightSeat, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ticketingApp:flightSeatUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
