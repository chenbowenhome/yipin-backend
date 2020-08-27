package utils;

import enums.ResultEnum;
import exception.BasicException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class OkHttpUtil {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded");
    public static final String HEADER = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11";

    private static ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();

    private static class InstanceHolder {
        private static final OkHttpClient INSTANCE = new OkHttpClient().newBuilder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                        log.info("get url={},cookie={}", httpUrl, list);
                        cookieStore.put(httpUrl.host(), list);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                        List<Cookie> cookies = cookieStore.get(httpUrl.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();
    }

    public static OkHttpClient getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * 清楚本次session与cookie
     */
    public static void clearSession() {
        cookieStore.clear();
    }

    /**
     * Get 请求，
     *
     * @param url
     * @return
     */
    public static String doGet(String url) {
        OkHttpClient client = getInstance();
        Request request = new Request.Builder()
                .addHeader("User-Agent", HEADER)
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * post json请求
     *
     * @param url
     * @param json json 字符串
     * @return
     */
    public static String doPostWithJson(String url, String json) {
        OkHttpClient client = getInstance();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", HEADER)
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            return null;
        }

    }

    public static String doPostWithForm(String url, Object form) {
        OkHttpClient client = getInstance();
        FormBody formBody = getFormBody(form);

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            return null;
        }

    }

    private static <T> FormBody getFormBody(T form) {
        FormBody.Builder builder = new FormBody.Builder();
        Class<?> clazz = form.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                String name = field.getName();
                Object val = field.get(form);
                field.setAccessible(false);
                if (name != null && val != null) {
                    builder.add(name, Objects.toString(val));
                }
            }
            return builder.build();
        } catch (IllegalAccessException e) {
            throw new  BasicException(ResultEnum.REMOTE_ERROR);
        }
    }

    private static <T> String getFormUrlParam(T form) {
        StringBuilder urlParam = new StringBuilder();

        Class<?> clazz = form.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            String tem = "";
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                String name = field.getName();
                Object val = field.get(form);
                field.setAccessible(false);
                if (name != null) {
                    tem = name + "=" + Objects.toString(val, "");
                    if (i != fields.length - 1) {
                        tem += "&";
                    }
                    urlParam.append(tem);
                }
            }
            return urlParam.toString();
        } catch (IllegalAccessException e) {
            throw new BasicException(ResultEnum.REMOTE_ERROR);
        }
    }
}
