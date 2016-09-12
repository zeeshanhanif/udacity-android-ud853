package com.example.android.sunshine;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.android.sunshine.data.WeatherContract.WeatherEntry;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int DETAIL_LOADER = 0;
    private static final String TAG = "DetailsFragment";
    private static final String FORCAST_SHARE_HASHTAG = "#SunshineApp";
    private ShareActionProvider mShareActionProvider;
    private String mForecast;

    private static final String[] FORECAST_COLUMNS = {
                            WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
                            WeatherEntry.COLUMN_DATE,
                            WeatherEntry.COLUMN_SHORT_DESC,
                            WeatherEntry.COLUMN_MAX_TEMP,
                            WeatherEntry.COLUMN_MIN_TEMP,
                    };

    private static final int COL_WEATHER_ID = 0;
            private static final int COL_WEATHER_DATE = 1;
            private static final int COL_WEATHER_DESC = 2;
            private static final int COL_WEATHER_MAX_TEMP = 3;
            private static final int COL_WEATHER_MIN_TEMP = 4;

    public DetailsFragment() {
        setHasOptionsMenu(true);
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_fragment_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if(mForecast != null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
        else {
            Log.d(TAG,"Share Action Provider is null?");
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        //shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast+" "+FORCAST_SHARE_HASHTAG );
        return shareIntent;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Intent intent = getActivity().getIntent();
        if(intent == null){
            return null;
        }
        return new CursorLoader(getActivity(), intent.getData(),FORECAST_COLUMNS,null,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(!data.moveToFirst()){
            return;
        }

        TextView detailText = (TextView) getView().findViewById(R.id.detail_text);
        mForecast = convertCursorRowToUXFormat(data);
        detailText.setText(mForecast);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(getActivity());
        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
        return highLowStr;
    }

    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor) {

        String highAndLow = formatHighLows(
                cursor.getDouble(MainFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(MainFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(MainFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(MainFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }
}
