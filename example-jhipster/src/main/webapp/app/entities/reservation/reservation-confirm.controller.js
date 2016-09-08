(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('ReservationConfirmController', ReservationConfirmController);

    ReservationConfirmController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Reservation', 'Airport', 'Airplane', 'Principal'];

    function ReservationConfirmController($scope, $rootScope, $stateParams, previousState, entity, Reservation, Airport, Airplane, Principal) {
        var vm = this;

        vm.flight = entity;
        vm.previousState = previousState.name;

        vm.makeReservation = function (flight) {
            console.log("flight: " + JSON.stringify(flight));
            console.log("principal: " + JSON.stringify(Principal.identity().$$state.value));
            var result = Reservation.get({flightId: flight.id, username: Principal.identity().$$state.value.login});
            console.log('result: ' + JSON.stringify(result));
            // Reservation.save(flight, Principal.identity().$$state.value.login);
        };

        var unsubscribe = $rootScope.$on('ticketingApp:flightUpdate', function(event, result) {
            vm.flight = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
