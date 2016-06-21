'use strict';

angular.module('plagueheartApp').controller('MemberDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Member', 'Company',
        function($scope, $stateParams, $uibModalInstance, entity, Member, Company) {

        $scope.member = entity;
        $scope.companys = Company.query();
        $scope.load = function(id) {
            Member.get({id : id}, function(result) {
                $scope.member = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('plagueheartApp:memberUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.member.id != null) {
                Member.update($scope.member, onSaveSuccess, onSaveError);
            } else {
                Member.save($scope.member, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
