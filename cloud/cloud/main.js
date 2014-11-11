Parse.Cloud.afterSave("UserComment", function(installationId, master, object, user) {
    var createdBy = object.get("createdBy");
    push("New comment from " + createdBy);
    response.success();
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
