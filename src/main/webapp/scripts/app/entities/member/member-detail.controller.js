'use strict';

angular.module('plagueheartApp')
    .controller('MemberDetailController', function ($scope, $rootScope, $stateParams, entity, Member, Company) {
        $scope.member = entity;
        $scope.load = function (id) {
            Member.get({id: id}, function(result) {
                $scope.member = result;
            });
        };
        var unsubscribe = $rootScope.$on('plagueheartApp:memberUpdate', function(event, result) {
            $scope.member = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
