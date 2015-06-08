'use strict';

angular.module('leaguegenApp')
    .controller('capitaDashController', function ($scope, Principal, Classificacio, Temporada, Grup, Equip, ClassificacioSearch) {
        console.log('capitaDashController');
        var nick = Principal.getIdentity();
        console.log(nick.equip);

    });
