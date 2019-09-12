// mymenubar.js

function MyMenuBar()
{
	Menu.prototype.cssFile = "/PUB_J2EE/skins/officexp/officexp.css";
	
	
	//--------------仓库管理 ----------------
	
	var m_jcgl = new Menu();
	
	var m_jcgl_item1 = new MenuItem("进仓单登记","/PUB_J2EE/form/CKGL/JCGL/JCDJ.jsp");
	m_jcgl_item1.target = "_top";
	
	var m_jcgl_item2 = new MenuItem("进仓单查询","/PUB_J2EE/form/CKGL/JCGL/JCCX.jsp");
	m_jcgl_item2.target = "_top";
	
	
	var m_jcgl_item3 = new MenuItem("出仓单登记","/PUB_J2EE/form/CKGL/CCGL/CCDJ.jsp");
	m_jcgl_item2.target = "_top";
	
	var m_jcgl_item4 = new MenuItem("出仓单查询","/PUB_J2EE/form/CKGL/CCGL/CCCX.jsp");
	m_jcgl_item2.target = "_top";
	
	m_jcgl.add(m_jcgl_item1);
	m_jcgl.add(m_jcgl_item2);
	m_jcgl.add(m_jcgl_item3);
	m_jcgl.add(m_jcgl_item4);

	
	
	//--------------商品管理 ----------------
	
	var m_goods = new Menu();
	

	
	//--------------盘点管理 ----------------
	
	var m_pandian = new Menu();
	
	var m_pandian_item1 = new MenuItem("进库单报表查询","/PUB_J2EE/form/PDGL/JKD-CX.jsp");
	m_pandian_item1.target = "_top";
	
	var m_pandian_item2 = new MenuItem("出库单报表查询","/PUB_J2EE/form/PDGL/CKD-CX.jsp");
	m_pandian_item2.target = "_top";
	
	var m_pandian_item3 = new MenuItem("当前库存报表查询","/PUB_J2EE/form/PDGL/DQK-CX.jsp");
	m_pandian_item2.target = "_top";
	
	m_pandian.add(m_pandian_item1);
	m_pandian.add(m_pandian_item2);
	m_pandian.add(m_pandian_item3);



	//登陆相关
	var m_login = new Menu();
	var m_login_item1 = new MenuItem("重新登陆","/PUB_J2EE/Login.jsp");
	m_login_item1.target = "_top";
	m_login.add(m_login_item1);
	
	
	//环境设置
	var m_set = new Menu();
	var m_set_item1 = new MenuItem("环境设置","/PUB_J2EE/base/config/BaseConfig.jsp");
	m_set_item1.target = "_top";
	m_set.add(m_set_item1);
	
	//短消息
	var m_message = new Menu();
	var m_message_item1 = new MenuItem("收件箱","/PUB_J2EE/form/Message/QueryMessage.jsp?Type=1");
	var m_message_item2 = new MenuItem("发送消息","/PUB_J2EE/form/Message/SendMessage.jsp");
	m_message_item1.target = "_top";
	m_message_item2.target = "_top";
	m_message.add(m_message_item1);
	m_message.add(m_message_item2);
	//========================================================================
	//创建父菜单
	var menubar = new MenuBar();
	
	var mb_button0 = new MenuButton("我的消息", m_message);
	var mb_button1 = new MenuButton("仓库管理", m_jcgl);

	
	var mb_button2 = new MenuButton("商品管理", m_goods);

	
	var mb_button3 = new MenuButton("盘点管理",m_pandian);
	
	var mb_button4 = new MenuButton("环境设置",m_set);
	
	var mb_button5 = new MenuButton("登陆相关",m_login);

	menubar.add(mb_button0);		
	menubar.add(mb_button1);
	menubar.add(mb_button2);
	menubar.add(mb_button3);
	menubar.add(mb_button4);
	menubar.add(mb_button5);

	
	
	return menubar;
}