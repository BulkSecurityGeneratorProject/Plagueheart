 'use strict';

angular.module('plagueheartApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-plagueheartApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-plagueheartApp-params')});
                }
                return response;
            }
        };
    });
