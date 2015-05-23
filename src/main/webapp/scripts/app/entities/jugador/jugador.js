'use strict';

angular.module('leaguegenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('jugador', {
                parent: 'entity',
                url: '/jugador',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.jugador.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jugador/jugadors.html',
                        controller: 'JugadorController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('jugador');
                        return $translate.refresh();
                    }]
                }
            })
            .state('jugadorDetail', {
                parent: 'entity',
                url: '/jugador/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.jugador.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jugador/jugador-detail.html',
                        controller: 'JugadorDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('jugador');
                        return $translate.refresh();
                    }]
                }
            });
    });
