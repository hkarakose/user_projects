(function() {
    'use strict';

    angular
        .module('ticketingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('airlines', {
            parent: 'entity',
            url: '/airlines?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Airlines'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/airlines/airlines.html',
                    controller: 'AirlinesController',
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
        .state('airlines-detail', {
            parent: 'entity',
            url: '/airlines/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Airlines'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/airlines/airlines-detail.html',
                    controller: 'AirlinesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Airlines', function($stateParams, Airlines) {
                    return Airlines.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'airlines',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('airlines-detail.edit', {
            parent: 'airlines-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airlines/airlines-dialog.html',
                    controller: 'AirlinesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Airlines', function(Airlines) {
                            return Airlines.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('airlines.new', {
            parent: 'airlines',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airlines/airlines-dialog.html',
                    controller: 'AirlinesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                abbreviation: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('airlines', null, { reload: 'airlines' });
                }, function() {
                    $state.go('airlines');
                });
            }]
        })
        .state('airlines.edit', {
            parent: 'airlines',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airlines/airlines-dialog.html',
                    controller: 'AirlinesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Airlines', function(Airlines) {
                            return Airlines.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('airlines', null, { reload: 'airlines' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('airlines.delete', {
            parent: 'airlines',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/airlines/airlines-delete-dialog.html',
                    controller: 'AirlinesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Airlines', function(Airlines) {
                            return Airlines.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('airlines', null, { reload: 'airlines' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
