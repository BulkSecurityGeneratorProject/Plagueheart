'use strict';

angular.module('plagueheartApp')
	.controller('CompanyDeleteController', function($scope, $uibModalInstance, entity, Company) {

        $scope.company = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Company.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
