'use strict';

angular.module('leaguegenApp')
    .controller('EquipDetailController', function ($scope, $stateParams, Equip, Grup, Partit, Jugador) {
        $scope.equip = {};
        $scope.jugadors = Jugador.query();
        $scope.load = function (id) {
            Equip.get({id: id}, function(result) {
              $scope.equip = result;
            });
        };
        $scope.load($stateParams.id);
    });
