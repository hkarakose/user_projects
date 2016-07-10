// look for oauth_pin
pin = document.getElementById("oauth_pin");

// send the pin to the extension
var port = chrome.extension.connect();
if(pin) {
  pin = pin.innerHTML.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
  port.postMessage({"success": true, "pin": pin});
} else {
  port.postMessage({"success": false})
}