'use strict';

angular.module('leaguegenApp')
    .factory('Equip', function ($resource) {
        return $resource('api/equips/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    if (data.data_alta != null){
                        var data_altaFrom = data.data_alta.split("-");
                        data.data_alta = new Date(new Date(data_altaFrom[0], data_altaFrom[1] - 1, data_altaFrom[2]));
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
