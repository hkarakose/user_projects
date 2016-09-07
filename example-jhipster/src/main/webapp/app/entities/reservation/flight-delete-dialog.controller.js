(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('FlightDeleteController',FlightDeleteController);

    FlightDeleteController.$inject = ['$uibModalInstance', 'entity', 'Flight'];

    function FlightDeleteController($uibModalInstance, entity, Flight) {
        var vm = this;

        vm.flight = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Flight.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
