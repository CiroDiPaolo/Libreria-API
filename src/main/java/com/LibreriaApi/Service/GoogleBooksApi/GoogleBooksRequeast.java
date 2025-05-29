package com.LibreriaApi.Service.GoogleBooksApi;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleBooksRequeast {

    //@Value("$(api.key)")
    private static String APIKEY = "AIzaSyBHYBfwtqrFU4Whru9dxqHD9xWgh6skA68";

    public String getThumbnailByISBN(String isbn) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn + "&key=" + APIKEY;

        String response = restTemplate.getForObject(url, String.class);

        JSONObject json = new JSONObject(response);
        JSONArray items = json.optJSONArray("items");

        if (items != null && items.length() > 0) {
            JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");
            JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
            if (imageLinks != null) {
                return imageLinks.optString("thumbnail", "Sin imagen");
            }
        }
        return "";
    }


}
