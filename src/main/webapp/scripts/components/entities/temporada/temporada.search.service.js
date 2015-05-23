'use strict';

angular.module('leaguegenApp')
    .factory('TemporadaSearch', function ($resource) {
        return $resource('api/_search/temporadas/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
