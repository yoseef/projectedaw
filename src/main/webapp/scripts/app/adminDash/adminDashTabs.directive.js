'use strict';

angular.module('leaguegenApp')
    .directive('admindashtabs', function() {
        return {
            restrict: 'E',
            templateUrl: 'views/admin-dash-tabs.html',
            link: function (scope, element) {
                console.log('hola tabs');
                new CBPFWTabs( document.getElementById( 'tabs2' ) );
            }
        }
    });


