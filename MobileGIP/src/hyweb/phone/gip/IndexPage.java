package hyweb.phone.gip;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexPage extends Activity {
    private static Element root;
    private GetXmlRoot getRoot;
    private GridView indexGridView;
    private List<Map<String, Object>> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_gridview);
        indexGridView = (GridView)findViewById(R.id.inde_grid_view);
        
        
        items = new ArrayList<Map<String,Object>>();
        
        getRoot = GetXmlRoot.getInstance();
        root = getRoot.getRoot(getApplicationContext());
        NodeList TabbarNode = root.getElementsByTagName("Tabbar");
         
        for(int i=0;i<TabbarNode.getLength();i++){
            NodeList ItemNode =root.getElementsByTagName("item");
            for(int j=1;j<ItemNode.getLength();j++){
                Element tabbarElement = (Element)ItemNode.item(j);
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("image", stringToRes(tabbarElement.getAttribute("icon")));
                item.put("text", tabbarElement.getAttribute("text"));
                items.add(item);
            }
        }
        
        SimpleAdapter adapter = new SimpleAdapter(this, 
                items, R.layout.index_gridview_item, new String[]{"image", "text"},
                new int[]{R.id.image, R.id.text});
        indexGridView.setAdapter(adapter);
        indexGridView.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("position", position+1);
                intent.putExtras(bundle);
                intent.setClass(IndexPage.this, MainTabActivity.class);
                startActivity(intent);
                //finish();
            }
            
        });
    }
    private int stringToRes(String name){
        
        int dotPos = name.lastIndexOf(".");
        String iconName = name.substring(0,dotPos);
        return getResources().getIdentifier(iconName, "drawable", getPackageName());
    }
    
}
