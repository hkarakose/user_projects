(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('available-flights', {
            parent: 'entity',
            url: '/reservation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Available Flights'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reservation/flights.html',
                    controller: 'ReservationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('reservation-confirm', {
            parent: 'available-flights',
            url: '/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Confirm Reservation'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reservation/reservation-confirm.html',
                    controller: 'ReservationConfirmController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Flight', function($stateParams, Flight) {
                    return Flight.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'flight',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
    }

})();
