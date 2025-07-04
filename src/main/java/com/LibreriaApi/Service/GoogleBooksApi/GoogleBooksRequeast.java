package com.LibreriaApi.Service.GoogleBooksApi;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GoogleBooksRequeast {

    //@Value("$(api.key)")
    private static String APIKEY = "AIzaSyBHYBfwtqrFU4Whru9dxqHD9xWgh6skA68";

    // OBTIENE LA URL DEL THUMBNAIL DEL LIBRO
    public String getThumbnailByISBN(String isbn) {
        JSONObject volumeInfo = getVolumeInfo(isbn);
        if (volumeInfo != null) {
            JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
            if (imageLinks != null) {
                return imageLinks.optString("thumbnail", "Sin imagen");
            }
        }
        return "";
    }

    // OBTIENE EL TITULO, LA DESCRIPCION Y LA EDITORIAL DEL LIBRO EN BASE AL ISBN
    public Optional<BookInfo> getBookInfoByISBN(String isbn) {
        JSONObject volumeInfo = getVolumeInfo(isbn);
        if (volumeInfo != null) {
            String title = volumeInfo.optString("title", "Título no disponible");
            String publisher = volumeInfo.optString("publisher", "Editorial no disponible");
            String releaseDate = volumeInfo.optString("publishedDate", "Fecha no disponible");

            return Optional.of(new BookInfo(title, publisher, releaseDate));
        }
        return Optional.empty();
    }

    // METODO PARA OBTENER EL volumeInfo DESDE LA API, CONTIENE TODA LA INFORMACION DEL LIBRO
    private JSONObject getVolumeInfo(String isbn) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn + "&key=" + APIKEY;
            String response = restTemplate.getForObject(url, String.class);

            JSONObject json = new JSONObject(response);
            JSONArray items = json.optJSONArray("items");

            if (items != null && items.length() > 0) {
                return items.getJSONObject(0).optJSONObject("volumeInfo");
            }
        } catch (Exception e) {
            System.out.println("Error al obtener datos desde Google Books: " + e.getMessage());
        }
        return null;
    }

    // Clase auxiliar para guardar la info obtenida de la API
    public static class BookInfo {
        private final String title;
        private final String publisher;
        private final String date;

        public BookInfo(String title, String publisher, String date) {
            this.title = title;
            this.publisher = publisher;
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public String getPublisher() {
            return publisher;
        }

        public String getDate() {
            return date;
        }
    }


}
