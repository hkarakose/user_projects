(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('AirplaneDeleteController',AirplaneDeleteController);

    AirplaneDeleteController.$inject = ['$uibModalInstance', 'entity', 'Airplane'];

    function AirplaneDeleteController($uibModalInstance, entity, Airplane) {
        var vm = this;

        vm.airplane = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Airplane.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
