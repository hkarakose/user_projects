(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('airplane-model', {
            parent: 'entity',
            url: '/airplane-model?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AirplaneModels'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/airplane-model/airplane-models.html',
                    controller: 'AirplaneModelController',
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
        .state('airplane-model-detail', {
            parent: 'entity',
            url: '/airplane-model/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'AirplaneModel'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/airplane-model/airplane-model-detail.html',
                    controller: 'AirplaneModelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'AirplaneModel', function($stateParams, AirplaneModel) {
                    return AirplaneModel.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'airplane-model',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('airplane-model-detail.edit', {
            parent: 'airplane-model-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airplane-model/airplane-model-dialog.html',
                    controller: 'AirplaneModelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AirplaneModel', function(AirplaneModel) {
                            return AirplaneModel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('airplane-model.new', {
            parent: 'airplane-model',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airplane-model/airplane-model-dialog.html',
                    controller: 'AirplaneModelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                model: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('airplane-model', null, { reload: 'airplane-model' });
                }, function() {
                    $state.go('airplane-model');
                });
            }]
        })
        .state('airplane-model.edit', {
            parent: 'airplane-model',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airplane-model/airplane-model-dialog.html',
                    controller: 'AirplaneModelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AirplaneModel', function(AirplaneModel) {
                            return AirplaneModel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('airplane-model', null, { reload: 'airplane-model' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('airplane-model.delete', {
            parent: 'airplane-model',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airplane-model/airplane-model-delete-dialog.html',
                    controller: 'AirplaneModelDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AirplaneModel', function(AirplaneModel) {
                            return AirplaneModel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('airplane-model', null, { reload: 'airplane-model' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
