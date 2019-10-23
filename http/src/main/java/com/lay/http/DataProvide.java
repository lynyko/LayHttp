package com.lay.http;

import java.util.HashMap;
import java.util.Map;

public class DataProvide {
    private static Map<String, String> data = new HashMap<>();
    public static String getData(String path){
        return data.get(path);
    }

    static {
        // 注册
        data.put("/api/register",
                "{\n" +
                "\"resultCode\":\"0\" ,\n" +
                "\"resultMsg\":\"注册成功！\" \n" +
                "}");

        // 登录
        data.put("/api/login", "{\"response\":{\"age\":20,\"constellationName\":\"1234\",\"desc\":\"1232\",\"headImg\":\"jfewwfe\",\"labelNames\":\"[31231,213123]\",\"professionName\":\"23131\",\"regionName\":\"339889\",\"sex\":1,\"token\":\"32132131231231\",\"userAccount\":\"588428745\",\"userName\":\"44788778854\"},\"count\":\"1\",\"resultCode\":\"0\",\"resultMsg\":\"完成\"}");

        // 修改用户信息
        data.put("/api/user/save", "{\"response\":{\"age\":20,\"constellationName\":\"1234\",\"desc\":\"31231231\",\"headImg\":\"jfewwfe\",\"labelNames\":\"[31231,213123]\",\"professionName\":\"23131\",\"regionName\":\"339889\",\"sex\":2,\"token\":\"32132131231231\",\"userAccount\":\"588428745\",\"userName\":\"44788778854\"},\"count\":\"1\",\"resultCode\":\"0\",\"resultMsg\":\"完成\"}");

        // 酒会列表
        data.put("/api/party/list", "{\"resultCode\":\"0\",\"resultMsg\":\"创建成功！\",\" count \":\"1\",\"response\":[{\"partyCode\":\"IFJ2RJ329SEF\",\"partyName\":\"快乐就好1\",\"title\":\"快乐\",\"address\":\"广州天河\",\"files\":\"[/img/party/123.jpg, /img/party/456.jpg]\",\"longitude\":\"113.3924\",\"latitude\":\"23.062797\",\"distance\":\"500\",\"startDate\":\"20190811090000\",\"endDate\":\"20190811120000\",\"desc\":\"聚会说明\",\"likeCount\":\"10\",\"commentCount\":\"20\",\"hotComment\":{\"commentCode\":\"DF4EFSF3FSEF\",\"content\":\"评论内容\",\"virtualName\":\"小明\",\"floor\":\"10\",\"likeCount\":\"100\",\"commentCount\":\"12\",\"createDate\":\"20190811090000\"}},{\"partyCode\":\"IFJ2RJ329SEF\",\"partyName\":\"快乐就好1\",\"title\":\"快乐\",\"address\":\"广州天河\",\"files\":\"[/img/party/123.jpg, /img/party/456.jpg]\",\"longitude\":\"113.3944\",\"latitude\":\"23.062797\",\"distance\":\"500\",\"startDate\":\"20190811090000\",\"endDate\":\"20190811120000\",\"desc\":\"聚会说明\",\"likeCount\":\"10\",\"commentCount\":\"20\",\"hotComment\":{\"commentCode\":\"DF4EFSF3FSEF\",\"content\":\"评论内容\",\"virtualName\":\"小明\",\"floor\":\"10\",\"likeCount\":\"100\",\"commentCount\":\"12\",\"createDate\":\"20190811090000\"}}]}");
    }
}
