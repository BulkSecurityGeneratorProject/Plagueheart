'use strict';

angular.module('plagueheartApp')
    .controller('SearchHistoryDetailController', function ($scope, $rootScope, $stateParams, entity, SearchHistory) {
        $scope.searchHistory = entity;
        $scope.load = function (id) {
            SearchHistory.get({id: id}, function(result) {
                $scope.searchHistory = result;
            });
        };
        var unsubscribe = $rootScope.$on('plagueheartApp:searchHistoryUpdate', function(event, result) {
            $scope.searchHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
