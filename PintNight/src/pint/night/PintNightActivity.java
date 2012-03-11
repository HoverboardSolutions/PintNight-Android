/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pint.night;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import org.jsoup.nodes.Document;

import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.view.View;
import android.webkit.WebView;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.*;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.*;

/**
 * This class provides a basic demonstration of how to write an Android
 * activity. Inside of its window, it places a single view: an EditText that
 * displays and edits some internal text.
 */
public class PintNightActivity extends Activity implements OnTabChangeListener{
    WebView upcoming_browser;
    WebView calendar_browser;
    WebView collection_browser;
    ListView calendar_list;
   protected InitTask initTask;
    
    

    
    protected List<PintNightEvent> pintNightEvents = new ArrayList<PintNightEvent>(10);
    protected ArrayList<String> beerList=new ArrayList<String>();	
    protected String[] beerListArray;
    public PintNightActivity() {
    }

    /** Called with the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate our UI from its XML layout description.
        setContentView(R.layout.main);
        
        AdView adView = (AdView)this.findViewById(R.id.adView);
        adView.loadAd(new AdRequest());	
        
       
        Resources res = getResources();
        
        TabHost tabs=(TabHost)findViewById(R.id.tabhost);
        tabs.setup();
        
        TabHost.TabSpec spec=tabs.newTabSpec("tag1");
        spec.setContent(R.id.upcoming);
        spec.setIndicator("",res.getDrawable(R.drawable.ic_tab_upcoming));
        tabs.addTab(spec);
        
        spec=tabs.newTabSpec("tag2");
        spec.setContent(R.id.ListView01);
        spec.setIndicator("", res.getDrawable(R.drawable.ic_tab_calendar));
        tabs.addTab(spec);
        
        spec=tabs.newTabSpec("collection");
        spec.setContent(R.id.collection);
        spec.setIndicator("",res.getDrawable(R.drawable.ic_tab_collection));
        tabs.addTab(spec);
        
        tabs.setOnTabChangedListener(this);
                       
        
        upcoming_browser=(WebView)findViewById(R.id.upcoming);
        upcoming_browser.loadUrl("http://schmidtcds.com/pintnight/v2/upcoming2.php");
        collection_browser=(WebView)findViewById(R.id.collection);
        collection_browser.loadUrl("http://ad.leadboltads.net/show_app_wall?section_id=504415991");
        
        calendar_list = (ListView)findViewById(R.id.ListView01);
        
        initTask = new InitTask();
        initTask.execute(this);
       	 
        
        
        /*try{
        	parseCalender();
        }catch (IOException a){
        	testAlert(a.toString());
        }*/
       
        
        


        
    }
    
    @Override
    public void onTabChanged(String tabId) {
    	if(tabId.equals("collection")){
            showInDevelopmentAlert();
            } else if(tabId.equals("tag2")){
            	while(initTask.getStatus() == AsyncTask.Status.RUNNING){
            		//Wait until init task has completed
            	}
    			calendar_list.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, beerListArray)); 
    			calendar_list.setOnItemClickListener(new OnItemClickListener() {
    	            @Override
    	            public void onItemClick(AdapterView<?> parent, View view, int position,
    	                    long id) {
    	                
    	                String item = pintNightEvents.get(position).getMoreInfo();
    	                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item));
    	                startActivity(browserIntent);
    	               
    	                
    	            }
    	        });
            }
    	
    }
    
    
    public void showInDevelopmentAlert() {
    	AlertDialog alertDialog = new AlertDialog.Builder(this).create(); 
    	alertDialog.setMessage("The Collections feature is currently in development.  If you would like to support this and other features, please check out our sponsors");
    	
    	alertDialog.setTitle("Feature in Development");
    	alertDialog.setButton("Cheers!", new DialogInterface.OnClickListener() {  
    	      public void onClick(DialogInterface dialog, int which) {  
    	        return;  
    	    } });
    	alertDialog.show();
    	
    }
    
    
    public void testAlert(String message) {
    	AlertDialog alertDialog = new AlertDialog.Builder(this).create(); 
    	alertDialog.setMessage(message);
    	
    	alertDialog.setTitle("Feature in Development");
    	alertDialog.setButton("Cheers!", new DialogInterface.OnClickListener() {  
    	      public void onClick(DialogInterface dialog, int which) {  
    	        return;  
    	    } });
    	alertDialog.show();
    	
    }
    
    public void parseCalender() throws IOException {
    	
    	try {
    		Document calender = Jsoup.connect("http://www.pazzospizzapub.com/PintNight.html").get();
    		Elements tables = calender.getElementsByTag("table");
    		Elements events = tables.get(2).getElementsByTag("tr");
    		
    		beerList.ensureCapacity(events.size());
    		
    		PintNightEvent event = new PintNightEvent();
    		for(int k = 0; k < events.size(); k++){
    			event = new PintNightEvent(events.get(k));
    			pintNightEvents.add(event);
    			beerList.add(event.toString());
    		}	
    		beerListArray = new String[beerList.size()];
    		beerList.toArray(beerListArray);
    		
    	} catch (IOException a) { 	
    		testAlert(a.toString());
    	} catch (IndexOutOfBoundsException a) {
    		testAlert(a.toString());
		} catch (NullPointerException a) {
    		testAlert(a.toString());
		}	
    	
    }
    protected class InitTask extends AsyncTask<Context, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(Context... params) {
			 try{
		        	parseCalender();
		        	return true;
		        }catch (IOException a){
		        	testAlert(a.toString());
		        	return false;
		        }
		}
		
		@Override
		protected void onPostExecute( Boolean result )  {
		      super.onPostExecute(result);
		}
    	
    }
    
}

    /**
     * Called when the activity is about to start interacting with the user.
     */
  //  @Override
   // protected void onResume() {
    //    super.onResume();
   // }

  

