'use strict';

angular.module('leaguegenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('info', {
                parent: 'site',
                url: '/info',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'views/info.html',
                        controller: 'InfoController'
                    }
                }
            });
    });
