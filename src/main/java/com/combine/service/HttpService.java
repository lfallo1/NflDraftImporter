package com.combine.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * http / document service
 * @author lfallon
 *
 */
public class HttpService {

	private static final int URL_REQUEST_ATTEMPTS = 5;
	private static final int SECOND = 1000;

	/**
	 * get document from URL
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public Document getDocumentFromUrl(String url) throws IOException {
		int attempt = 0;
		while (attempt < URL_REQUEST_ATTEMPTS) {
			try {
				Document doc = Jsoup.connect(url).timeout(15*SECOND).get();

				// in this step, uncomment html (this logic could change, but
				// currently some tables / data are commented out on the website
				// and unrecognized by document parser)
				String formatted = doc.toString().replaceAll("<!-- ", "");
				formatted = formatted.toString().replaceAll(" -->", "");
				return Jsoup.parse(formatted);
			} catch (IOException e) {
				attempt++;
				System.out.println("retrying " + url);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					throw new IOException("Unable to fetch " + url);
				}
			}
		}
		throw new IOException("Unable to fetch " + url);
	}
}
