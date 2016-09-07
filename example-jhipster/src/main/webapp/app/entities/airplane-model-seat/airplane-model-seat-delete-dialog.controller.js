(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('AirplaneModelSeatDeleteController',AirplaneModelSeatDeleteController);

    AirplaneModelSeatDeleteController.$inject = ['$uibModalInstance', 'entity', 'AirplaneModelSeat'];

    function AirplaneModelSeatDeleteController($uibModalInstance, entity, AirplaneModelSeat) {
        var vm = this;

        vm.airplaneModelSeat = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AirplaneModelSeat.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
