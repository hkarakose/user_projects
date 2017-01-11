(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('FlightDialogController', FlightDialogController);

    FlightDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Flight', 'Airport', 'Airplane'];

    function FlightDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Flight, Airport, Airplane) {
        var vm = this;

        vm.flight = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.airports = Airport.query();
        vm.airplanes = Airplane.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.flight.id !== null) {
                Flight.update(vm.flight, onSaveSuccess, onSaveError);
            } else {
                Flight.save(vm.flight, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ticketingApp:flightUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
