'use strict';

angular.module('leaguegenApp')
    .factory('JugadorSearch', function ($resource) {
        return $resource('api/_search/jugadors/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
