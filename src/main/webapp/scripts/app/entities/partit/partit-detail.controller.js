'use strict';

angular.module('leaguegenApp')
    .controller('PartitDetailController', function ($scope, $stateParams, Partit, Jornada, Equip, Franja) {
        $scope.partit = {};
        $scope.load = function (id) {
            Partit.get({id: id}, function(result) {
              $scope.partit = result;
            });
        };
        $scope.load($stateParams.id);
    });
