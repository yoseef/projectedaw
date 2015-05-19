'use strict';

angular.module('leaguegenApp')
    .factory('Jornada', function ($resource) {
        return $resource('api/jornadas/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
