(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('CountryDetailController', CountryDetailController);

    CountryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Country'];

    function CountryDetailController($scope, $rootScope, $stateParams, previousState, entity, Country) {
        var vm = this;

        vm.country = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ticketingApp:countryUpdate', function(event, result) {
            vm.country = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
