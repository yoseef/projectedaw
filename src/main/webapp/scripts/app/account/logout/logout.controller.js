'use strict';

angular.module('leaguegenApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
