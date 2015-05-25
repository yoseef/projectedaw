'use strict';

angular.module('leaguegenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('admindash', {
                parent: 'entity',
                url: '/admindash',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'leaguegenApp.admindash.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'views/admin-dash.html',
                        controller: 'ClassificacioController'
                    }
                }
            })
    });
