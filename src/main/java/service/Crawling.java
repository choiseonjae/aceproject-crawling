package service;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Crawling {

	/**
	 * get 요청
	 * 
	 * @param url
	 * @return
	 */
	public static Document get(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			throw new RuntimeException();
		}
		return doc;
	}

	/**
	 * post 요청
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static Document post(String url, Map<String, String> params) {
		Connection conn = Jsoup.connect(url);
		params.forEach((key, value) -> conn.data(key, value));
		Document doc = null;
		try {
			doc = conn.post();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return doc;
	}

}
