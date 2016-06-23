'use strict';

angular.module('plagueheartApp')
    .factory('SearchHistory', function ($resource, DateUtils) {
        return $resource('api/searchHistorys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.queryDate = DateUtils.convertLocaleDateFromServer(data.queryDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.queryDate = DateUtils.convertLocaleDateToServer(data.queryDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.queryDate = DateUtils.convertLocaleDateToServer(data.queryDate);
                    return angular.toJson(data);
                }
            }
        });
    });
