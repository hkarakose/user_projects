(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .controller('AirlinesDetailController', AirlinesDetailController);

    AirlinesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Airlines', 'Airplane'];

    function AirlinesDetailController($scope, $rootScope, $stateParams, previousState, entity, Airlines, Airplane) {
        var vm = this;

        vm.airlines = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ticketingApp:airlinesUpdate', function(event, result) {
            vm.airlines = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
