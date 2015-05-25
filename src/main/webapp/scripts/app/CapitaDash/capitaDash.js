'use strict';

angular.module('leaguegenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('capitadash', {
                parent: 'entity',
                url: '/capitadash',
                data: {
                    roles: ['ROLE_CAPITA'],
                    pageTitle: 'leaguegenApp.capitadash.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'views/capita-dash.html',
                        controller: 'capitaDashController'
                    }
                }
            })
    });
