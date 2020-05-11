package com.example.mp3freeforyou.Ultils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {

    //chuyển đổi chữ có dấu thành không dấu
    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD); //chuyển đổi dạng input Unicode
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

        //Trả về chuỗi không có dấu nhưng có thể không đổi được "Đ" và "đ" thành "D" và "d"
        //return pattern.matcher(temp).replaceAll("");

        //Trả về chuỗi không có dấu
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "");
    }
}
