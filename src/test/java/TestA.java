/**
 * Created by Administrator on 2018/9/20.
 */
import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestA {
    public static void main(String[] args){


        try {
            String str="1,,2,3";

           String[] arr = str.split(",",4);

           System.out.println(arr.length);



        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
