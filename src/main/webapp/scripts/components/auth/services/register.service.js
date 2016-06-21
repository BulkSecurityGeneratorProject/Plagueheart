'use strict';

angular.module('plagueheartApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


