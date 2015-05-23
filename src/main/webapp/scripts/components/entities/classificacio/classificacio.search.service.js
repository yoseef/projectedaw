'use strict';

angular.module('leaguegenApp')
    .factory('ClassificacioSearch', function ($resource) {
        return $resource('api/_search/classificacios/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
