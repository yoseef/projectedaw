'use strict';

angular.module('leaguegenApp')
    .factory('FranjaSearch', function ($resource) {
        return $resource('api/_search/franjas/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
