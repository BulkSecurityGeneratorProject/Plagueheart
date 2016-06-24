'use strict';

angular.module('plagueheartApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('search', {
                url: '/search',
                parent: 'site',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plagueheartApp.search.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/search/search.html',
                        controller: 'SearchController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('member');
                        $translatePartialLoader.addPart('search');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
    });
