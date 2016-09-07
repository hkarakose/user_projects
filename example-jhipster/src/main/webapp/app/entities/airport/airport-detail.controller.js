(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('AirportDetailController', AirportDetailController);

    AirportDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Airport', 'City'];

    function AirportDetailController($scope, $rootScope, $stateParams, previousState, entity, Airport, City) {
        var vm = this;

        vm.airport = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ticketingApp:airportUpdate', function(event, result) {
            vm.airport = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
