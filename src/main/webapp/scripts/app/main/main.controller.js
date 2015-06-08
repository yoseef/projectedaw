'use strict';

angular.module('leaguegenApp')
    .controller('MainController', function ($scope, Principal, Temporada, GrupPers, equipPers, $state) {
        $scope.temporadas = [];
        $scope.grups = [];
        $scope.equips = [];

        $scope.$watch(function () {
            return $scope.temporada
        }, function () {
            if ($scope.temporada)
                $scope.loadGrups($scope.temporada.id);
        });

        $scope.$watch(function () {
            return $scope.grups;
        }, function () {
            $scope.loadEquipsByGrup();
        });

        $scope.loadTemporada = function () {
            Temporada.query(function (result) {
                $scope.temporadas = result;
                $scope.temporada = $scope.temporadas[0];
            });
        };
        $scope.loadTemporada();

        $scope.loadGrups = function (tempo) {
            GrupPers.grupByTemp.query({id: tempo}, function (result) {
                $scope.grups = result;
            });
        };

        $scope.loadEquipsByGrup = function () {
            for (var i = 0; i < $scope.grups.length; i++) {
                equipsByGrup($scope.grups[i]);
                //$scope.equips.push(a);
            }
        }

        function equipsByGrup(grup) {
            equipPers.equipsByGrup.query({id: grup.id}, function (res) {
                $scope.equips.push(res);
                console.log($scope.equips);
            });
        }


        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
    });
