package com.ioi.universe.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.junit.jupiter.api.Test;

import java.util.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
class User {
    @JsonProperty(index = 2)
    private String name;
    @JsonProperty(index = 1)
    private String id;
    private List datas;
    private Date date;

}

public class JsonUtilTest {

    @Test
    public void simpleJson() {
        System.out.println("test");
//        User user = new User("1", "ioi", Arrays.asList("hello", "world"));
        User user = new User(null, null, null, new Date());
        System.out.println(JsonUtil.toJson(user));
        String json = JsonUtil.toJson(user);
        User user1 = JsonUtil.parseObject(json, User.class);
        System.out.println(user1.toString());
    }

    @Test
    public void toList() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User("1", "ioi", Arrays.asList("hello", "world"), new Date());
            users.add(user);
        }
        String json = JsonUtil.toJson(users);
        System.out.println(json);
        List<User> list = JsonUtil.toList(json, User.class);
        for (User user : list) {
            System.out.println(user.toString());
        }
    }

    @Test
    public void toMap() {
        User user = new User("1", "ioi", Arrays.asList("hello", "world"), new Date());
        String json = JsonUtil.toJson(user);
        Map<String, Object> map = JsonUtil.toMap(json, String.class, Object.class);
        map.forEach((k,v) -> System.out.println(k+"=="+v));

    }

    @Test
    public void toJsonNode() {
        /*User user = new User("1", "ioi", Arrays.asList("hello", "world"), new Date());
        String json = JsonUtil.toJson(user);*/
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User("1", "ioi", Arrays.asList("hello"+i, "world"), new Date());
            users.add(user);
        }
        String json = JsonUtil.toJson(users);
        JsonNode jsonNode = JsonUtil.toJsonNode(json);
        List<String> firstDatas = JsonUtil.findVauleFirst(jsonNode, "datas", List.class);
        for (String data : firstDatas) {
            System.out.println(data);
        }
        List lastDatas = JsonUtil.findVauleLast(jsonNode, "datas", List.class);
        for (Object data : lastDatas) {
            System.out.println(data);
        }
        System.out.println("===============");
        List<List> datas = JsonUtil.findVaules(jsonNode, "datas", List.class);
        for (List data : datas) {
            System.out.println("-------------------");
            data.forEach(System.out::println);
        }
    }
}
