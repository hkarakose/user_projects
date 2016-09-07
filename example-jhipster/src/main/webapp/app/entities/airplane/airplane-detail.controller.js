(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('AirplaneDetailController', AirplaneDetailController);

    AirplaneDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Airplane', 'AirplaneModel', 'Airlines'];

    function AirplaneDetailController($scope, $rootScope, $stateParams, previousState, entity, Airplane, AirplaneModel, Airlines) {
        var vm = this;

        vm.airplane = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ticketingApp:airplaneUpdate', function(event, result) {
            vm.airplane = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
