'use strict';

angular.module('leaguegenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('partit', {
                parent: 'entity',
                url: '/partit',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.partit.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/partit/partits.html',
                        controller: 'PartitController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('partit');
                        return $translate.refresh();
                    }]
                }
            })
            .state('partitDetail', {
                parent: 'entity',
                url: '/partit/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.partit.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/partit/partit-detail.html',
                        controller: 'PartitDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('partit');
                        return $translate.refresh();
                    }]
                }
            });
    });
