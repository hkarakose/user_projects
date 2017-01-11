(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('FlightSeatDetailController', FlightSeatDetailController);

    FlightSeatDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FlightSeat', 'Flight', 'User'];

    function FlightSeatDetailController($scope, $rootScope, $stateParams, previousState, entity, FlightSeat, Flight, User) {
        var vm = this;

        vm.flightSeat = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ticketingApp:flightSeatUpdate', function(event, result) {
            vm.flightSeat = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
