'use strict';

angular.module('leaguegenApp')
    .controller('JugadorController', function ($scope, Jugador, Equip, JugadorSearch) {
        $scope.jugadors = [];
        $scope.equips = Equip.query();
        $scope.loadAll = function() {
            Jugador.query(function(result) {
               $scope.jugadors = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Jugador.get({id: id}, function(result) {
                $scope.jugador = result;
                $('#saveJugadorModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.jugador.id != null) {
                Jugador.update($scope.jugador,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Jugador.save($scope.jugador,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Jugador.get({id: id}, function(result) {
                $scope.jugador = result;
                $('#deleteJugadorConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Jugador.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteJugadorConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            JugadorSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.jugadors = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveJugadorModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.jugador = {nom: null, gols: null, any_neix: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
