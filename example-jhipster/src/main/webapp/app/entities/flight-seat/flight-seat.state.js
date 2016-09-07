(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('flight-seat', {
            parent: 'entity',
            url: '/flight-seat',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FlightSeats'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flight-seat/flight-seats.html',
                    controller: 'FlightSeatController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('flight-seat-detail', {
            parent: 'entity',
            url: '/flight-seat/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FlightSeat'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flight-seat/flight-seat-detail.html',
                    controller: 'FlightSeatDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FlightSeat', function($stateParams, FlightSeat) {
                    return FlightSeat.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'flight-seat',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('flight-seat-detail.edit', {
            parent: 'flight-seat-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flight-seat/flight-seat-dialog.html',
                    controller: 'FlightSeatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FlightSeat', function(FlightSeat) {
                            return FlightSeat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flight-seat.new', {
            parent: 'flight-seat',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flight-seat/flight-seat-dialog.html',
                    controller: 'FlightSeatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                seatNo: null,
                                availability: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('flight-seat', null, { reload: 'flight-seat' });
                }, function() {
                    $state.go('flight-seat');
                });
            }]
        })
        .state('flight-seat.edit', {
            parent: 'flight-seat',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flight-seat/flight-seat-dialog.html',
                    controller: 'FlightSeatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FlightSeat', function(FlightSeat) {
                            return FlightSeat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flight-seat', null, { reload: 'flight-seat' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flight-seat.delete', {
            parent: 'flight-seat',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flight-seat/flight-seat-delete-dialog.html',
                    controller: 'FlightSeatDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FlightSeat', function(FlightSeat) {
                            return FlightSeat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flight-seat', null, { reload: 'flight-seat' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
