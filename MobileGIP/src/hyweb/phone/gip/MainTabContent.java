package hyweb.phone.gip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;



public class MainTabContent extends Fragment {
    String currentTabName;
    NodeList nodes;
    String menutype="";
    TabHost tabHost;
    
    TabHost mTabHost;
    TabManager mTabManager;
    
    View v = null;
    
    private static Element root;
    private GetXmlRoot getRoot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.e("content onCreate","onCreate");
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments(); 
        currentTabName = bundle.getString("id");
       // Log.e("id",currentTabName);
        getRoot = GetXmlRoot.getInstance();
        root = getRoot.getRoot(getActivity()); 
        
        nodes = root.getElementsByTagName("Node");
        //取得menutype值 判斷要以什麼型態顯示資料 list group segment grid
        //這裡怪怪的 感覺要爬很多才可以找到menutype 而且動作跟下面的程式碼很多都重複
        for(int i=0; i<nodes.getLength(); i++){
            if(nodes.item(i).getNodeType()==Node.ELEMENT_NODE){
                Element element = (Element)nodes.item(i);
                if(element.getAttribute(ConstantClass.XML_ATTRIBUTE_ID).equals(currentTabName)){
                    menutype = element.getAttribute("menutype");
                }
            }       
        }
        
    }


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	Log.e("menutype","menutype:"+menutype);
    	Log.e("onCreateView","onCreateView");
    	container.removeAllViews();
    	if(menutype != null){
            if(menutype.equals("segment")){
            	goSegment(inflater,container);
            }else if(menutype.equals("list")){
            	goList(inflater,container);
            }else if(menutype.equals("group")){
                goExpandList(inflater,container);
            }else{
            	if(!currentTabName.equals("menuRoot")){
	        	   	v = inflater.inflate(R.layout.hello_world, container, false);
	    	        View tv = v.findViewById(R.id.text);
	    	        ((TextView)tv).setText("id : " + currentTabName);
	    	        tv.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.gallery_thumb));
	            }
            }
        }
    	return v;
    }
	
	private void goSegment(LayoutInflater inflater, ViewGroup container) {
		// container.removeAllViews();
		v = inflater.inflate(R.layout.segment, container, false);
        
    	//tabHost = (TabHost) v.findViewById(R.id.TabHost01);
       // tabHost.setup();
		mTabHost = (TabHost)v.findViewById(R.id.TabHost01);
        mTabHost.setup();
        mTabManager = new TabManager(getActivity(), mTabHost, R.id.realtabcontents);
        
      
        for(int i=0; i<nodes.getLength(); i++){
            if(nodes.item(i).getNodeType()==Node.ELEMENT_NODE){
                Element element = (Element)nodes.item(i);
                setTabTitle(element, ConstantClass.XML_ATTRIBUTE_ID, currentTabName);
                //@@@@@@@@@@@@@@@@@@@@@@@@@@@
                //取消註解會錯
            }       
        }
	}
	
	private void goExpandList(LayoutInflater inflater, ViewGroup container) {
		//setContentView(R.layout.expandlist);
		v = inflater.inflate(R.layout.expandlist, container, false);
	    
		final ExpandableListView elv = (ExpandableListView)v.findViewById(R.id.mExpandableListView);
    	//準備一級清單中顯示的資料:2個一級清單,分別顯示"group1"和"group2"
    	List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
    	List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();
    	
		//程式碼寫得很亂@@ 不知道這樣寫好不好
        for(int i=0; i<nodes.getLength(); i++){
            if(nodes.item(i).getNodeType()==Node.ELEMENT_NODE){
                Element element = (Element)nodes.item(i);
                if(element.getAttribute(ConstantClass.XML_ATTRIBUTE_ID).equals(currentTabName)){
                	NodeList MenuNode=element.getChildNodes();
                	for(int j=0;j<MenuNode.getLength();j++){
                		Node node1=MenuNode.item(j); //node1
						if (node1.getNodeType() == Node.ELEMENT_NODE)  {
							Element element2 = (Element) node1;
							Map<String, String> group1 = new HashMap<String, String>();
					    	group1.put("group", element2.getAttribute(ConstantClass.XML_ATTRIBUTE_TEXT));
					    	groups.add(group1);
					    	NodeList MenuNodeNode=element2.getChildNodes();
					    	List<Map<String, String>> child1 = new ArrayList<Map<String, String>>();
					    	
					    	for(int k=0;k<MenuNodeNode.getLength();k++){
		                		Node node2=MenuNodeNode.item(k); //node1
								if (node2.getNodeType() == Node.ELEMENT_NODE)  {
									Element element3 = (Element) node2;
									
							    	Map<String, String> child1Data1 = new HashMap<String, String>();
							    	child1Data1.put("child", element3.getAttribute(ConstantClass.XML_ATTRIBUTE_TEXT));
							    	child1.add(child1Data1);
								}
							}
		                	childs.add(child1);
						}
                	}
                }
            }       
        }


    	ExpandableAdapter viewAdapter = new ExpandableAdapter(getActivity(), groups, childs);
    	elv.setAdapter(viewAdapter);
    	elv.setGroupIndicator(null); //不要有左邊的箭頭圖案

    	//預設為全部展開
    	int groupCount = viewAdapter.getGroupCount();
    	for (int i=0; i<groupCount; i++) {
    		elv.expandGroup(i);
    	} 
    	
    	elv.setOnGroupCollapseListener(new OnGroupCollapseListener(){
			@Override
			public void onGroupCollapse(int groupPosition) {
				// TODO Auto-generated method stub
				elv.expandGroup(groupPosition);
			}
    	});
    	
    	elv.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				return false;
			}
    		
    	});
	
	}
	private void goList(LayoutInflater inflater, ViewGroup container) {
			// TODO Auto-generated method stub
	    container.removeAllViews();
		v = inflater.inflate(R.layout.list, container, false);
		
		ArrayList<String> listText = new ArrayList<String>();
		final ArrayList<String> listId = new ArrayList<String>();
		final ArrayList<String> listMenuType = new ArrayList<String>();
		//程式碼寫得很亂@@ 不知道這樣寫好不好
	    for(int i=0; i<nodes.getLength(); i++){
	        if(nodes.item(i).getNodeType()==Node.ELEMENT_NODE){
	            Element element = (Element)nodes.item(i);
	            if(element.getAttribute(ConstantClass.XML_ATTRIBUTE_ID).equals(currentTabName)){
	            	NodeList MenuNode=element.getChildNodes();
	            	for(int j=0;j<MenuNode.getLength();j++){
	            		Node node1=MenuNode.item(j); //node1
						if (node1.getNodeType() == Node.ELEMENT_NODE)  {
							Element element2 = (Element) node1;
							listText.add(element2.getAttribute(ConstantClass.XML_ATTRIBUTE_TEXT));
							listId.add(element2.getAttribute(ConstantClass.XML_ATTRIBUTE_ID));
							listMenuType.add(element2.getAttribute(ConstantClass.XML_ATTRIBUTE_MENUTYPE));
						}
	            	}
	            }
	        }       
	    }
	    
	    ListView listView = (ListView)v.findViewById(R.id.listView);
	    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listText);
	    listView.setAdapter(listAdapter);
	    listView.setFocusable(true);
	    
	    listView.setOnItemClickListener(new OnItemClickListener() {

	           @Override
	           public void onItemClick(AdapterView<?> arg0, View arg1, int position,
	                   long arg3) {
	        	   if(listMenuType.get(position).equals("list")){
	        	       
//		        	   Intent intent = new Intent().setClass(getActivity(), ListPage.class); 
//		        	   intent.putExtra("id", listId.get(position)); 
//		        	   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		        	   View view = getLocalActivityManager().startActivity("ListPage"+position,intent).getDecorView();
//		        	   
//		        	   history.add(view);
//		        	   //container.addView(view);
//		        	   setContentView(view);
	        		   
	        		   Bundle args = new Bundle();
	       	           args.putString(ConstantClass.XML_ATTRIBUTE_ID, listId.get(position));
	       	        
	        		   Fragment newFragment =new ListPage();
	        	       //Fragment newFragment = ListPage.newInstance(listId.get(position));
		        	   FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		        	   newFragment.setArguments(args);
		        	   
		        	   ft.replace(R.id.realtabcontent, newFragment);
		               ft.setTransition(FragmentTransaction.TRANSIT_NONE);
		               ft.addToBackStack(null);
		               ft.commit();
	        	   }
	           }
	       });
	}


	private void setTabTitle(Element element, String attri, String name){
		
        if(element.getAttribute(attri).equals(name)){
            NodeList list = element.getChildNodes();
            for(int i=0; i<list.getLength(); i++){
                if(list.item(i).getNodeType()==Node.ELEMENT_NODE){
                    Element e = (Element)list.item(i);
//                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("id", e.getAttribute(ConstantClass.XML_ATTRIBUTE_ID));
//                    bundle.putString("text", e.getAttribute(ConstantClass.XML_ATTRIBUTE_TEXT));
//                    intent.putExtras(bundle);
//                    intent.setClass(getActivity(), SegmentPage.class);
////                    tabHost.addTab(tabHost.newTabSpec(e.getAttribute(ConstantClass.XML_ATTRIBUTE_TEXT))
//                            .setContent(intent)
//                            .setIndicator(e.getAttribute(ConstantClass.XML_ATTRIBUTE_TEXT))); 
                    Bundle bundle = new Bundle(); 
                    bundle.putString("id", e.getAttribute(ConstantClass.XML_ATTRIBUTE_ID)); 
                    bundle.putString("text", e.getAttribute(ConstantClass.XML_ATTRIBUTE_TEXT)); 

                    mTabManager.addTab(mTabHost.newTabSpec(e.getAttribute("id"))
                    		.setIndicator(e.getAttribute("text")),
                    		SegmentPage.class, bundle);
                }
            }
        }
    }
	
	
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
            Log.e("TabManager onTabChanged","tabId:"+tabId);
            TabInfo newTab = mTabs.get(tabId);
          //  int pageNum = mTabHost.getCurrentTab();
           //Log.e("content tag change",pageNum+"");
            Log.e("mTabs",mTabs+"");
            Log.e("mLastTab",mLastTab+"");
            Log.e("newTab",newTab+"");
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
              //  mActivity.getSupportFragmentManager().executePendingTransactions();
            }
            else{
                Log.e("MainTabActivity","mLastTab == newTab");
            }
            
        }
    }
}
