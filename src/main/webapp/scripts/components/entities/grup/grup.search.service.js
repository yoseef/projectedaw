'use strict';

angular.module('leaguegenApp')
    .factory('GrupSearch', function ($resource) {
        return $resource('api/_search/grups/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
