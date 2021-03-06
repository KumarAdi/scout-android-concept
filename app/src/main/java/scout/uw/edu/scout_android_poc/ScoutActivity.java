package scout.uw.edu.scout_android_poc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.basecamp.turbolinks.TurbolinksAdapter;
import com.basecamp.turbolinks.TurbolinksSession;

import scout.uw.edu.scout_android_poc.utils.ErrorHandler;
import scout.uw.edu.scout_android_poc.utils.UserPreferences;


/**
 * A superclass for turbolinks activities containing common variables and implementing stub
 * methods for TurbolinksAdapter.
 */
public class ScoutActivity extends AppCompatActivity implements TurbolinksAdapter {

    private static final String LOG_TAG = ScoutActivity.class.getSimpleName();
    protected UserPreferences userPreferences;
    protected TurbolinksSession turbolinksSession;
    protected String location;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        userPreferences = new UserPreferences(this);

        Scout scout = Scout.getInstance();
        if(scout == null) {
            turbolinksSession = TurbolinksSession.getDefault(this);
        } else {
            turbolinksSession = TurbolinksSession.getNew(this);
        }

        turbolinksSession.setPullToRefreshEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
            //case R.id.action_refresh:
            //    refresh();
            //    return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scout, menu);
        return true;
    }

    @Override
    public void onPageFinished() {

    }

    @Override
    public void onReceivedError(int errorCode) {
        switch (errorCode){
            case 404:
                turbolinksSession.getWebView().loadUrl("about:blank");
                ErrorHandler.show404(this);
                return;
        }
        ErrorHandler.showUncaught(this);
    }

    @Override
    public void pageInvalidated() {

    }

    @Override
    public void requestFailedWithStatusCode(int statusCode) {
        switch (statusCode){
            case 404:
                ErrorHandler.show404(this);
                return;
        }
        ErrorHandler.showUncaught(this);
    }

    @Override
    public void visitCompleted() {

    }

    @Override
    public void visitProposedToLocationWithAction(String location, String action) {

    }

    protected void refresh(){
        turbolinksSession.getWebView().loadUrl(location);
    }
}
