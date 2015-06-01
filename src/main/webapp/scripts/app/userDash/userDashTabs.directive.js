'use strict';

angular.module('leaguegenApp')
    .directive('userdashtabs', function() {
        return {
            restrict: 'E',
            templateUrl: 'views/user-dash-tabs.html',
            link: function (scope, element) {
                new CBPFWTabs( document.getElementById( 'tabs2' ) );
            }
        }
    });


