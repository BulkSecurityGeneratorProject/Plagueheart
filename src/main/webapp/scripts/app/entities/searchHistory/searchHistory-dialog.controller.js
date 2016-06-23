'use strict';

angular.module('plagueheartApp').controller('SearchHistoryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'SearchHistory',
        function($scope, $stateParams, $uibModalInstance, entity, SearchHistory) {

        $scope.searchHistory = entity;
        $scope.load = function(id) {
            SearchHistory.get({id : id}, function(result) {
                $scope.searchHistory = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('plagueheartApp:searchHistoryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.searchHistory.id != null) {
                SearchHistory.update($scope.searchHistory, onSaveSuccess, onSaveError);
            } else {
                SearchHistory.save($scope.searchHistory, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForQueryDate = {};

        $scope.datePickerForQueryDate.status = {
            opened: false
        };

        $scope.datePickerForQueryDateOpen = function($event) {
            $scope.datePickerForQueryDate.status.opened = true;
        };
}]);
