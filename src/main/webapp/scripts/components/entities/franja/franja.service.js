'use strict';

angular.module('leaguegenApp')
    .factory('Franja', function ($resource) {
        return $resource('api/franjas/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    if (data.dia != null){
                        var diaFrom = data.dia.split("-");
                        data.dia = new Date(new Date(diaFrom[0], diaFrom[1] - 1, diaFrom[2]));
                    }
                    if (data.hora_inici != null) data.hora_inici = new Date(data.hora_inici);
                    if (data.hora_fi != null) data.hora_fi = new Date(data.hora_fi);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
