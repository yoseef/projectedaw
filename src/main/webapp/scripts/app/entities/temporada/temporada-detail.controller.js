'use strict';

angular.module('leaguegenApp')
    .controller('TemporadaDetailController', function ($scope, $stateParams, Temporada, Grup) {
        $scope.temporada = {};
        $scope.load = function (id) {
            Temporada.get({id: id}, function(result) {
              $scope.temporada = result;
            });
        };
        $scope.load($stateParams.id);
    });
