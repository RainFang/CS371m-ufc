package yftvn.ufc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ComparisonProfileActivity extends Activity {
	
	// A tag for the cat log
	private static final String TAG = "UFC Fighter App";

	/**
	 * TextView and ImageView fields.
	 */
	private static TextView mNameTextView1, mNameTextView2;
	private static TextView mWinsTextView1, mWinsTextView2;
	private static TextView mWKOTextView1, mWKOTextView2;
	private static TextView mWSubTextView1, mWSubTextView2;
	private static TextView mWDTextView1, mWDTextView2;
	private static TextView mLossesTextView1, mLossesTextView2;
	private static TextView mTitlesTextView1, mTitlesTextView2;
	private static ImageView mImgView1, mImgView2;
	private static ImageLoader mImgLoader1, mImgLoader2;

	/**
	 * The URL Format to get the photo of the fighter based on his espnId. The
	 * format request 3 integers: espnId, width, height.
	 */
	private static final String PHOTO_URL_FORMAT = "http://a.espncdn.com/combiner/i?img=/i/headshots/mma/players/full/%d.png&w=%d&h=%d";
	private static final int PHOTO_DEFAULT_WIDTH = 250;
	private static final int PHOTO_DEFAULT_HEIGHT = 181;
	
	/**
	 * The ESPN ID for the fighter this profile will display.
	 * This value will also be sent to comparison search so the comparison search
	 * can display a mini profile for the same fighter.
	 */
	private int espnId1, espnId2;

	@Override
	/**
	 * Activities run when viewing a profile page.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set Content View.
		setContentView(R.layout.comparison_profile);
		// Associate TextFields and display info.
		initFighterViewInfo();
		// Get Fighter ESPN Id.
		Bundle bundle = getIntent().getExtras();
		
		Log.d(TAG, "We have entered ComparisonProfileActivity");
		Log.d(TAG, "espn ID 1: "+bundle.getInt("espnId1"));
		Log.d(TAG, "espn ID 2: "+bundle.getInt("espnId2"));
		
		espnId1 = bundle.getInt("espnId1");
		espnId2 = bundle.getInt("espnId2");
		
		// Config ImageLoader.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).build();
		
		if (espnId1 > 0) {
			mImgLoader1 = ImageLoader.getInstance();
			mImgLoader1.init(config);
			// Display fighter profile.
			displayFighterProfile1(espnId1);
		}
		
		if (espnId2 > 0) {
			mImgLoader2 = ImageLoader.getInstance();
			mImgLoader2.init(config);
			// Display fighter profile.
			displayFighterProfile2(espnId2);
		}
	}

	/**
	 * Associate all the TextView fields with an object.
	 */
	private void initFighterViewInfo() {
		mNameTextView = (TextView) findViewById(R.id.name);
		mWinsTextView = (TextView) findViewById(R.id.wins);
		mWKOTextView = (TextView) findViewById(R.id.wko);
		mWSubTextView = (TextView) findViewById(R.id.wsub);
		mWDTextView = (TextView) findViewById(R.id.wd);
		mLossesTextView = (TextView) findViewById(R.id.losses);
		mTitlesTextView = (TextView) findViewById(R.id.titles);
		mImgView1 = (ImageView) findViewById(R.id.fighterPic1);
		mImgView2 = (ImageView) findViewById(R.id.fighterPic2);
	}

	@Override 
	public boolean onCreateOptionsMenu(Menu menu) 
	{ 
		super.onCreateOptionsMenu(menu); 

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.fighter_profile, menu);
		return true;

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		super.onOptionsItemSelected(item);
		
		switch (item.getItemId()) 
		{
			case R.id.comparison_search:
				Intent intent = new Intent(ComparisonProfileActivity.this,
						ComparisonSearchActivity.class);
				intent.putExtra("espnId", espnId1);
				startActivity(intent);
				return true;
		}
		return false;
	}

	/**
	 * Display a Fighter Profile page given his espnId.
	 * 
	 * @param espnId
	 */
	private void displayFighterProfile1(int espnId) {
		String imageUri = getPhotoURL(espnId);
		Fighter profile = FighterData.getFighter(espnId);
		Record rec = profile.getRecord();
		mImgLoader1.displayImage(imageUri, mImgView1);
		mNameTextView.setText(profile.getFullName());
		mWinsTextView.setText(String.valueOf(rec.getWins()));
		mWKOTextView.setText(String.valueOf(rec.getKnockout()));
		mWSubTextView.setText(String.valueOf(rec.getSubmission()));
		mWDTextView.setText(String.valueOf(rec.getDecisionWins()));
		mLossesTextView.setText(String.valueOf(rec.getLosses()));
		mTitlesTextView.setText(profile.getTitles());
	}
	
	/**
	 * Display a Fighter Profile page given his espnId.
	 * Yes, I know this code is essentially duplicated. We are running out of time,
	 * and Yu-Chieh Fang was sick for a week. We'll make the code a lot cleaner, 
	 * and less "duplicative" in the next release. We're very sorry!
	 * 
	 * @param espnId
	 */
	private void displayFighterProfile2(int espnId) {
		String imageUri = getPhotoURL(espnId);
		Fighter profile = FighterData.getFighter(espnId);
		Record rec = profile.getRecord();
		mImgLoader2.displayImage(imageUri, mImgView2);
		mNameTextView.setText(profile.getFullName());
		mWinsTextView.setText(String.valueOf(rec.getWins()));
		mWKOTextView.setText(String.valueOf(rec.getKnockout()));
		mWSubTextView.setText(String.valueOf(rec.getSubmission()));
		mWDTextView.setText(String.valueOf(rec.getDecisionWins()));
		mLossesTextView.setText(String.valueOf(rec.getLosses()));
		mTitlesTextView.setText(profile.getTitles());
	}

	/**
	 * @param espnId
	 * @return String of the correct Photo URL to be displayed.
	 */
	private static String getPhotoURL(int espnId) {
		return String.format(PHOTO_URL_FORMAT, espnId, PHOTO_DEFAULT_WIDTH,
				PHOTO_DEFAULT_HEIGHT);
	}

}
