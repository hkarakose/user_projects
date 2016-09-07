(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('flight', {
            parent: 'entity',
            url: '/flight',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Flights'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flight/flights.html',
                    controller: 'FlightController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('flight-detail', {
            parent: 'entity',
            url: '/flight/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Flight'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flight/flight-detail.html',
                    controller: 'FlightDetailController',
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
        .state('flight-detail.edit', {
            parent: 'flight-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flight/flight-dialog.html',
                    controller: 'FlightDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Flight', function(Flight) {
                            return Flight.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flight.new', {
            parent: 'flight',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flight/flight-dialog.html',
                    controller: 'FlightDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                flightCode: null,
                                date: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('flight', null, { reload: 'flight' });
                }, function() {
                    $state.go('flight');
                });
            }]
        })
        .state('flight.edit', {
            parent: 'flight',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flight/flight-dialog.html',
                    controller: 'FlightDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Flight', function(Flight) {
                            return Flight.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flight', null, { reload: 'flight' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flight.delete', {
            parent: 'flight',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flight/flight-delete-dialog.html',
                    controller: 'FlightDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Flight', function(Flight) {
                            return Flight.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('flight', null, { reload: 'flight' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
