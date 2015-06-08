'use strict';

angular.module('leaguegenApp')
    .controller('SettingsController', function ($scope, Principal, Auth, imagee, $state, $stateParams) {
        $scope.success = null;
        $scope.error = null;
        Principal.identity(true).then(function(account) {
            $scope.settingsAccount = account;
            if(!$scope.settingsAccount.img || $scope.settingsAccount.img == ""){
                $scope.settingsAccount.img ="assets/images/hipster.png";
            }

            });

        $scope.save = function () {
            $scope.uploadIt();
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


        $scope.uploadIt = function () {
            if (files != null && files.length > 0) {
                var filee = files[0];
                imagee.save({'file' : filee}, function (result) {
                    console.info(result.msg);
                    $state.transitionTo($state.current, $stateParams, {
                        reload: true,
                        inherit: false,
                        notify: true
                    });
                });

            }
        };
    });
