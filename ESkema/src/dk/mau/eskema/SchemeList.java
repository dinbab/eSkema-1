package dk.mau.eskema;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 
 * @author Frank Myhre
 * @author Dino Babic
 *
 */

public class SchemeList extends Activity implements TextWatcher, OnTouchListener, OnItemClickListener{
	


	public class SchemeElement{
		String headline;
		String description;
	}
	
	SchemeListAdapter schemeListAdapter;
	
	EditText et;
	Drawable clear;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schemelist);

		schemeListAdapter = new SchemeListAdapter();
		
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(schemeListAdapter);
		
		listView.setOnItemClickListener(this);
		
		et = (EditText) findViewById(R.id.searchfield);
		et.addTextChangedListener(this);
		
		String value = "Søg";
		
		clear = getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel);
		clear.setBounds(0, 0, clear.getIntrinsicWidth(), clear.getIntrinsicHeight());
		et.setCompoundDrawables(null, null, value.equals("") ? null : clear, null);
		et.setOnTouchListener(this);
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
		schemeListAdapter.getFilter().filter(s);
		et.setCompoundDrawables(null, null, et.getText().toString().equals("") ? null : clear, null);		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(et.getCompoundDrawables()[2] == null){
			return false;
		}
		if(event.getAction() != MotionEvent.ACTION_UP){
			return false;
		}
		if(event.getX() > et.getWidth() - et.getPaddingRight() - clear.getIntrinsicWidth()){
			et.setText("");
			et.setCompoundDrawables(null, null, null, null);
		}
		return false;
	}

	public class SchemeListAdapter extends BaseAdapter implements Filterable{

		List<SchemeElement> schemeElementList = getDataForListView();
		
		@Override
		public int getCount() {
			return schemeElementList.size();
		}

		@Override
		public Object getItem(int item) {
			return schemeElementList.get(item);
		}

		@Override
		public long getItemId(int itemid) {
			return itemid;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(convertView==null){
				LayoutInflater inflater = (LayoutInflater) SchemeList.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_element, parent,false);
			}
			
			TextView headline = (TextView) convertView.findViewById(R.id.listelement_headline);
			TextView description = (TextView) convertView.findViewById(R.id.listelement_description);
			
			SchemeElement element = schemeElementList.get(position);
			
			headline.setText(element.headline);
			description.setText(element.description);
			
			return convertView;
		}
		
		public SchemeElement getSchemeElement(int position){
			return schemeElementList.get(position);
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter(){

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				protected void publishResults(CharSequence constraint,
						FilterResults results) {
					// TODO Auto-generated method stub
					
				}
				
			};
			return filter;
		}

	}
	
	public List<SchemeElement> getDataForListView(){
		List<SchemeElement> schemeElementsList = new ArrayList<SchemeElement>();
		
		SchemeElement element = new SchemeElement();
		
		element.headline = "Early Warning Score";
		element.description = "Diagnosticering";

		schemeElementsList.add(element);
		
		element.headline = "Abstinens Score";
		element.description = "Efter alkoholafhængighed";

		schemeElementsList.add(element);
		
		element.headline = "Insulin Indgift";
		element.description = "Daglig indtagen af insulin";

		schemeElementsList.add(element);
		
		return schemeElementsList;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		SchemeElement element = schemeListAdapter.getSchemeElement(arg2);
		Toast.makeText(SchemeList.this, element.headline, Toast.LENGTH_LONG).show();
		CheckBox box = (CheckBox) arg1.findViewById(R.id.checkBox);
		
	}
	
}
