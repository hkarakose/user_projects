(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('airport', {
            parent: 'entity',
            url: '/airport?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Airports'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/airport/airports.html',
                    controller: 'AirportController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('airport-detail', {
            parent: 'entity',
            url: '/airport/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Airport'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/airport/airport-detail.html',
                    controller: 'AirportDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Airport', function($stateParams, Airport) {
                    return Airport.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'airport',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('airport-detail.edit', {
            parent: 'airport-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airport/airport-dialog.html',
                    controller: 'AirportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Airport', function(Airport) {
                            return Airport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('airport.new', {
            parent: 'airport',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airport/airport-dialog.html',
                    controller: 'AirportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                iataCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('airport', null, { reload: 'airport' });
                }, function() {
                    $state.go('airport');
                });
            }]
        })
        .state('airport.edit', {
            parent: 'airport',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airport/airport-dialog.html',
                    controller: 'AirportDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Airport', function(Airport) {
                            return Airport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('airport', null, { reload: 'airport' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('airport.delete', {
            parent: 'airport',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airport/airport-delete-dialog.html',
                    controller: 'AirportDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Airport', function(Airport) {
                            return Airport.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('airport', null, { reload: 'airport' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
