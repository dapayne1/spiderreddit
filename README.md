SpiderReddit RSS Reader (Demo)
==============================

Barebones, simple demonstration of an Android RSS Reader for the site [www.reddit.com][1].

- [Features] (#features)
- [Build Instructions] (#build)
- [Third Party Libraries] (#libraries)

# Features

* Asynchronously fetches RSS feeds from any specified RSS URL for www.reddit.com, such as [www.reddit.com/.rss][2] or any of its subreddits such as [www.reddit.com/r/madmen/.rss][3].
* Articles display in WebViews that cache. In case of network connectivity issues, articles still populate nicely.
* RSS feed listings are stored in an ORMLite local database, for more offline handling capabilities and for handling state changes on device quicker.
* Plays well on both phones and tablets; for tablets, SpiderReddit has responsive UI with some sweet side-by-side navigational action going on in landscape mode.
* Each article that comes up is delivered to you by your friendly neighborhood web-slinger. You'll see.

# Build Instructions

Built and Compiled with Android Studio 0.5.8. I'm a bit new to the IDE myself, having been an Eclipse man for many years, so I can offer few words of advice save for pointing one towards [http://developer.android.com/sdk/installing/studio.html][4], and that the "Sync Project With Gradle Files" button will become your good friend.

# Third Party Libraries

* [ormlite-core-4.48.jar][5] & [ormlite-android-4.48.jar][6] : For ORMLite SQL local databases.
* [Simple-Rss2-Android.jar][7] : simple RSS 2.0 Parser Library for Android.

[1]: http://www.reddit.com
[2]: www.reddit.com/.rss
[3]: www.reddit.com/r/madmen/.rss
[4]: http://developer.android.com/sdk/installing/studio.html
[5]: http://ormlite.com/releases/
[6]: http://ormlite.com/releases/
[7]: https://github.com/salendron/Simple-Rss2-Android
