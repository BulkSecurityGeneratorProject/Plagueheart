'use strict';

angular.module('plagueheartApp')
    .factory('MemberSearch', function ($resource) {
        return $resource('api/_search/members/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
