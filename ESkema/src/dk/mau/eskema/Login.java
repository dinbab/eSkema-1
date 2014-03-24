package dk.mau.eskema;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dk.mau.tools.*; // Importing our tool package

/**
 * 
 * @author Frank Myhre
 * @author Dino Babic
 *
 */

public class Login extends Activity implements OnClickListener {

	private EditText user, pass;
	private Button submit;
	
	private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
	
	private static final String LOGIN_URL = "http://tenny.dk/new/eskema/login.php";
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		user = (EditText) findViewById(R.id.usernamefield);
		pass = (EditText) findViewById(R.id.pinfield);
		submit = (Button) findViewById(R.id.submit);
		
		submit.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.submit:
			new AttemptLogin().execute();
			break;
		default:
			break;
		}
		
	}
	
	class AttemptLogin extends AsyncTask<String, String, String>{

		boolean failure = false;
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pDialog = new ProgressDialog(Login.this);
			pDialog.setMax(R.string.login_dialog);
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
				
		@Override
		protected String doInBackground(String... arg0) {
			int success;
			String username = user.getText().toString();
			String password = pass.getText().toString();
			try{
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("password", password));
				
				Log.d("Request!", "Starting");
				
				JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);
				
				Log.d("Login attempt", json.toString());
				
				success = json.getInt(TAG_SUCCESS);
				if (success == 1){
					Log.d("Login Successful!", json.toString());
					
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Login.this);
					Editor edit = sp.edit();
					edit.putString("username", username);
					edit.commit();
					
					Intent i = new Intent(Login.this, SchemeList.class);
					finish();
					startActivity(i);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					return json.getString(TAG_MESSAGE);
				}else{
					Log.d("Login failed!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);
				}
			} catch(JSONException e){
				Log.e(getClass().getName(), "Exception: " + e.getMessage());
			}
			return null;
		}
		
		protected void onPostExecute(String file_url){
			pDialog.dismiss();
			if(file_url != null){
				Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
			}
		}
	}

}
