(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('AirplaneModelSeatDetailController', AirplaneModelSeatDetailController);

    AirplaneModelSeatDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AirplaneModelSeat', 'AirplaneModel'];

    function AirplaneModelSeatDetailController($scope, $rootScope, $stateParams, previousState, entity, AirplaneModelSeat, AirplaneModel) {
        var vm = this;

        vm.airplaneModelSeat = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ticketingApp:airplaneModelSeatUpdate', function(event, result) {
            vm.airplaneModelSeat = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
