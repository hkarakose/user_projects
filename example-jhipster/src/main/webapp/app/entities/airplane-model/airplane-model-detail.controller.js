(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('AirplaneModelDetailController', AirplaneModelDetailController);

    AirplaneModelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AirplaneModel', 'Airplane', 'AirplaneModelSeat'];

    function AirplaneModelDetailController($scope, $rootScope, $stateParams, previousState, entity, AirplaneModel, Airplane, AirplaneModelSeat) {
        var vm = this;

        vm.airplaneModel = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ticketingApp:airplaneModelUpdate', function(event, result) {
            vm.airplaneModel = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
