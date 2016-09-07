(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('AirlinesDialogController', AirlinesDialogController);

    AirlinesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Airlines', 'Airplane'];

    function AirlinesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Airlines, Airplane) {
        var vm = this;

        vm.airlines = entity;
        vm.clear = clear;
        vm.save = save;
        vm.airplanes = Airplane.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.airlines.id !== null) {
                Airlines.update(vm.airlines, onSaveSuccess, onSaveError);
            } else {
                Airlines.save(vm.airlines, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ticketingApp:airlinesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
