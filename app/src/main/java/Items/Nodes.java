package Items;

/**
 * Created by 吴航辰 on 2016/12/6.
 */

public class Nodes {
    private int id, topics;
    private String name, url, title, title_alternative, header, footer, created;

    public void setTopics(int topics) {
        this.topics = topics;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_alternative() {
        return title_alternative;
    }

    public void setTitle_alternative(String title_alternative) {
        this.title_alternative = title_alternative;
    }

    public int getTopics() {
        return topics;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
