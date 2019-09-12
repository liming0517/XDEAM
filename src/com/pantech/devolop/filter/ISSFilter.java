package com.pantech.devolop.filter;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.util.AssertionHolder;

import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.TraceLog;
import com.pantech.src.develop.store.user.UserProfile;

public class ISSFilter implements Filter{
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse)servletResponse;
		HttpSession session = request.getSession();
		
		//通过CAS的API获得登陆账号
		System.out.println("loginName:--------");
		String loginName = "post";
		//String loginName = AssertionHolder.getAssertion().getPrincipal().getName();
		System.out.println("loginName:--------"+loginName);
		try {
			//执行本系统的登陆。跟平常同时校验用户名和密码不同，这里只有用户名。
			executeLoginProc(request,response,loginName,session);
		} catch (Exception e) {
			//logger.log(Level.SEVERE, "executeLoginProc error:", e);
			return;
		}
		
		//登陆成功
		session.setAttribute("AURORA_USER_LOGIN", Boolean.TRUE);
		
		//在session中自定义一个参数，以它来校验是否完成过自动登陆
		Object user_login = session.getAttribute("AURORA_USER_LOGIN");
				
		if (user_login != null){
			//登陆过，就继续执行其他filter
			filterChain.doFilter(request, response);
			return;
		}else{
			//跳转到登陆页面
			response.sendRedirect("http://pkxt.shxj.edu.cn:8080/ISS");
		}
		
//		//跳转到登陆成功后的页面
//		response.sendRedirect("http://192.168.252.55:8080/ISS/mainframe.jsp");
	}

	private void executeLoginProc(HttpServletRequest request, HttpServletResponse response, String loginName,HttpSession session) {
		// TODO Auto-generated method stub
		System.out.println("usercode=========="+loginName);
    	try {
			try {
				UserProfile up = new UserProfile(request,response,loginName,"","SSO");
				int s=up.getLoginState();
				TraceLog.Trace("用户"+loginName+"登录验证状态："+s);
				if(s==0){
					TraceLog.Trace("用户名或密码错误");
					//response.sendRedirect(casServerLoginUrl);
					return;
				}else if(s==2){
					TraceLog.Trace("用户名或密码为空");
					//response.sendRedirect(casServerLoginUrl);
					return;
				}else if(s==3){
					TraceLog.Trace("系统版本已过期");
					//response.sendRedirect(casServerLoginUrl);
					return;
				}else if(s==4){
				TraceLog.Trace("SSO错误");
				//response.sendRedirect(casServerLoginUrl);
				return;
					}		else if(s!=1){
					//其他错误
					TraceLog.Trace("其他错误");
					//response.sendRedirect(casServerLoginUrl);
					return;
				}
				//将用户对象存入session
				//session.setAttribute("UserProfile",up);
				//将用户对象存入session
				//session.setAttribute("UserProfile",up);
				//开始初始化Session对象
				
				session.setAttribute("UserCode",up.getUserCode()); //用户编号
				session.setAttribute("UserName",up.getUserName()); //用户名称
				session.setAttribute("UserAuth", up.getUserAuth());//用户角色
				session.setAttribute("UserDept", up.getUserDept());//用户所属部门
				//session.setAttribute("ModuleId", up.getModuleId());//用户所属部门
				session.setAttribute("UserModule", up.getUserModule());//用户所属部门
				session.setAttribute("UserState", up.getUserState());//用户所属部门
				session.setAttribute("ModuleId", up.getModuleId());//用户所属部门
				session.setAttribute("UserPassword", up.getUserPassword());
				session.setAttribute("isSSOLogin", "true");
				//记录登录信息
				up.recordLoginInfo("I");
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			//植入监听器（用于监控用户登录状态）
			//SessionListener sessionListener=new SessionListener();  //对于每一个会话过程均启动一个监听器
			//session.setAttribute("listener",sessionListener);
			//记录语言
			//if (MyTools.StrFiltr(language).trim().equalsIgnoreCase("")){
				String language="language_C"; //默认中文
			//}
			session.setAttribute("language",language);
		    // 多参数、复杂的targetUrl被base64加密，在此需要先解密
	        //targetUrl = targetUrl.replace("{base64}", "");
	       // targetUrl = Base64Util.decode(targetUrl);
	        //response.sendRedirect(targetUrl);
		} catch (WrongSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}
}

