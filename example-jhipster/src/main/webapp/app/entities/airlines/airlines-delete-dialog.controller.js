(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('AirlinesDeleteController',AirlinesDeleteController);

    AirlinesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Airlines'];

    function AirlinesDeleteController($uibModalInstance, entity, Airlines) {
        var vm = this;

        vm.airlines = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Airlines.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
