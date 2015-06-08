'use strict';

angular.module('leaguegenApp')
    .controller('JornadaGeneratorController', function ($scope, $stateParams, Jornada, GrupPers, equipPers, Partit, Temporada, JornPers,$state) {
        $scope.temporadas = [];
        $scope.grups = [];
        $scope.equips = [];
        $scope.data_inici = new Date();


        $scope.$watch(function () {
            return $scope.temporada
        }, function () {
            if ($scope.temporada)
                $scope.loadGrups($scope.temporada.id);
        });

        $scope.$watch(function () {
            return $scope.grup;
        }, function () {
            if ($scope.grup)
                $scope.loadEquips($scope.grup.id);
        });

        $scope.loadTemporada = function () {
            Temporada.query(function (result) {
                $scope.temporadas = result;
            });
        };
        $scope.loadTemporada();

        $scope.loadGrups = function (tempo) {
            GrupPers.grupByTemp.query({id: tempo}, function (result) {
                $scope.grups = result;
            });
        };

        $scope.loadEquips = function (grup) {
            equipPers.equipsByGrup.query({id: grup}, function (result) {
                $scope.equips = result;
            });
        };

        $scope.generateLeague = function () {
            if ($scope.temporada && $scope.grup ) {
                var data = $scope.data_inici;
                var d = data.getFullYear() + "-" + (parseInt(data.getMonth()) + 1) + "-" + data.getDate();
                console.log(d);
                JornPers.generate.get({id: $scope.grup.id, data: d},
                    function (msg) {
                        $state.go('jornada');
                    }, function (err) {
                        alert('Error')
                        console.log(err);
                    })
            }
        }
    });
