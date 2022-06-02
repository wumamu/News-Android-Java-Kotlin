# NewsMoment
a smartphone news-aggregation app that logs its users’ reading behavior and sends pushed news notifications. 
### Feature
1. Our UI included a nested tab bar. The top tab bar is the news sources bar, which contains a list of the nine selected news sources that the app aggregatesnews from. NewsMoment users can determine which of these nine sources they want to receive news and news-related push notifications.
2. The tab bar second from the top is the news classification bar. The list of news-classification tabs that appears under each news source is identical to that organization’s own such lists.
3. News content was crawled from the website of each news organization every 10 minutes, and included images, text, and ad text; all of this was then displayed in NewsMoment.
4. Also pushed news notifications from users’ existing news apps were suppressed, such that participants would not be disturbed by receiving multiple notifications about the same news item.
5. NewsMoment logs its users’ news-browsing and news-reading behaviors. Specifically, it tracks the position of the user’s viewport in the news.
6. The app also logs users’ actions such as scrolling, entry and exit, and use of the Android built-in sharing function. These logs allowed us to track what area within a news item each of our participants was focused on at any given time, from which we were able to generate advanced metrics such as dwell time of each viewport, scrolling speed (changes in viewports per second), and variances in scrolling speed.
7. NewsMoment logged our participants’ actions during article reading, including detailed of scroll gesture; whether the article was accessed through notifications or not, and shared or not; total dwell time.
8. NewsMoment collected sensor data to analyze.

Note:
*新聞列表有圖片版本到 NewsRecycleViewAdapter, ReadingHistoryRecycleViewAdapter imgView 打開

### screenshot
<p float="left">
  <img src="https://github.com/wumamu/NewsMoment-Android-/blob/main/NewsMoment%20Screenshot/Screenshot_20220221-005919.png" width="150" />
  <img src="https://github.com/wumamu/NewsMoment-Android-/blob/main/NewsMoment%20Screenshot/Screenshot_20220221-001133.png" width="150" />
  <img src="https://github.com/wumamu/NewsMoment-Android-/blob/main/NewsMoment%20Screenshot/ss%20(3).png" width="150" />
  <img src="https://github.com/wumamu/NewsMoment-Android-/blob/main/NewsMoment%20Screenshot/ss%20(4).png" width="150" />
  
</p>
<p float="left">
  <img src="https://github.com/wumamu/NewsMoment-Android-/blob/main/NewsMoment%20Screenshot/ss%20(6).png" width="150" />
  <img src="https://github.com/wumamu/NewsMoment-Android-/blob/main/NewsMoment%20Screenshot/ss%20(7).png" width="150" />
  <img src="https://github.com/wumamu/NewsMoment-Android-/blob/main/NewsMoment%20Screenshot/ss%20(1).png" width="150" />
  <img src="https://github.com/wumamu/NewsMoment-Android-/blob/main/NewsMoment%20Screenshot/Screenshot_20220221-001246.png" width="150" />
</p>
<!-- ![image]
![image]
![image]
![image]
![image](https://github.com/wumamu/NewsMoment-Android-/blob/main/NewsMoment%20Screenshot/ss%20(1).png) -->

### Apk DownloadLink (version 22.05.31-2)
https://drive.google.com/file/d/1Rpl8RK218Sm3ucsXb_4V0UmmN4jgsAo6/view?usp=sharing
### Figma 
https://www.figma.com/file/jzv84uzQUCqguhb6DXrKoC/news-moment-anntation
### Demo Video
https://youtu.be/AwhqyfGazxg
