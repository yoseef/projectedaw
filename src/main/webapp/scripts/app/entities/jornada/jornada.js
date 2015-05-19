'use strict';

angular.module('leaguegenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('jornada', {
                parent: 'entity',
                url: '/jornada',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.jornada.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jornada/jornadas.html',
                        controller: 'JornadaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('jornada');
                        return $translate.refresh();
                    }]
                }
            })
            .state('jornadaDetail', {
                parent: 'entity',
                url: '/jornada/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.jornada.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jornada/jornada-detail.html',
                        controller: 'JornadaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('jornada');
                        return $translate.refresh();
                    }]
                }
            });
    });
