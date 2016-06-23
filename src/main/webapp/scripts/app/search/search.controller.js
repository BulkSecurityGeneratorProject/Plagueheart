'use strict';

angular.module('plagueheartApp')
    .controller('SearchController', function ($scope, $state, Member, MemberSearch, Search, Company, ParseLinks) {

        $scope.members = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.companys = Company.query();
        $scope.loadAll = function() {
            Member.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.members = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            console.log("companyID is " + $scope.company);
            console.log($scope.company);
            Search.query({companyID: $scope.company.id, query: $scope.searchQuery}, function(result) {
                $scope.members = result;
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
            $scope.member = {
                name: null,
                mobile: null,
                file: null,
                id: null
            };
        };
    });
