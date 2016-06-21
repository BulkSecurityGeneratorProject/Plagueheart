'use strict';

angular.module('plagueheartApp').controller('CompanyDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Company',
        function($scope, $stateParams, $uibModalInstance, entity, Company) {

        $scope.company = entity;
        $scope.load = function(id) {
            Company.get({id : id}, function(result) {
                $scope.company = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('plagueheartApp:companyUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.company.id != null) {
                Company.update($scope.company, onSaveSuccess, onSaveError);
            } else {
                Company.save($scope.company, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
