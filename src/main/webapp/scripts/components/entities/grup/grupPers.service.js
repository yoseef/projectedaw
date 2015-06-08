'use strict';

angular.module('leaguegenApp')
    .factory('GrupPers', function ($resource) {
        return {
            grupByTemp : $resource('api/grupsByTemp/:id', {}, {
                'query': { method: 'GET', isArray: true},
                'get': {
                    method: 'GET',
                    isArray: true,
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'update': { method:'PUT' }
            })
        }
    });
