'use strict';

angular.module('plagueheartApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('member', {
                parent: 'entity',
                url: '/members',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plagueheartApp.member.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/member/members.html',
                        controller: 'MemberController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('member');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('member.detail', {
                parent: 'entity',
                url: '/member/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'plagueheartApp.member.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/member/member-detail.html',
                        controller: 'MemberDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('member');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Member', function($stateParams, Member) {
                        return Member.get({id : $stateParams.id});
                    }]
                }
            })
            .state('member.new', {
                parent: 'member',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/member/member-dialog.html',
                        controller: 'MemberDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    mobile: null,
                                    file: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('member', null, { reload: true });
                    }, function() {
                        $state.go('member');
                    })
                }]
            })
            .state('member.edit', {
                parent: 'member',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/member/member-dialog.html',
                        controller: 'MemberDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Member', function(Member) {
                                return Member.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('member', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('member.delete', {
                parent: 'member',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/member/member-delete-dialog.html',
                        controller: 'MemberDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Member', function(Member) {
                                return Member.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('member', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
