'use strict';

angular.module('leaguegenApp')
    .controller('SettingsController', function ($scope, Principal, Auth, imagee) {
        $scope.success = null;
        $scope.error = null;
        Principal.identity(true).then(function(account) {
            $scope.settingsAccount = account;
        });

        $scope.save = function () {
            Auth.updateAccount($scope.settingsAccount).then(function() {
                $scope.error = null;
                $scope.success = 'OK';
                Principal.identity().then(function(account) {
                    $scope.settingsAccount = account;
                });
            }).catch(function() {
                $scope.success = null;
                $scope.error = 'ERROR';
            });
        };
        var files;
        $scope.setFile = function (f) {
            files = f;
        };
        //$scope.img;
        //imagee.get(function(data){
        //    console.log(data);
        //    $scope.img = data;
        //}, function(data){
        //    console.log("Error : " + data);
        //    $scope.img = "assets/images/hipster.png";
        //});
        $scope.uploadIt = function () {
            if (files != null && files.length > 0) {
                var filee = files[0];
                imagee.save({'file' : filee}, function (result) {
                    console.info(result.msg);
                });
            }
        };
    });
