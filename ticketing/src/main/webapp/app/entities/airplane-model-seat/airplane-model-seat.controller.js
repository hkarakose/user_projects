(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('AirplaneModelSeatController', AirplaneModelSeatController);

    AirplaneModelSeatController.$inject = ['$scope', '$state', 'AirplaneModelSeat', 'AirplaneModelSeatSearch'];

    function AirplaneModelSeatController ($scope, $state, AirplaneModelSeat, AirplaneModelSeatSearch) {
        var vm = this;
        
        vm.airplaneModelSeats = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            AirplaneModelSeat.query(function(result) {
                vm.airplaneModelSeats = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            AirplaneModelSeatSearch.query({query: vm.searchQuery}, function(result) {
                vm.airplaneModelSeats = result;
            });
        }    }
})();
