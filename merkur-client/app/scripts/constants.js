'use strict';

/**
* merkurClientApp.constants Modul
*
* Stellt Konstanten bereit.
*/
var merkurClientAppConstants = angular.module('merkurClientApp.constants', []);

// Endpunkt-Adresse für die Kommunikation mit dem Websocket-Server
merkurClientAppConstants.constant('messages.serverWebsocketEndpoint', 'http://localhost:9090/merkur-server/socket');

// Anzahl maximal angezeigter Nachrichten
merkurClientAppConstants.constant('messages.maxEntriesShown', 10000);