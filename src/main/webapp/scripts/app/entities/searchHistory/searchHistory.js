'use strict';

angular.module('plagueheartApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('searchHistory', {
                parent: 'entity',
                url: '/searchHistorys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plagueheartApp.searchHistory.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/searchHistory/searchHistorys.html',
                        controller: 'SearchHistoryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('searchHistory');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('searchHistory.detail', {
                parent: 'entity',
                url: '/searchHistory/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plagueheartApp.searchHistory.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/searchHistory/searchHistory-detail.html',
                        controller: 'SearchHistoryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('searchHistory');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SearchHistory', function($stateParams, SearchHistory) {
                        return SearchHistory.get({id : $stateParams.id});
                    }]
                }
            })
            .state('searchHistory.new', {
                parent: 'searchHistory',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/searchHistory/searchHistory-dialog.html',
                        controller: 'SearchHistoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    query: null,
                                    queryDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('searchHistory', null, { reload: true });
                    }, function() {
                        $state.go('searchHistory');
                    })
                }]
            })
            .state('searchHistory.edit', {
                parent: 'searchHistory',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/searchHistory/searchHistory-dialog.html',
                        controller: 'SearchHistoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SearchHistory', function(SearchHistory) {
                                return SearchHistory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('searchHistory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('searchHistory.delete', {
                parent: 'searchHistory',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/searchHistory/searchHistory-delete-dialog.html',
                        controller: 'SearchHistoryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['SearchHistory', function(SearchHistory) {
                                return SearchHistory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('searchHistory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
