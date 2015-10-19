package com.morristaedt.mirror.modules;

import android.os.AsyncTask;
import android.util.Log;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSReader;
import org.mcsoxford.rss.RSSReaderException;

/**
 * Created by alex on 21/09/15.
 */
public class NewsModule {
    private static int numUpdates = 0;
    public interface NewsListener {
        void onNewNews(String headline);
    }

    public static RSSFeed feed;
    public static void getNewsHeadline(final NewsListener newsListener) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                newsListener.onNewNews(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                numUpdates++;
                RSSReader rssReader = new RSSReader();
                String url = "http://feeds.bbci.co.uk/news/world/rss.xml?edition=us";

                // Pick up latest news every 10 minutes only
                if (numUpdates % 10 == 1)
                {
                    try {
                        feed = rssReader.load(url);

                        return feed.getItems().get(numUpdates%30).getTitle();
                    } catch (RSSReaderException e) {
                        Log.e("NewsModule", "Error parsing RSS");
                        return null;
                    }
                }
                else
                {
                    // Show cached news and change it every minute
                    return feed.getItems().get(numUpdates%30).getTitle();
                }

            }
        }.execute();
    }
}
