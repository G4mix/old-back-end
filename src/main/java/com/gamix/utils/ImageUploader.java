package com.gamix.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import io.github.cdimascio.dotenv.Dotenv;

public class ImageUploader {
    @Autowired
    private static Dotenv dotenv;

    public static final String UPLOAD_API_URL = "https://api.imgur.com/3/image";
    public static final int MAX_UPLOAD_ATTEMPTS = 3;

    public static String upload(File file) {
        HttpURLConnection conn = getHttpConnection(UPLOAD_API_URL);
        writeToConnection(conn, "image=" + toBase64(file));
        return getResponse(conn);
    }

    public static String createAlbum(List<String> imageIds, Integer userId) {
        String ALBUM_API_URL = "https://api.imgur.com/3/" + userId;
        HttpURLConnection conn = getHttpConnection(ALBUM_API_URL);
        String ids = "";
        for (String id : imageIds) {
            if (!ids.equals("")) {
                ids += ",";
            }
            ids += id;
        }
        writeToConnection(conn, "ids=" + ids);
        return getResponse(conn);
    }

    private static String toBase64(File file) {
        try {
            byte[] b = new byte[(int) file.length()];
            FileInputStream fs = new FileInputStream(file);
            fs.read(b);
            fs.close();
            return URLEncoder.encode(DatatypeConverter.printBase64Binary(b), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
    }

    private static HttpURLConnection getHttpConnection(String url) {
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Client-ID " + dotenv.get("IMGUR_CLIENT_ID"));
            conn.setReadTimeout(100000);
            conn.connect();
            return conn;
        } catch (UnknownHostException e) {
            throw new RuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.name());
        } catch (IOException e) {
            throw new RuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
    }

    private static void writeToConnection(HttpURLConnection conn, String message) {
        OutputStreamWriter writer;
        try {
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(message);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
    }

    private static String getResponse(HttpURLConnection conn) {
        StringBuilder str = new StringBuilder();
        BufferedReader reader;
        try {
            if (conn.getResponseCode() != HttpStatus.OK.value()) {
                throw new RuntimeException(conn.getResponseMessage());
            }
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
        if (str.toString().equals("")) {
            throw new RuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.name());
        }
        return str.toString();
    }
}
