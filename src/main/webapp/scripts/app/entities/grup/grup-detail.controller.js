'use strict';

angular.module('leaguegenApp')
    .controller('GrupDetailController', function ($scope, $stateParams, Grup, Temporada, Equip, Jornada) {
        $scope.grup = {};
        $scope.load = function (id) {
            Grup.get({id: id}, function(result) {
              $scope.grup = result;
            });
        };
        $scope.load($stateParams.id);
    });
