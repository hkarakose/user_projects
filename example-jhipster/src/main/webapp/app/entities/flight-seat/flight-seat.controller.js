(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('FlightSeatController', FlightSeatController);

    FlightSeatController.$inject = ['$scope', '$state', 'FlightSeat', 'FlightSeatSearch'];

    function FlightSeatController ($scope, $state, FlightSeat, FlightSeatSearch) {
        var vm = this;
        
        vm.flightSeats = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            FlightSeat.query(function(result) {
                vm.flightSeats = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FlightSeatSearch.query({query: vm.searchQuery}, function(result) {
                vm.flightSeats = result;
            });
        }    }
})();
