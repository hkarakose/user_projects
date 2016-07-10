onload = setTimeout(init, 0); // workaround for http://crbug.com/24467

var timeline, template, link, image, author, content;

// initialize timeline template
function init() {
    chrome.browserAction.setBadgeText({text: ''});
    bg = chrome.extension.getBackgroundPage();
    bg.unreadCount = 0;

    getTweets();
}


// process new batch of tweets
function processTweets() {
    var res = JSON.parse(req.responseText);
    unreadCount += res.length;

    if (unreadCount > 0) {
        chrome.browserAction.setBadgeBackgroundColor({
            color: [255, 0, 0, 255]
        });
        chrome.browserAction.setBadgeText({text: '' + unreadCount});
    }


    var res = JSON.parse(req.responseText);
    tweets = res.concat(tweets);
    update();
}

function update() {
    for (var i in tweets) {
        user = tweets[i].user;
        url = 'http://twitter.com/' + user.screen_name;

        // thumbnail
        link.title = user.name;
        link.href = openInNewTab(url);
        image.src = user.profile_image_url;
        image.alt = user.name;

        // text
        author.href = openInNewTab(url);
        author.innerHTML = user.name;
        content.innerHTML = linkify(tweets[i].text);

        // copy node and update
        item = template.cloneNode(true);
        timeline.appendChild(item);
    }
}

