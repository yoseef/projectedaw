'use strict';

angular.module('leaguegenApp')
    .factory('Classificacio', function ($resource) {
        return $resource('api/classificacios/:id', {}, {
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
