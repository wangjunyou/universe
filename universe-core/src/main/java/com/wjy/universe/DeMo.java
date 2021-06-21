package com.wjy.universe;

import java.util.ArrayList;

public class DeMo {
    private static final ArrayList<String> author = new ArrayList<>();
    static{
        author.add("author: wangjunyou");
    }
    public static void main(String[] args) {
        author.forEach(System.out::println);
    }
}
