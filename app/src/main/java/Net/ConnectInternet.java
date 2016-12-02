package Net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
import Items.TheHottest;

/**
 * Created by 吴航辰 on 2016/12/1.
 */

public class ConnectInternet {
    private static ConnectInternet connectInternet=null;

    private ConnectInternet() {
    }

    public static ConnectInternet getInstance(){
        if (connectInternet==null){
            connectInternet=new ConnectInternet();
        }
        return connectInternet;
    }

    public List<TheHottest> getTheHottestList(String url) throws Exception {
        List<TheHottest> theHottestList = new ArrayList<>();

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
            theHottestList = GsonUtil.parseJsonArrayWithGson(response.toString(), TheHottest.class);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            return theHottestList;
        }
    }

    public List<ContentDetail> getContentDetailList(String url) throws Exception{
        List<ContentDetail> contentDetailList=new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Element userBox = document.getElementsByClass("box").get(1);
            Elements allUsers = userBox.getElementsByClass("cell");

            for (int i = 0; i < allUsers.size(); i++) {
                if (allUsers.get(i).id().toString().length() != 0) {
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
        }finally {
            return contentDetailList;
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

