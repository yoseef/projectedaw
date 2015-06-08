'use strict';

angular.module('leaguegenApp')
    .controller('TemporadaDetailController', function ($scope, $stateParams, Temporada, Grup, GrupPers) {
        $scope.temporada = {};
        $scope.grups = [];


        $scope.load = function (id) {
            Temporada.get({id: id}, function (result) {
                $scope.temporada = result;
            });
        };

        $scope.load($stateParams.id);

        GrupPers.grupByTemp.query({id: $stateParams.id}, function (res) {
            $scope.grups = res;
            console.log($scope.grups);
        }, function (err) {
            console.log(err);
        })
    });
