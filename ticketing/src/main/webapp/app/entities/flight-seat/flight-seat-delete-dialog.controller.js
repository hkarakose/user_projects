(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('FlightSeatDeleteController',FlightSeatDeleteController);

    FlightSeatDeleteController.$inject = ['$uibModalInstance', 'entity', 'FlightSeat'];

    function FlightSeatDeleteController($uibModalInstance, entity, FlightSeat) {
        var vm = this;

        vm.flightSeat = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FlightSeat.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
