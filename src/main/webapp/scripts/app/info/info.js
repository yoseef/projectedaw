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
                        templateUrl: 'views/main.html',
                        controller: 'InfoController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('info');
                        return $translate.refresh();
                    }]
                }
            });
    });
