'use strict';

angular.module('leaguegenApp')
    .controller('GrupController', function ($scope, Grup, Temporada, Equip, Jornada, GrupSearch) {
        $scope.grups = [];
        $scope.temporadas = Temporada.query();
        //$scope.equips = Equip.query();
        //$scope.jornadas = Jornada.query();
        $scope.loadAll = function() {
            Grup.query(function(result) {
               $scope.grups = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Grup.get({id: id}, function(result) {
                $scope.grup = result;
                $('#saveGrupModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.grup.id != null) {
                Grup.update($scope.grup,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Grup.save($scope.grup,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Grup.get({id: id}, function(result) {
                $scope.grup = result;
                $('#deleteGrupConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Grup.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteGrupConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            GrupSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.grups = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveGrupModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.grup = {nom: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
