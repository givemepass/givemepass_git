package hyweb.phone.gip;


import java.util.List;
import java.util.Map;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

//自訂的ExpandListAdapter
public class ExpandableAdapter extends BaseExpandableListAdapter{
	private Context context;
	List<Map<String, String>> groups;
	List<List<Map<String, String>>> childs;
	
	
	/*
	* 構造函數:
	* 參數1:context物件
	* 參數2:一級清單資料來源
	* 參數3:二級清單資料來源
	*/
	public ExpandableAdapter(Context context, List<Map<String, String>> groups, List<List<Map<String, String>>> childs){
		this.groups = groups;
		this.childs = childs;
		this.context = context;
	}
	
	
	public Object getChild(int groupPosition, int childPosition){
		return childs.get(groupPosition).get(childPosition);
	}
	
	
	public long getChildId(int groupPosition, int childPosition){
		return childPosition;
	}
	
	
	//獲取二級清單的View物件
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
	ViewGroup parent){
		@SuppressWarnings("unchecked")
		String text = ((Map<String, String>) getChild(groupPosition, childPosition)).get("child");
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		
		//獲取二級清單對應的佈局檔, 並將其各元素設置相應的屬性
		LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.expandlist_child, null);
		TextView tv = (TextView) linearLayout.findViewById(R.id.child_tv);
		tv.setText(text);
		//ImageView imageView = (ImageView)linearLayout.findViewById(R.id.child_iv);
		//imageView.setImageResource(R.drawable.icon);
		
		
		return linearLayout;
	}
	
	
	public int getChildrenCount(int groupPosition){
		return childs.get(groupPosition).size();
	}
	
	
	public Object getGroup(int groupPosition){
		return groups.get(groupPosition);
	}
	
	
	public int getGroupCount(){
		return groups.size();
	}
	
	
	public long getGroupId(int groupPosition){
		return groupPosition;
	}
	
	
	//獲取一級清單View物件
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
		String text = groups.get(groupPosition).get("group");
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//獲取一級清單佈局檔,設置相應元素屬性
		LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.expandlist_group, null);
		TextView textView = (TextView)linearLayout.findViewById(R.id.group_tv);
		textView.setText(text);

		return linearLayout;
	}
	
	
	public boolean hasStableIds(){
		return false;
	}
	
	
	public boolean isChildSelectable(int groupPosition, int childPosition){
		return true;
		//子節點可以被點
	}
	
}

