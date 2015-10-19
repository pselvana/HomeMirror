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
    public interface NewsListener {
        void onNewNews(String headline);
    }

    private static int numUpdates = 0;
    private static RSSFeed feed;
    private static int maxStories = 1; // Sensible default
    private static int refreshInMin = 30;
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
                String url = "http://rss.cnn.com/rss/cnn_world.rss";
                //String url = "http://feeds.bbci.co.uk/news/world/rss.xml?edition=us";

                // Pick up latest news every 30 minutes only
                if (numUpdates % refreshInMin == 1)
                {
                    try {
                        feed = rssReader.load(url);
                        maxStories = Math.min(feed.getItems().size(), refreshInMin);

                        return feed.getItems().get(numUpdates%maxStories).getTitle();
                    } catch (RSSReaderException e) {
                        Log.e("NewsModule", "Error parsing RSS");
                        return null;
                    }
                }
                else
                {
                    // Show cached news and change it every minute
                    return feed.getItems().get(numUpdates%maxStories).getTitle();
                }

            }
        }.execute();
    }
}
