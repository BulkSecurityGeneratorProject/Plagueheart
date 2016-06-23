'use strict';

angular.module('plagueheartApp')
    .factory('Search', function ($resource) {
        return $resource('searchcloud/api/_search/company/:companyID/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
