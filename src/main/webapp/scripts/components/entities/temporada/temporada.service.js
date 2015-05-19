'use strict';

angular.module('leaguegenApp')
    .factory('Temporada', function ($resource) {
        return $resource('api/temporadas/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    if (data.any != null){
                        var anyFrom = data.any.split("-");
                        data.any = new Date(new Date(anyFrom[0], anyFrom[1] - 1, anyFrom[2]));
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
