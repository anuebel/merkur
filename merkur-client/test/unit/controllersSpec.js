'use strict';

describe('Controllers', function () {

  // Controller-Modul laden
  beforeEach(module('merkurClientApp.controllers'));

  describe('MainCtrl', function () {

    var scope,
        defaultMaxEntriesShown,
        defaultWebsocketEndpoints,
        ctrl;

    // Controller initialisieren
    beforeEach(inject(['$rootScope','$controller','$window',function ($rootScope, $controller, $window, $filter, $timeout, $log) {
      scope = $rootScope.$new();
      defaultMaxEntriesShown = 100;
      defaultWebsocketEndpoints = ['dummyEndpoint'];
      ctrl = $controller('MainCtrl', {
        $scope: scope,
        '$window': $window,
        'defaultWebsocketEndpoints': defaultWebsocketEndpoints,
        'defaultMaxEntriesShown': defaultMaxEntriesShown,
        '$filter':$filter,
        '$timeout':$timeout,
        '$log':$log
      });
    }]));

    it('should create an empty websocket endpoint', function () {
      expect(scope.websocketEndpoint).toEqual('');
    });

    it('should contain a list of websocket endpoints', function () {
      expect(scope.websocketEndpoints).toBeDefined();
      // TODO -> to be an array oder so
    });

    it('should have a Flag indicating the current connection state initialized to false', function () {
      expect(scope.connected).toBeFalsy();
    });

    it('should create an empty source of log messages', function () {
      expect(scope.logMessagesSource).toEqual('');
    });

    it('should create an empty selected log subscription', function () {
      expect(scope.selectedLogSubscription).toEqual('');
    });

    it('should create an empty filter for log messages', function () {
      expect(scope.filter).toEqual('');
    });

    it('should create an empty list of log messages', function () {
      expect(scope.logMessages.length).toBeDefined();
      expect(scope.logMessages.length).toBe(0);
    });

    it('should create an empty list of log subscriptions', function () {
      expect(scope.logSubscriptions.length).toBeDefined();
      expect(scope.logSubscriptions.length).toBe(0);
    });

    it('should create a max number for log messages initialised with a constant value', function () {
      expect(scope.maxEntriesShown).toBe(defaultMaxEntriesShown);
    });

  });
});