(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('AirplaneModelDeleteController',AirplaneModelDeleteController);

    AirplaneModelDeleteController.$inject = ['$uibModalInstance', 'entity', 'AirplaneModel'];

    function AirplaneModelDeleteController($uibModalInstance, entity, AirplaneModel) {
        var vm = this;

        vm.airplaneModel = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AirplaneModel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
