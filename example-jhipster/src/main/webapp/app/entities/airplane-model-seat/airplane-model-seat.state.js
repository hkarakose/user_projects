(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('airplane-model-seat', {
            parent: 'entity',
            url: '/airplane-model-seat',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AirplaneModelSeats'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/airplane-model-seat/airplane-model-seats.html',
                    controller: 'AirplaneModelSeatController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('airplane-model-seat-detail', {
            parent: 'entity',
            url: '/airplane-model-seat/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AirplaneModelSeat'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/airplane-model-seat/airplane-model-seat-detail.html',
                    controller: 'AirplaneModelSeatDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'AirplaneModelSeat', function($stateParams, AirplaneModelSeat) {
                    return AirplaneModelSeat.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'airplane-model-seat',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('airplane-model-seat-detail.edit', {
            parent: 'airplane-model-seat-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airplane-model-seat/airplane-model-seat-dialog.html',
                    controller: 'AirplaneModelSeatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AirplaneModelSeat', function(AirplaneModelSeat) {
                            return AirplaneModelSeat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('airplane-model-seat.new', {
            parent: 'airplane-model-seat',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airplane-model-seat/airplane-model-seat-dialog.html',
                    controller: 'AirplaneModelSeatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                modelId: null,
                                seatNo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('airplane-model-seat', null, { reload: 'airplane-model-seat' });
                }, function() {
                    $state.go('airplane-model-seat');
                });
            }]
        })
        .state('airplane-model-seat.edit', {
            parent: 'airplane-model-seat',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airplane-model-seat/airplane-model-seat-dialog.html',
                    controller: 'AirplaneModelSeatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AirplaneModelSeat', function(AirplaneModelSeat) {
                            return AirplaneModelSeat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('airplane-model-seat', null, { reload: 'airplane-model-seat' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('airplane-model-seat.delete', {
            parent: 'airplane-model-seat',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airplane-model-seat/airplane-model-seat-delete-dialog.html',
                    controller: 'AirplaneModelSeatDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AirplaneModelSeat', function(AirplaneModelSeat) {
                            return AirplaneModelSeat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('airplane-model-seat', null, { reload: 'airplane-model-seat' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
