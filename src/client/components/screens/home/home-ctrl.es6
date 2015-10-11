app.controller('HomeCtrl', ($element, $timeout, API, $scope, State, $state) => {

    var groups = [];

    var getGroups = () => {
        return groups;
    };

    var loadGroups = () => {
        API.getGroups({}).then((response) => {
            groups = response;
        });
    };

    var init = () => {
        $timeout(() => $element.find('[screen]').addClass('active'), 50);
        loadGroups();
    };

    init();

    $scope.getGroups = getGroups;
});



