'use strict';

angular.module('leaguegenApp')
    .factory('PartitSearch', function ($resource) {
        return $resource('api/_search/partits/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
