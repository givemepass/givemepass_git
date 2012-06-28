package hyweb.phone.gip;


import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListPage extends ListFragment {
	private static Element root;
    private GetXmlRoot getRoot;
    private static String currentTabName;
    private NodeList nodes;

    
	    /*static ListPage newInstance(String string) {
	    	
	    	ListPage f = new ListPage();
	
	        // Supply num input as an argument.
	        Bundle args = new Bundle();
	        args.putString(ConstantClass.XML_ATTRIBUTE_ID, string);
	        f.setArguments(args);
	
	        return f;
	    }*/
	    
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Log.e("ListPage","onActivityCreated");
            currentTabName=getArguments().getString(ConstantClass.XML_ATTRIBUTE_ID);
            Log.e("currentTabName",currentTabName);
            getRoot = GetXmlRoot.getInstance();
            root = getRoot.getRoot(getActivity());
                
            nodes = root.getElementsByTagName(ConstantClass.XML_TAG_NODE);
            ArrayList<String> listText = new ArrayList<String>();
            final ArrayList<String> listId = new ArrayList<String>();
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
                            }
                        }
                    }
                }       
            }
            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, listText));
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("FragmentList", "Item clicked: " + id);
        }

        
        
	
}
