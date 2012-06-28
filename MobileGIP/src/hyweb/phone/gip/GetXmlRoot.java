package hyweb.phone.gip;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class GetXmlRoot {
    private InputStream inputStream;
    private static Element root = null;
    private static GetXmlRoot mInstance = null;
    private GetXmlRoot(){}
    public static GetXmlRoot getInstance(){
        if(mInstance == null){
            mInstance = new GetXmlRoot();
        }
        return mInstance;
    }
    protected Element getRoot(Context mContext){
        if(root == null){
            try {
                inputStream = mContext.getAssets().open(ConstantClass.FILE_NAME);
                DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
                                
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(inputStream);
                root = document.getDocumentElement(); 
    
            } catch (ParserConfigurationException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return root;
    }
}
