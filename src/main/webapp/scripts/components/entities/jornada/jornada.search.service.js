'use strict';

angular.module('leaguegenApp')
    .factory('JornadaSearch', function ($resource) {
        return $resource('api/_search/jornadas/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
