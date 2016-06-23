'use strict';

describe('Controller Tests', function() {

    describe('SearchHistory Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSearchHistory;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSearchHistory = jasmine.createSpy('MockSearchHistory');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SearchHistory': MockSearchHistory
            };
            createController = function() {
                $injector.get('$controller')("SearchHistoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'plagueheartApp:searchHistoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
