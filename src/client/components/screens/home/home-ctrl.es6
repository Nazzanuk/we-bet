app.controller('HomeCtrl', ($element, $timeout, API, $scope, State, Alert) => {

    var groups = [];

    var getGroups = () => {
        return groups;
    };

    var loadGroups = () => {
        API.getGroups({})
            .then((response) => {
            groups = response;
            $element.find('[screen]').addClass('active')
        });
    };

    var init = () => {
        loadGroups();
    };

    init();

    $scope.getGroups = getGroups;
});



