package hyweb.phone.gip;


import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

/**
 * This demonstrates how you can implement switching between the tabs of a
 * TabHost through fragments.  It uses a trick (see the code below) to allow
 * the tabs to switch between fragments instead of simple views.
 */
public class MainTabActivity extends FragmentActivity {
    TabHost mTabHost;
    TabManager mTabManager;

    private static Element root;
    private GetXmlRoot getRoot;
    public static int pageNum;
    static int position;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position=-1;
        setContentView(R.layout.main);
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
                        
        getRoot = GetXmlRoot.getInstance();
        root = getRoot.getRoot(getApplicationContext());
        
        

        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);
        
        
        NodeList TabbarNode = root.getElementsByTagName(ConstantClass.XML_TAG_TABBAR);
        for(int i=0;i<TabbarNode.getLength();i++){
            NodeList ItemNode =root.getElementsByTagName(ConstantClass.XML_TAG_ITEM);
            for(int j=0;j<ItemNode.getLength();j++){
                Element tabbarElement = (Element)ItemNode.item(j);
//                addNewTab(this,MainTabContent.class,
//                        tabbarElement.getAttribute("text"),
//                        tabbarElement.getAttribute("targetId"),
//                        stringToRes(tabbarElement.getAttribute("icon")));
                int img=stringToRes(tabbarElement.getAttribute("icon"));
                Bundle bundle = new Bundle(); 
                bundle.putString("id", tabbarElement.getAttribute("targetId")); 
                View tab = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
                Integer iconId=img;
                ImageView image = (ImageView) tab.findViewById(R.id.icon);
                TextView text = (TextView) tab.findViewById(R.id.text);
                if(iconId != null){
                    image.setImageResource(iconId);
                }
                text.setText(tabbarElement.getAttribute("text"));


                
                mTabManager.addTab(mTabHost.newTabSpec(tabbarElement.getAttribute("targetId"))
                		//.setIndicator(tabbarElement.getAttribute("text"),resize(getResources().getDrawable(img))),
                		.setIndicator(tab),
                		MainTabContent.class, bundle);
            }
        }
        
        

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
        
        // Get scream width   
         DisplayMetrics dm = new DisplayMetrics();   
         getWindowManager().getDefaultDisplay().getMetrics(dm);   
         int screenWidth = dm.widthPixels;      
            
         // Get tab counts   
         TabWidget tabWidget = mTabHost.getTabWidget();   
         int count = tabWidget.getChildCount();   
         if (count > 3) {   
             for (int i = 0; i < count; i++) {   
                 tabWidget.getChildTabViewAt(i).setMinimumWidth((screenWidth) / 3);   
             }   
         }
         final int positionXY=(screenWidth) / 3;
         
         //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
         position = getIntent().getExtras().getInt("position");
         mTabHost.setCurrentTab(position);
         //this.mTabHost.setCurrentTab(position);
         final HorizontalScrollView hScrollView = (HorizontalScrollView)findViewById(R.id.scroller);
         hScrollView.post(new Runnable() {
        	 @Override     public void run() {         
        		 hScrollView.scrollTo(positionXY*position-100, 0);     
        		 }  
        	 }); 
         
         
         Log.e("position","position:"+position);
         Log.e("position","current:"+mTabHost.getCurrentTab());
         
    }
    
    private Drawable resize(Drawable image) {
    	Bitmap d = ((BitmapDrawable)image).getBitmap();
    	Bitmap bitmapOrig = Bitmap.createScaledBitmap(d, 100, 100, false);
    	return new BitmapDrawable(bitmapOrig); 
    }
    
    private int stringToRes(String name){
        int dotPos = name.lastIndexOf(".");
        String iconName = name.substring(0,dotPos);
        return getResources().getIdentifier(iconName, "drawable", getPackageName());
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

    /**
     * This is a helper class that implements a generic mechanism for
     * associating fragments with the tabs in a tab host.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between fragments.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabManager supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct fragment shown in a separate content area
     * whenever the selected tab changes.
     */
    public static class TabManager implements TabHost.OnTabChangeListener {
        private final FragmentActivity mActivity;
        private final TabHost mTabHost;
        private final int mContainerId;
        private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
        TabInfo mLastTab;

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;
            private Fragment fragment;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
            mActivity = activity;
            mTabHost = tabHost;
            mContainerId = containerId;
            mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mActivity));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                ft.detach(info.fragment);
                ft.commit();
            }

            mTabs.put(tag, info);
            mTabHost.addTab(tabSpec);
        }

        @Override
        public void onTabChanged(String tabId) {
            TabInfo newTab = mTabs.get(tabId);
            Log.e("Activity tabId","tabId:"+tabId);
            Log.e("Activity mLastTab",mLastTab+"");
            Log.e("Activity newTab",newTab+"");
            pageNum = mTabHost.getCurrentTab();
        	Log.e("onTabChanged","pageNum:"+pageNum+"");
            if(position!=-1){            
	        	if(pageNum == 0){
					Intent intent = new Intent();
					intent.setClass(mActivity, IndexPage.class);
					mActivity.startActivity(intent); 
					mActivity.finish();
				}
            }
            if (mLastTab != newTab) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                if (mLastTab != null) {
                    if (mLastTab.fragment != null) {
                        ft.detach(mLastTab.fragment);
                    }
                }
                else{
                    Log.e("MainTabActivity","mLastTab == null");
                }
                
                if (newTab != null) {
                    newTab.fragment = Fragment.instantiate(mActivity,
                            newTab.clss.getName(), newTab.args);
                    ft.add(mContainerId, newTab.fragment, newTab.tag);
                    if (newTab.fragment == null) {
                        
                        
                        //ft.detach(mLastTab.fragment);
                    } else {
//                        Log.e("MainTabActivity","newTab.fragment != null");
                        mActivity.getSupportFragmentManager().popBackStack();
                        ft.replace(mContainerId, newTab.fragment);
                        ft.attach(newTab.fragment);
                    }
                    
                }
                else{
                    Log.e("MainTabActivity","newTab == null");
                }
                mLastTab = newTab;
                ft.commit();
                mActivity.getSupportFragmentManager().executePendingTransactions();
            }
            else{
                Log.e("MainTabActivity","mLastTab == newTab");
            }
        }
    }
}

