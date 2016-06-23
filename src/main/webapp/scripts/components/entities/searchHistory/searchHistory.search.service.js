'use strict';

angular.module('plagueheartApp')
    .factory('SearchHistorySearch', function ($resource) {
        return $resource('api/_search/searchHistorys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
