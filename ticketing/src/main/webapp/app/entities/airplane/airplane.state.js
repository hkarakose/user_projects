(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('airplane', {
            parent: 'entity',
            url: '/airplane?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Airplanes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/airplane/airplanes.html',
                    controller: 'AirplaneController',
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
        .state('airplane-detail', {
            parent: 'entity',
            url: '/airplane/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Airplane'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/airplane/airplane-detail.html',
                    controller: 'AirplaneDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Airplane', function($stateParams, Airplane) {
                    return Airplane.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'airplane',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('airplane-detail.edit', {
            parent: 'airplane-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airplane/airplane-dialog.html',
                    controller: 'AirplaneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Airplane', function(Airplane) {
                            return Airplane.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('airplane.new', {
            parent: 'airplane',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airplane/airplane-dialog.html',
                    controller: 'AirplaneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('airplane', null, { reload: 'airplane' });
                }, function() {
                    $state.go('airplane');
                });
            }]
        })
        .state('airplane.edit', {
            parent: 'airplane',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airplane/airplane-dialog.html',
                    controller: 'AirplaneDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Airplane', function(Airplane) {
                            return Airplane.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('airplane', null, { reload: 'airplane' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('airplane.delete', {
            parent: 'airplane',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airplane/airplane-delete-dialog.html',
                    controller: 'AirplaneDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Airplane', function(Airplane) {
                            return Airplane.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('airplane', null, { reload: 'airplane' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
