(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('AirportDialogController', AirportDialogController);

    AirportDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Airport', 'City'];

    function AirportDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Airport, City) {
        var vm = this;

        vm.airport = entity;
        vm.clear = clear;
        vm.save = save;
        vm.cities = City.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.airport.id !== null) {
                Airport.update(vm.airport, onSaveSuccess, onSaveError);
            } else {
                Airport.save(vm.airport, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ticketingApp:airportUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
