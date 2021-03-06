package FromGson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import Items.Nodes;
import Items.Topics;

/**
 * Created by 吴航辰 on 2016/11/25.
 */

 /*
 * 封装的GSON解析工具类，提供泛型参数
 */
public class GsonUtil {
    // 将Json数据解析成相应的映射对象
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }

    // 将Json数组解析成相应的映射对象列表
    public static <T> List<Topics> parseTopicsJsonArrayWithGson(String jsonData) {
        Gson gson = new Gson();
        List<Topics> result = gson.fromJson(jsonData, new TypeToken<List<Topics>>() {
        }.getType());
        return result;
    }
    public static <T> List<Nodes> parseNodesJsonArrayWithGson(String jsonData) {
        Gson gson = new Gson();
        List<Nodes> result = gson.fromJson(jsonData, new TypeToken<List<Nodes>>() {
        }.getType());
        return result;
    }
}
