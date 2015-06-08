'use strict';

angular.module('leaguegenApp')
    .factory('equipPers', function ($resource) {
        return {
            equipsByGrup : $resource('api/equipsByGrup/:id', {}, {
                'query': { method: 'GET', isArray: true},
                'get': {
                    method: 'GET',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'update': { method:'PUT' }
            })
        }
    });
