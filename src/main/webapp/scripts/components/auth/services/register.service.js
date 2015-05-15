'use strict';

angular.module('leaguegenApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


