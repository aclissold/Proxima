Parse.Cloud.afterSave("UserComment", function(installationId, master, object, user) {
    var createdBy = object.createdBy;
    push("New comment from " + createdBy);
    response.success();
});

Parse.Cloud.afterSave("UserPhoto", function(request, response) {
    if (request.object.existed()) { return; }

    var userQuery = new Parse.Query(Parse.User);
    userQuery.withinKilometers("location", request.object.get("location"), 0.5);
    userQuery.notEqualTo("username", request.user.getUsername());

    var pushQuery = new Parse.Query(Parse.Installation);
    pushQuery.matchesQuery("user", userQuery);

    Parse.Push.send({
        where: pushQuery,
        data: {
            alert: "hello, nearby user"
        }
    });
});

var push = function(alert) {
    var installation = new Parse.Query(Parse.Installation);
    Parse.Push.send({
        where: installation,
        data: {
            alert: alert,
            sound: "default"
        }
    });
}
