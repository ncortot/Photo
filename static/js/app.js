'use strict';

/* Main App Module */

angular.module('photo', ['photo.services', 'photo.controllers', 'photo.filters', 'photo.directives']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.
      when('/browse', {
        templateUrl: '/static/partials/photo-list.html',
        controller: 'PhotoListCtrl'
      }).
      when('/detail/:photoId', {
        templateUrl: '/static/partials/photo-detail.html',
        controller: 'PhotoDetailCtrl'
      }).
      otherwise({redirectTo: '/browse'});
  }]);
