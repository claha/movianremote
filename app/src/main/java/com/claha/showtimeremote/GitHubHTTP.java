package com.claha.showtimeremote;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class GitHubHTTP {

    private static final String URL_GITHUB = "https://github.com/claha/showtimeremote";
    private OnCommitsCountedListener onCommitsCountedListener;
    private OnReleasesCountedListener onReleasesCountedListener;

    public void run() {
        new Download().execute();
    }

    public void setOnCommitsCountedListener(OnCommitsCountedListener onCommitsCountedListener) {
        this.onCommitsCountedListener = onCommitsCountedListener;
    }

    public void setOnReleasesCountedListener(OnReleasesCountedListener onReleasesCountedListener) {
        this.onReleasesCountedListener = onReleasesCountedListener;
    }

    public interface OnCommitsCountedListener {
        public void onCounted(int count);
    }

    public interface OnReleasesCountedListener {
        public void onCounted(int count);
    }

    private class Download extends AsyncTask {

        private List<String> html;

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                URL url = new URL(URL_GITHUB);
                URLConnection connection = url.openConnection();
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                html = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    html.add(line);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            boolean foundCommits = false;
            boolean foundReleases = false;
            int commits = -1;
            int releases = -1;
            int i = 0;
            while (!(foundCommits && foundReleases)) {
                if (!foundCommits) {
                    if (html.get(i).contains("/commits") && html.get(i).contains("data-pjax")) {
                        commits = Integer.parseInt(html.get(i + 3).replace(" ", ""));
                        foundCommits = true;
                    }
                } else {
                    if (html.get(i).contains("/releases")) {
                        releases = Integer.parseInt(html.get(i + 3).replace(" ", ""));
                        foundReleases = true;
                    }
                }
                i++;
            }
            onCommitsCountedListener.onCounted(commits);
            onReleasesCountedListener.onCounted(releases);
        }
    }
}
