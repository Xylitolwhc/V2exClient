package Net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import FromGson.GsonUtil;
import Items.ContentDetail;
import Items.TopicsFromJson;
import Items.TopicsFromJsoup;

/**
 * Created by 吴航辰 on 2016/12/1.
 */

public class ConnectInternet {
    private static ConnectInternet connectInternet = null;

    private ConnectInternet() {
    }

    public static ConnectInternet getInstance() {
        if (connectInternet == null) {
            connectInternet = new ConnectInternet();
        }
        return connectInternet;
    }

    public List<TopicsFromJson> getTheHottestList(String url) throws Exception {
        List<TopicsFromJson> topicsFromJsonList = new ArrayList<>();

        HttpURLConnection connection = null;
        try {
            URL myUrl = new URL(url);
            connection = (HttpURLConnection) myUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            topicsFromJsonList = GsonUtil.parseJsonArrayWithGson(response.toString(), TopicsFromJson.class);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            return topicsFromJsonList;
        }
    }

    public List<ContentDetail> getContentDetailList(String url) throws Exception {
        List<ContentDetail> contentDetailList = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Element poster = document.getElementsByClass("box").get(0);
            Element userBox = document.getElementsByClass("box").get(1);
            Elements allUsers = userBox.getElementsByClass("cell");

            ContentDetail posterDetail = new ContentDetail();
            posterDetail.setImageUrl(poster.select("img[src]").attr("src").toString());
            posterDetail.setTitle(poster.getElementsByTag("h1").text().toString());
            posterDetail.setUsername(poster.getElementsByClass("gray").select("a[href]").first().text().toString());
            posterDetail.setReplyContent(poster.getElementsByClass("topic_content").first().text().toString());
            contentDetailList.add(posterDetail);
            for (int i = 0; i < allUsers.size(); i++) {
                if (allUsers.get(i).id().toString().length() != 0 ||true) {
                    Element user = allUsers.get(i);
                    String imgUrl = user.select("img[src]").first().attr("src").toString();
                    String replyContent = user.getElementsByClass("reply_content").first().text();
                    String userName = user.select("a[href]").first().text();
                    String detail = user.getElementsByClass("fade small").first().text().toString();

                    ContentDetail contentDetail = new ContentDetail();
                    contentDetail.setReplyContent(replyContent);
                    contentDetail.setUsername(userName);
                    contentDetail.setImageUrl(imgUrl);
                    contentDetail.setDetail(detail);
                    contentDetailList.add(contentDetail);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return contentDetailList;
        }
    }

    public List<TopicsFromJsoup> getTopicsFromJsoup(String url) throws Exception {
        List<TopicsFromJsoup> topicsFromJsoupList = new ArrayList<>();
        Log.d("Internet", "Go" + url);
        try {
            Document document = Jsoup.connect(url).get();
            Elements topics = document.getElementsByClass("cell").select("tbody");
            for (int i = 0; i < topics.size(); i++) {
                Element topic = topics.get(i);
                Log.d("test",topic.html());
                String imgUrl = topic.select("img[src]").first().attr("src").toString();
                String title = topic.getElementsByClass("item_title").first().select("a[href]").text().toString();
                String itemUrl="https://www.v2ex.com"+topic.getElementsByClass("item_title").first().select("a[href]").attr("href").toString();
                String detail = topic.getElementsByClass("small fade").first().text().toString();
                //String username = topic.getElementsByClass("small fade").first().text().toString();
                String Replies = "";
                if (topic.hasClass("count_livid")) {
                    Replies = topic.getElementsByClass("count_livid").first().text().toString();
                }
                Log.d("Detail", imgUrl + "\n" + title + "\n" + detail + "\n" + Replies);
                TopicsFromJsoup topicsFromJsoup = new TopicsFromJsoup();
                topicsFromJsoup.setImgUrl(imgUrl);
                topicsFromJsoup.setTitle(title);
                topicsFromJsoup.setUrl(itemUrl);
                // topicsFromJsoup.setUsername(username);
                topicsFromJsoup.setDetail(detail);
                topicsFromJsoup.setLastReply(Replies);
                topicsFromJsoupList.add(topicsFromJsoup);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return topicsFromJsoupList;
        }
    }

    public Bitmap getPicture(String imgUrl) throws Exception {
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            URL imgurl = new URL(imgUrl);
            connection = (HttpURLConnection) imgurl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            InputStream In = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(In);
        } finally {
            connection.disconnect();
            return bitmap;
        }
    }
}

