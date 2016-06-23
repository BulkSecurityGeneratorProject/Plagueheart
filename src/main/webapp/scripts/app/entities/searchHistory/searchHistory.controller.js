'use strict';

angular.module('plagueheartApp')
    .controller('SearchHistoryController', function ($scope, $state, SearchHistory, SearchHistorySearch, ParseLinks) {

        $scope.searchHistorys = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            SearchHistory.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.searchHistorys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            SearchHistorySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.searchHistorys = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.searchHistory = {
                query: null,
                queryDate: null,
                id: null
            };
        };
    });
