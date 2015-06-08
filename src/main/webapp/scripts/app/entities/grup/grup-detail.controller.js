'use strict';

angular.module('leaguegenApp')
    .controller('GrupDetailController', function ($scope, $stateParams, Grup, Temporada, Equip, Jornada , equipPers) {
        $scope.grup = {};
        $scope.load = function (id) {
            Grup.get({id: id}, function(result) {
              $scope.grup = result;
            });
        };
        equipPers.equipsByGrup.query({id: $stateParams.id}, function (res) {
            $scope.equips = res;
            console.log($scope.equips);
        });
        $scope.load($stateParams.id);
    });
