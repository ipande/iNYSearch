# Project 2 - New York Times Search Client

NYTimesSearch is an android app that allows a user to search for images on web using simple filters. The app utilizes [New York Times Search API](http://developer.nytimes.com/docs/read/article_search_api_v2).

Time spent: 12 hours spent in total

## User Stories

The following **required** functionality is completed:

*  User can **search for news article** by specifying a query and launching a search. Search displays a grid of image results from the New York Times Search API.
* User can click on "settings" which allows selection of **advanced search options** to filter results
  *  User can configure advanced search filters such as:
  *  Begin Date (using a date picker)
  *  News desk values (Arts, Fashion & Style, Sports)
  *  Sort order (oldest or newest)
* Subsequent searches have any filters applied to the search results
* User can tap on any image in results to see the full text of article **full-screen**
* User can **scroll down to see more articles**. The maximum number of articles is limited by the API search.

The following **optional** features are implemented:

* Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
* Used the **ActionBar SearchView** or custom layout as the query box instead of an EditText
* User can **share an article link** to their friends or email it to themselves
* Replaced Filter Settings Activity with a lightweight modal overlay
* Improved the user interface and experiment with image assets and/or styling and coloring
  *  Added placeholder gif
  *  Added custom Icon
  *  Added dynamic resizing for images

The following **bonus** features are implemented:

* Use the [RecyclerView](http://guides.codepath.com/android/Using-the-RecyclerView) with the `StaggeredGridLayoutManager` to display improve the grid of image results
* Apply the popular [Butterknife annotation library](http://guides.codepath.com/android/Reducing-View-Boilerplate-with-Butterknife) to reduce view boilerplate.
* Leverage the popular [GSON library](http://guides.codepath.com/android/Using-Android-Async-Http-Client#decoding-with-gson-library) to streamline the parsing of JSON data.

The following **additional** features are implemented:
* Added 2 fragments for handling the search filter
* Added dynamic resizing for images
* Optimized picasso to load images by calculating image height and width before image is loaded


## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='NYTimes.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android

## License

    Copyright 2016 [Ishan Pande]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.