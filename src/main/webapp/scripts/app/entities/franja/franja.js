'use strict';

angular.module('leaguegenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('franja', {
                parent: 'entity',
                url: '/franja',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.franja.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/franja/franjas.html',
                        controller: 'FranjaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('franja');
                        return $translate.refresh();
                    }]
                }
            })
            .state('franjaDetail', {
                parent: 'entity',
                url: '/franja/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.franja.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/franja/franja-detail.html',
                        controller: 'FranjaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('franja');
                        return $translate.refresh();
                    }]
                }
            });
    });
