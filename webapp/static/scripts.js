$(document).ready(function () {
  var i;
  for (i = 0; i < $("#results > .row").length; i++) {
    var link = $("#result-"+i+"-link");
    (function () {
      var position = i;
      link.click(function () {
        var url = $(this).attr("href");
        mixpanel.track("link-click", {"position": position, "url": url});
      });
    })();
  }
});
