'use strict';

angular.module('leaguegenApp')
    .factory('JornPers', function ($resource) {
        return {
            generate : $resource('api/jornadas/generar/:id/:data', {}, {
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
