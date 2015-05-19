'use strict';

angular.module('leaguegenApp')
    .controller('PartitController', function ($scope, Partit, Jornada, Equip, Franja, PartitSearch) {
        $scope.partits = [];
        $scope.jornadas = Jornada.query();
        $scope.equips = Equip.query();
        $scope.franjas = Franja.query();
        $scope.loadAll = function() {
            Partit.query(function(result) {
               $scope.partits = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Partit.get({id: id}, function(result) {
                $scope.partit = result;
                $('#savePartitModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.partit.id != null) {
                Partit.update($scope.partit,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Partit.save($scope.partit,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Partit.get({id: id}, function(result) {
                $scope.partit = result;
                $('#deletePartitConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Partit.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePartitConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            PartitSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.partits = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#savePartitModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.partit = {nom_v: null, nom_l: null, gols_v: null, gols_l: null, data: null, arbitre: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
