'use strict';

angular.module('leaguegenApp')
    .controller('ClassificacioDetailController', function ($scope, $stateParams, Classificacio, Temporada, Grup, Equip) {
        $scope.classificacio = {};
        $scope.load = function (id) {
            Classificacio.get({id: id}, function(result) {
              $scope.classificacio = result;
            });
        };
        $scope.load($stateParams.id);
    });
