'use strict';

angular.module('leaguegenApp')
    .factory('Jugador', function ($resource) {
        return $resource('api/jugadors/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    if (data.any_neix != null){
                        var any_neixFrom = data.any_neix.split("-");
                        data.any_neix = new Date(new Date(any_neixFrom[0], any_neixFrom[1] - 1, any_neixFrom[2]));
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
