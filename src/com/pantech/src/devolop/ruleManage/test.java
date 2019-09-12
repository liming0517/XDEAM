package com.pantech.src.devolop.ruleManage;

import java.net.NetworkInterface;
import java.sql.SQLException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import com.pantech.base.common.db.DBSource;

public class test {
	
	private test() {  
    } 
	
	public static DBSource db;
	
	
	 
  
    /** 
     * 按照"XX-XX-XX-XX-XX-XX"格式，获取本机MAC地址 
     * @return 
     * @throws Exception 
     */  
    public static String getMacAddress() throws Exception{  
        Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();  
          
        while(ni.hasMoreElements()){  
            NetworkInterface netI = ni.nextElement();  
              
            byte[] bytes = netI.getHardwareAddress();  
            if(netI.isUp() && netI != null && bytes != null && bytes.length == 6){  
                StringBuffer sb = new StringBuffer();  
                for(byte b:bytes){  
                     //与11110000作按位与运算以便读取当前字节高4位  
                     sb.append(Integer.toHexString((b&240)>>4));  
                     //与00001111作按位与运算以便读取当前字节低4位  
                     sb.append(Integer.toHexString(b&15));  
                     sb.append("-");  
                 }  
                 sb.deleteCharAt(sb.length()-1);  
                 return sb.toString().toUpperCase();   
            }  
        }  
        return null;  
    }  
      

    /**
	 * @param args
	 */
	@SuppressWarnings("null")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		 String str = "2016-02-25"; 
		 Date dt=sdf.parse(str,new   ParsePosition(0)); 
		 Calendar rightNow = Calendar.getInstance(); 
		 rightNow.setTime(dt); 
		 rightNow.add(Calendar.DATE,-1);//你要加减的日期   
		 Date dt1=rightNow.getTime(); 
		 String reStr=sdf.format(dt1); 
		 //System.out.println(reStr);   
		 
		 
		 try {
			System.out.println(test.getMacAddress());
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }  
	}
	
	
}
