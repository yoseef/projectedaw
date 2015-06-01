'use strict';

angular.module('leaguegenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('imagee', {
                parent: 'entity',
                url: '/image',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.uploader.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'views/image.html',
                        controller: 'imageeController'
                    }
                }
            })
    });
