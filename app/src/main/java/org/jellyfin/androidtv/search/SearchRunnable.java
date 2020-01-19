package org.jellyfin.androidtv.search;

import android.content.Context;

import org.jellyfin.androidtv.R;
import org.jellyfin.androidtv.itemhandling.ItemRowAdapter;
import org.jellyfin.androidtv.presentation.CardPresenter;
import org.jellyfin.apiclient.interaction.EmptyResponse;
import org.jellyfin.apiclient.model.search.SearchQuery;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;

import java.util.ArrayList;

/**
 * Created by Eric on 4/8/2015.
 */
public class SearchRunnable implements Runnable {
    private String searchString;
    private Context context;
    private ArrayObjectAdapter mRowsAdapter;
    private boolean musicOnly;
    private int searchesReceived;
    private ArrayList<ItemRowAdapter> searchItemRows;

    public void setQueryString(String value) {
        searchString = value;
    }

    public SearchRunnable(Context context, ArrayObjectAdapter adapter, boolean musicOnly) {
        this.context = context;
        this.mRowsAdapter = adapter;
        this.musicOnly = musicOnly;
        this.searchItemRows = new ArrayList<>();
    }

    @Override
    public void run() {
        mRowsAdapter.clear();
        searchItemRows.clear();
        searchesReceived = 0;

        if (!musicOnly) {
            //Get search results by type
            SearchQuery movies = getSearchQuery(new String[] {"Movie", "BoxSet"});
            ItemRowAdapter movieAdapter = new ItemRowAdapter(movies, new CardPresenter(), mRowsAdapter);
            ListRow movieRow = new ListRow(new HeaderItem(context.getString(R.string.lbl_movies)), movieAdapter);
            movieAdapter.setRow(movieRow);
            RetrieveSearchResult(movieAdapter);

            SearchQuery tvSeries = getSearchQuery(new String[] {"Series"});
            ItemRowAdapter tvSeriesAdapter = new ItemRowAdapter(tvSeries, new CardPresenter(), mRowsAdapter);
            ListRow tvSeriesRow = new ListRow(new HeaderItem(context.getString(R.string.lbl_series)), tvSeriesAdapter);
            tvSeriesAdapter.setRow(tvSeriesRow);
            RetrieveSearchResult(tvSeriesAdapter);

            SearchQuery tv = getSearchQuery(new String[] {"Episode"});
            ItemRowAdapter tvAdapter = new ItemRowAdapter(tv, new CardPresenter(), mRowsAdapter);
            ListRow tvRow = new ListRow(new HeaderItem(context.getString(R.string.lbl_episodes)), tvAdapter);
            tvAdapter.setRow(tvRow);
            RetrieveSearchResult(tvAdapter);

            SearchQuery people = getSearchQuery(new String[] {"Person","People"});
            ItemRowAdapter peopleAdapter = new ItemRowAdapter(people, new CardPresenter(), mRowsAdapter);
            ListRow peopleRow = new ListRow(new HeaderItem(context.getString(R.string.lbl_people)), peopleAdapter);
            peopleAdapter.setRow(peopleRow);
            RetrieveSearchResult(peopleAdapter);

            SearchQuery videos = getSearchQuery(new String[] {"Video"});
            ItemRowAdapter videoAdapter = new ItemRowAdapter(videos, new CardPresenter(), mRowsAdapter);
            ListRow videoRow = new ListRow(new HeaderItem(context.getString(R.string.lbl_videos)), videoAdapter);
            videoAdapter.setRow(videoRow);
            RetrieveSearchResult(videoAdapter);

            SearchQuery recordings = getSearchQuery(new String[] {"Recording"});
            ItemRowAdapter recordingAdapter = new ItemRowAdapter(recordings, new CardPresenter(), mRowsAdapter);
            ListRow recordingRow = new ListRow(new HeaderItem(context.getString(R.string.lbl_recordings)), recordingAdapter);
            recordingAdapter.setRow(recordingRow);
            RetrieveSearchResult(recordingAdapter);

            SearchQuery programs = getSearchQuery(new String[] {"Program"});
            ItemRowAdapter programAdapter = new ItemRowAdapter(programs, new CardPresenter(), mRowsAdapter);
            ListRow programRow = new ListRow(new HeaderItem(context.getString(R.string.lbl_programs)), programAdapter);
            programAdapter.setRow(programRow);
            RetrieveSearchResult(programAdapter);
        }

        SearchQuery artists = getSearchQuery(new String[] {"MusicArtist"});
        ItemRowAdapter artistAdapter = new ItemRowAdapter(artists, new CardPresenter(), mRowsAdapter);
        ListRow artistRow = new ListRow(new HeaderItem(context.getString(R.string.lbl_artists)), artistAdapter);
        artistAdapter.setRow(artistRow);
        RetrieveSearchResult(artistAdapter);

        SearchQuery albums = getSearchQuery(new String[] {"MusicAlbum"});
        ItemRowAdapter albumAdapter = new ItemRowAdapter(albums, new CardPresenter(), mRowsAdapter);
        ListRow albumRow = new ListRow(new HeaderItem(context.getString(R.string.lbl_albums)), albumAdapter);
        albumAdapter.setRow(albumRow);
        RetrieveSearchResult(albumAdapter);

        SearchQuery songs = getSearchQuery(new String[] {"Audio"});
        ItemRowAdapter songAdapter = new ItemRowAdapter(songs, new CardPresenter(), mRowsAdapter);
        ListRow songRow = new ListRow(new HeaderItem(context.getString(R.string.lbl_songs)), songAdapter);
        songAdapter.setRow(songRow);
        RetrieveSearchResult(songAdapter);

    }

    private void RetrieveSearchResult(ItemRowAdapter itemRow) {
        searchItemRows.add(itemRow);
        itemRow.setRetrieveFinishedListener(new EmptyResponse() {
            @Override
            public void onResponse() {
                searchesReceived++;
                if (searchesReceived == searchItemRows.size()){
                    for (ItemRowAdapter itemRowAdapter : searchItemRows) {
                        itemRowAdapter.AddToParentIfResultsReceived();
                    }
                }
            }
        });
        itemRow.Retrieve();
    }

    private SearchQuery getSearchQuery(String[] itemTypes) {
        SearchQuery query = new SearchQuery();
        query.setLimit(50);
        query.setSearchTerm(searchString);
        query.setIncludeItemTypes(itemTypes);

        return query;

    }
}

