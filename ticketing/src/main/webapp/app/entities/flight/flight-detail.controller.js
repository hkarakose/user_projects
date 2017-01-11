(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('FlightDetailController', FlightDetailController);

    FlightDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Flight', 'Airport', 'Airplane'];

    function FlightDetailController($scope, $rootScope, $stateParams, previousState, entity, Flight, Airport, Airplane) {
        var vm = this;

        vm.flight = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ticketingApp:flightUpdate', function(event, result) {
            vm.flight = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
