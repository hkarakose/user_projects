'use strict';

angular.module('istanbulhipsterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('employer', {
                parent: 'entity',
                url: '/employers',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'istanbulhipsterApp.employer.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/employer/employers.html',
                        controller: 'EmployerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('employer');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('employer.detail', {
                parent: 'entity',
                url: '/employer/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'istanbulhipsterApp.employer.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/employer/employer-detail.html',
                        controller: 'EmployerDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('employer');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Employer', function($stateParams, Employer) {
                        return Employer.get({id : $stateParams.id});
                    }]
                }
            })
            .state('employer.new', {
                parent: 'employer',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/employer/employer-dialog.html',
                        controller: 'EmployerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {title: null, establishDate: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('employer', null, { reload: true });
                    }, function() {
                        $state.go('employer');
                    })
                }]
            })
            .state('employer.edit', {
                parent: 'employer',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/employer/employer-dialog.html',
                        controller: 'EmployerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Employer', function(Employer) {
                                return Employer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('employer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
