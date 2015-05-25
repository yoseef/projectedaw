'use strict';

angular.module('leaguegenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userdash', {
                parent: 'entity',
                url: '/userdash',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.userdash.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'views/user-dash.html',
                        controller: 'userDashController'
                    }
                }
            })
    });
