'use strict';

angular.module('leaguegenApp')
    .factory('EquipSearch', function ($resource) {
        return $resource('api/_search/equips/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
