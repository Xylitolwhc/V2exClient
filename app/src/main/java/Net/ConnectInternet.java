package Net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.markdown4j.Markdown4jProcessor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import FromGson.GsonUtil;
import Items.TopicDetail;
import Items.Nodes;
import Items.Topics;

/**
 * Created by 吴航辰 on 2016/12/1.
 */

public class ConnectInternet {

    public static List<Topics> getTheHottestList(String url) throws Exception {
        List<Topics> topicsFromJsonList = new ArrayList<>();
        if (url.substring(url.length() - 5).equals(".json")) {
            try {
                topicsFromJsonList = GsonUtil.parseTopicsJsonArrayWithGson(getHTML(url));
            } finally {
                return topicsFromJsonList;
            }
        } else {
            try {
                Document document = Jsoup.parse(getHTML(url));
                Elements topics = document.getElementsByClass("cell").select("tbody");
                for (int i = 0; i < topics.size(); i++) {
                    Element topic = topics.get(i);
                    Log.d("topics", topic.html());
                    String imgUrl = topic.select("img[src]").first().attr("src").toString();
                    String title = topic.getElementsByClass("item_title").first().select("a[href]").text().toString();
                    String itemUrl = "https://www.v2ex.com" + topic.getElementsByClass("item_title").first().select("a[href]").attr("href").toString();
                    String username = topic.getElementsByClass("small fade").first().select("strong").text();
                    int Replies = 0;
                    if (!topic.select(".count_livid").html().equals("")) {
                        Replies = Integer.valueOf(topic.select(".count_livid").html().replace(" ", ""));
                    }
                    Log.d("Replies", topic.select(".count_livid").html());
                    Topics topicsFromJson=new Topics();
                    topicsFromJson.setTitle(title);
                    Log.d("url",url);
                    topicsFromJson.setUrl(itemUrl);
                    topicsFromJson.setReplies(Replies);
                    topicsFromJson.member.setUsername(username);
                    topicsFromJson.member.setAvatar_mini(imgUrl);
                    topicsFromJsonList.add(topicsFromJson);

                }
            } catch (Exception e) {
                Log.d("WrongUrl", url + ",newUrl:" + url);
                e.printStackTrace();
            } finally {
                return topicsFromJsonList;
            }
        }
    }

    private static String getHTML(String url) throws Exception {
        url = url.replace(" ", "").replace("http:", "https:");
        HttpURLConnection connection = null;
        StringBuilder response = new StringBuilder();
        try {
            URL myUrl = new URL(url);
            connection = (HttpURLConnection) myUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } finally {
            connection.disconnect();
            return response.toString();
        }
    }

    public static List<TopicDetail> getContentDetailList(String url) throws Exception {
        List<TopicDetail> contentDetailList = new ArrayList<>();
        try {
            int page = 1, lastReply = 0, theEndReply = 0;
            if (page == 1) {
                Markdown4jProcessor markdown4jProcessor = new Markdown4jProcessor();
                Document document = Jsoup.parse(getHTML(url + "?p=1"));
                Element poster = document.getElementsByClass("box").get(0);
                TopicDetail posterDetail = new TopicDetail();
                posterDetail.setImageUrl(poster.select("img[src]").attr("src").toString());
                posterDetail.setTitle(poster.getElementsByTag("h1").text().toString());
                posterDetail.setUsername(poster.getElementsByClass("gray").select("a[href]").first().text().toString());
                posterDetail.setReplyContent(Html.fromHtml(poster.select("div.topic_content").html()));
                posterDetail.setReplyContentHtml(markdown4jProcessor.process(poster.select("div.topic_content").html()));
                contentDetailList.add(posterDetail);
                if (!document.getElementsByClass("box").get(1).select("div.inner").text().equals("目前尚无回复")) {
                    Element allReplies = document.getElementsByClass("box").get(1).getElementsByClass("gray").first();
                    posterDetail.setDetail(Html.fromHtml(allReplies.html()).toString());
                    lastReply = Integer.valueOf(allReplies.text().substring(0, allReplies.text().indexOf("回复")).replace(" ", ""));
                }
            }
            do {
                Document document = Jsoup.parse(getHTML(url + "?p=" + page));
                if (document.getElementsByClass("box").size() > 1) {
                    Element allReplies = document.getElementsByClass("box").get(1);
                    Elements allUsers = allReplies.getElementsByClass("cell");
                    for (int i = 0; i < allUsers.size(); i++) {
                        if (allUsers.get(i).id().toString().length() != 0) {
                            contentDetailList.add(getContentDetail(allUsers.get(i)));
                        }
                    }
                    if (!allReplies.select("div.inner").text().equals("目前尚无回复")) {
                        Element firstComment = document.getElementsByClass("inner").first();
                        TopicDetail contentDetail = getContentDetail(firstComment);
                        theEndReply = contentDetail.getFloor();
                        contentDetailList.add(contentDetail);
                    }
                }
                page++;
            } while (lastReply != theEndReply);
        } finally {
            return contentDetailList;
        }
    }

    private static TopicDetail getContentDetail(Element content) throws Exception {
        Markdown4jProcessor markdown4jProcessor = new Markdown4jProcessor();
        String imgUrl = content.select("img[src]").first().attr("src").toString();
        Spanned replyContent = Html.fromHtml(content.select("div.reply_content").html());
//        String replyContentHtml = content.select("div.reply_content").html();
//        String replyContentHtml = markdown4jProcessor.process(content.select("div.reply_content").html());
        String replyContentHtml = content.select("div.reply_content").html();

        String userName = content.select("a[href]").first().text();
        String detail = content.getElementsByClass("fade small").first().text().toString();
        int floor = Integer.valueOf(content.getElementsByClass("no").first().text());

        TopicDetail contentDetail = new TopicDetail();
        contentDetail.setReplyContent(replyContent);
        contentDetail.setReplyContentHtml(replyContentHtml);
        contentDetail.setUsername(userName);
        contentDetail.setImageUrl(imgUrl);
        contentDetail.setDetail(detail);
        contentDetail.setFloor(floor);
        return contentDetail;
    }

    public static Bitmap getPicture(String imgUrl) {
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        imgUrl = imgUrl.replace(" ", "");
        if (!imgUrl.substring(0, 4).equals("http")) {
            imgUrl = "http:" + imgUrl;
        }
        try {
            URL imgurl = new URL(imgUrl);
            connection = (HttpURLConnection) imgurl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            InputStream In = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(In);
        } catch (Exception e) {
            Log.d("WrongImgUrl", imgUrl);
            e.printStackTrace();
        } finally {
            connection.disconnect();
            return bitmap;
        }
    }

    public static List<Nodes> nodesList(String url) {
        List<Nodes> nodesList = new ArrayList<>();

        HttpURLConnection connection = null;
        try {
            nodesList = GsonUtil.parseNodesJsonArrayWithGson(getHTML(url));
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            return nodesList;
        }
    }
}

