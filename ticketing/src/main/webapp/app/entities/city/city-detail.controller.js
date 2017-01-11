(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('CityDetailController', CityDetailController);

    CityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'City', 'Country'];

    function CityDetailController($scope, $rootScope, $stateParams, previousState, entity, City, Country) {
        var vm = this;

        vm.city = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ticketingApp:cityUpdate', function(event, result) {
            vm.city = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
