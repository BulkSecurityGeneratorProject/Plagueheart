'use strict';

angular.module('plagueheartApp')
	.controller('SearchHistoryDeleteController', function($scope, $uibModalInstance, entity, SearchHistory) {

        $scope.searchHistory = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            SearchHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
