'use strict';

angular.module('leaguegenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('classificacio', {
                parent: 'entity',
                url: '/classificacio',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.classificacio.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/classificacio/classificacios.html',
                        //templateUrl: 'scripts/app/entities/adminDash/admin-dash.html',
                        controller: 'ClassificacioController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('classificacio');
                        return $translate.refresh();
                    }]
                }
            })
            .state('classificacioDetail', {
                parent: 'entity',
                url: '/classificacio/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.classificacio.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/classificacio/classificacio-detail.html',
                        controller: 'ClassificacioDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('classificacio');
                        return $translate.refresh();
                    }]
                }
            });
    });
