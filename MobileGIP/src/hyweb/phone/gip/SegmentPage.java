package hyweb.phone.gip;


import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SegmentPage extends Fragment {
	private ListView listView;
	private ArrayAdapter<String> listAdapter;
	private ArrayList<String> list;
	private static Element root;
    private GetXmlRoot getRoot;
    String currentText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        getRoot = GetXmlRoot.getInstance();
        root = getRoot.getRoot(getActivity());
        
        //String currentText = getIntent().getExtras().getString(ConstantClass.XML_ATTRIBUTE_TEXT);
        
        Bundle bundle = this.getArguments(); 
        currentText = bundle.getString(ConstantClass.XML_ATTRIBUTE_TEXT);
		Log.e("segment onCreate",currentText);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		container.removeAllViews();
		Log.e("segment onCreateView",currentText);
		View v = inflater.inflate(R.layout.list, container, false);
        
		//setContentView(R.layout.list);
        listView = (ListView)v.findViewById(R.id.listView);
        list = new ArrayList<String>();
        
        NodeList nodes = root.getElementsByTagName("Node");

        for(int i=0; i<nodes.getLength(); i++){
            if(nodes.item(i).getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element)nodes.item(i);
                if(element.getAttribute(ConstantClass.XML_ATTRIBUTE_TEXT).equals(currentText)){
                    if(element.hasChildNodes()){
                        NodeList listValues = element.getChildNodes();
                        for(int j=0; j<listValues.getLength(); j++){
                            if(listValues.item(j).getNodeType() == Node.ELEMENT_NODE){
                                Element e = (Element)listValues.item(j);     
                                list.add(e.getAttribute(ConstantClass.XML_ATTRIBUTE_TEXT));
                            }
                        }
                    }
                    
                }
            }       
        }
        
        listAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
        listView.setAdapter(listAdapter);
        
        return v;
      }
    
}
