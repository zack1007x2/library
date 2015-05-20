package com.framework.net;

import java.util.Locale;
        import android.content.Context;
        import android.content.SharedPreferences;
        import android.text.TextUtils;
        import android.util.Log;

        import org.apache.http.client.CookieStore;
        import org.apache.http.cookie.Cookie;

        import java.io.ByteArrayInputStream;
        import java.io.ByteArrayOutputStream;
        import java.io.ObjectInputStream;
        import java.io.ObjectOutputStream;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;
        import java.util.concurrent.ConcurrentHashMap;
/**
 * 底层内部接口类。用于解决Seesion和Cookie的问题
 * @author lee
 */
public class PersistentCookieStore implements CookieStore {
    private static final String LOG_TAG = "PersistentCookieStore";
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private static final String COOKIE_NAME_STORE = "names";
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    private boolean omitNonPersistentCookies = false;

    private final ConcurrentHashMap<String, Cookie> cookies;
    private final SharedPreferences cookiePrefs;
    private static PersistentCookieStore cookie;
    public static PersistentCookieStore getInstance(Context context){
    	if(cookie == null){
    		cookie = new PersistentCookieStore(context);
    	}
    	return cookie;
    }
    private PersistentCookieStore(Context context) {
    	System.out.println("context="+context);
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        cookies = new ConcurrentHashMap<String, Cookie>();
        String storedCookieNames = cookiePrefs.getString(COOKIE_NAME_STORE, null);
        if (storedCookieNames != null) {
            String[] cookieNames = TextUtils.split(storedCookieNames, ",");
            for (String name : cookieNames) {
                String encodedCookie = cookiePrefs.getString(COOKIE_NAME_PREFIX + name, null);
                if (encodedCookie != null) {
                    Cookie decodedCookie = decodeCookie(encodedCookie);
                    if (decodedCookie != null) {
                        cookies.put(name, decodedCookie);
                    }
                }
            }
            clearExpired(new Date());
        }
    }

    @Override
    public void addCookie(Cookie cookie) {
        if (omitNonPersistentCookies && !cookie.isPersistent())
            return;
        String name = cookie.getName() + cookie.getDomain();
        if (!cookie.isExpired(new Date())) {
            cookies.put(name, cookie);
        } else {
            cookies.remove(name);
        }

        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.putString(COOKIE_NAME_STORE, TextUtils.join(",", cookies.keySet()));
        prefsWriter.putString(COOKIE_NAME_PREFIX + name, encodeCookie(new SerializableCookie(cookie)));
        prefsWriter.commit();
    }

    @Override
    public void clear() {
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        for (String name : cookies.keySet()) {
            prefsWriter.remove(COOKIE_NAME_PREFIX + name);
        }
        prefsWriter.remove(COOKIE_NAME_STORE);
        prefsWriter.commit();

        cookies.clear();
    }

    @Override
    public boolean clearExpired(Date date) {
        boolean clearedAny = false;
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();

        for (ConcurrentHashMap.Entry<String, Cookie> entry : cookies.entrySet()) {
            String name = entry.getKey();
            Cookie cookie = entry.getValue();
            if (cookie.isExpired(date)) {

                cookies.remove(name);

                prefsWriter.remove(COOKIE_NAME_PREFIX + name);

                clearedAny = true;
            }
        }

        if (clearedAny) {
            prefsWriter.putString(COOKIE_NAME_STORE, TextUtils.join(",", cookies.keySet()));
        }
        prefsWriter.commit();

        return clearedAny;
    }

    @Override
    public List<Cookie> getCookies() {
        return new ArrayList<Cookie>(cookies.values());
    }

    public void setOmitNonPersistentCookies(boolean omitNonPersistentCookies) {
        this.omitNonPersistentCookies = omitNonPersistentCookies;
    }

    public void deleteCookie(Cookie cookie) {
        String name = cookie.getName();
        cookies.remove(name);
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.remove(COOKIE_NAME_PREFIX + name);
        prefsWriter.commit();
    }

    protected String encodeCookie(SerializableCookie cookie) {
        if (cookie == null)
            return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (Exception e) {
            return null;
        }

        return byteArrayToHexString(os.toByteArray());
    }

    protected Cookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableCookie) objectInputStream.readObject()).getCookie();
        } catch (Exception exception) {
            Log.d(LOG_TAG, "decodeCookie failed", exception);
        }

        return cookie;
    }

    protected String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    protected byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
