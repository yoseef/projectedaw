'use strict';

angular.module('leaguegenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('grup', {
                parent: 'entity',
                url: '/grup',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.grup.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/grup/grups.html',
                        controller: 'GrupController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('grup');
                        return $translate.refresh();
                    }]
                }
            })
            .state('grupDetail', {
                parent: 'entity',
                url: '/grup/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.grup.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/grup/grup-detail.html',
                        controller: 'GrupDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('grup');
                        return $translate.refresh();
                    }]
                }
            });
    });
