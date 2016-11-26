package FromGson;

import android.graphics.Bitmap;

/**
 * Created by 吴航辰 on 2016/11/24.
 */

public class TheHottest {
    private int id, replies, created, last_modified, last_touched;
    private String title, url, content;
    private Member member;
    private Node node;
    private Bitmap avatar_mini=null, avatar_normal=null, avatar_large=null;

    public class Member {
        private int id;
        private String username, tagline, avatar_mini, avatar_normal, avatar_large;
    }

    public class Node {
        private int id;
        private String name, title, title_alternative, url, topics, avatar_mini, avatar_normal, avatar_large;
    }

    public int getInt(String query) {
        switch (query) {
            case "id":
                return id;
            case "replies":
                return replies;
            case "created":
                return created;
            case "last_modified":
                return last_modified;
            case "last_touched":
                return last_touched;

            case "memberid":
                return member.id;

            case "nodeid":
                return node.id;
        }
        return 0;
    }

    public String getString(String query) {
        switch (query) {//name,title,title_alternative,url,topicsavatar_mini,avatar_normal,avatar_large;
            case "title":
                return title;
            case "url":
                return url;
            case "content":
                return content;

            case "username":
                return member.username;
            case "tagline":
                return member.tagline;
            case "avatar_mini":
                return member.avatar_mini;
            case "avatar_normal":
                return member.avatar_normal;
            case "avatar_large":
                return member.avatar_large;

            case "name":
                return node.name;
            case "nodetitle":
                return node.title;
            case "title_alternative":
                return node.title_alternative;
            case "nodeurl":
                return node.url;
            case "topics":
                return node.topics;
            case "nodeavatar_mini":
                return node.avatar_mini;
            case "nodeavatar_normal":
                return node.avatar_normal;
            case "nodeavatar_large":
                return node.avatar_large;
        }
        return "";
    }

    public void setBitmap(String bitmapName, Bitmap bitmap) {
        switch (bitmapName) {
            case "avatar_mini":
                avatar_mini = bitmap;
                break;
            case "avatar_normal":
                avatar_normal = bitmap;
                break;
            case "avatar_large":
                avatar_large = bitmap;
                break;
        }
    }

    public Bitmap getBitmap(String query) {
        switch (query) {
            case "avatar_mini":
                return avatar_mini;
            case "avatar_normal":
                return avatar_normal;
            case "avatar_large":
                return avatar_large;
        }
        return null;
    }
}
