package test;

import com.easycode.framework.web.domain.server.Sys;

import java.util.HashSet;
import java.util.Iterator;

public class Test {
    public void test(){
        int [] data ={45,54,1,12,11,2,1,5,6,45};
        HashSet<Object> setCollection = new HashSet<>();
        for (int i = 0; i < data.length; i++) {
            setCollection.add(data[i]);
        }
        Iterator<Object> iterator = setCollection.iterator();
        while((iterator.hasNext())){
            System.out.println(iterator.next());
        }
    }

}
