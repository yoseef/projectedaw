'use strict';

angular.module('leaguegenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('equip', {
                parent: 'entity',
                url: '/equip',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.equip.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/equip/equips.html',
                        controller: 'EquipController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('equip');
                        return $translate.refresh();
                    }]
                }
            })
            .state('equipDetail', {
                parent: 'entity',
                url: '/equip/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'leaguegenApp.equip.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/equip/equip-detail.html',
                        controller: 'EquipDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('equip');
                        return $translate.refresh();
                    }]
                }
            });
    });
