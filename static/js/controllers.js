'use strict';

/* Controllers */

angular.module('photo.controllers', []).
  controller('PhotoListCtrl', ['$scope', 'Browser', function($scope, Browser) {
    $scope.photos = Browser.query();
  }]).
  controller('PhotoDetailCtrl', ['$scope', '$routeParams', 'Photo', function($scope, $routeParams, Photo) {
    $scope.photo = Photo.get({photoId: $routeParams.photoId});
  }]);
