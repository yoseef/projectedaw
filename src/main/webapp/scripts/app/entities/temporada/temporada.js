'use strict';

angular.module('leaguegenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('temporada', {
                parent: 'entity',
                url: '/temporada',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.temporada.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/temporada/temporadas.html',
                        controller: 'TemporadaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('temporada');
                        return $translate.refresh();
                    }]
                }
            })
            .state('temporadaDetail', {
                parent: 'entity',
                url: '/temporada/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.temporada.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/temporada/temporada-detail.html',
                        controller: 'TemporadaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('temporada');
                        return $translate.refresh();
                    }]
                }
            });
    });
