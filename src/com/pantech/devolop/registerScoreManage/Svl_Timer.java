package com.pantech.devolop.registerScoreManage;

import java.io.IOException;   
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;   
import javax.servlet.ServletContext;   
import javax.servlet.ServletException;   
import javax.servlet.http.HttpServlet;   
import javax.servlet.http.HttpServletRequest;   
import javax.servlet.http.HttpServletResponse;   
  
public class Svl_Timer extends HttpServlet {   
    private static final long serialVersionUID = 1L;   
  
    private Timer timer = null;   
       
    /**  
     * Constructor of the object.  
     */  
    public Svl_Timer() {   
        super();   
    }   
  
    /**  
     * Destruction of the servlet. 
     */  
    public void destroy() {   
        super.destroy();    
        if(timer != null){   
            timer.cancel();   
        }   
    }   
  
    public void doGet(HttpServletRequest request, HttpServletResponse response)   
            throws ServletException, IOException {   
           
    }   
  
    public void doPost(HttpServletRequest request, HttpServletResponse response)   
            throws ServletException, IOException {   
        doGet(request, response);          
    }   
  
    // init方法启动定时器   
    public void init() throws ServletException {   
        ServletContext sc = getServletContext();   
           
        //(true为用定时间刷新缓存)   
        String startTask = getInitParameter("startTask");
        
       /* timer = new Timer(true);
        timer.schedule(new Task(sc), 1000, 3000);*/
        
        //每天执行任务的时间
        String executeTime = sc.getInitParameter("ExecuteTime");
        
        //定时刷新时间(秒)   
        Long inspectionTime = Long.parseLong(sc.getInitParameter("InspectionTime"));  //获取巡检间隔时间，默认一天
    	if (inspectionTime == null)
    		inspectionTime = Long.parseLong("86400");  //一天(秒)
    	
    	//Long delay = Long.parseLong(sc.getInitParameter("Delay")); //获取定时器启动延迟，默认30秒
    	//计算当前时间到01:00:00的时间差，设置为延迟启动时间
    	SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long delay = null;
        try {
            java.util.Date begin = dfs.parse(dfs.format(new Date()));
            java.util.Date end = dfs.parse(sdf.format(new Date()) + " " + executeTime);
            //判断如果当前时间在设定时间之前，直接计算时间差
            //如果在设定时间之后的话，需要加一天时间后计算
            if(begin.before(end)){
            	delay = (end.getTime() - begin.getTime());// 得到两者的毫秒数
            }else if(begin.after(end)){
            	delay = ((end.getTime()+24*60*60*1000) - begin.getTime());// 得到两者的毫秒数
            }else if(begin.equals(end)){
            	delay = (end.getTime() - begin.getTime());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        if (delay == null)
    		delay = Long.parseLong("3");
    	
        //启动定时器   
        if("true".equalsIgnoreCase(startTask)){
            timer = new Timer(true);
            timer.schedule(new Task(sc), delay, inspectionTime*1000);
        }
    }   
}   
