'use strict';

angular.module('leaguegenApp')
    .controller('JugadorDetailController', function ($scope, $stateParams, Jugador, Equip) {
        $scope.jugador = {};
        $scope.load = function (id) {
            Jugador.get({id: id}, function(result) {
              $scope.jugador = result;
            });
        };
        $scope.load($stateParams.id);
    });
