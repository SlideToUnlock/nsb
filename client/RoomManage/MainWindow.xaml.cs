using System;
using System.Collections;
using System.Collections.Generic;
using System.Windows;
using System.Windows.Forms;
using System.Web.Script.Serialization;
using System.Threading;

namespace RoomManage
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {

        private static ScoketCommunication tcInt = new ScoketCommunication();
        public MainWindow()
        {
            // 默认不显示主窗体
            this.Visibility = Visibility.Hidden;
            InitializeComponent();
            InitialTray();
        }
        private NotifyIcon notifyIcon = null;



        /**
         * 设置托盘
         * */
        private void InitialTray()
        {
            // 获得电脑信息
            GetInfo getInfo = new GetInfo();
            string Ip = getInfo.GetIpInfo();
            string Mac = getInfo.GetMacInfo();
            Dictionary<int, string> process = getInfo.GetProcessInfo();

            //设置托盘的各个属性
            notifyIcon = new System.Windows.Forms.NotifyIcon();
            // 设置托盘图标
            notifyIcon.Icon = new System.Drawing.Icon(System.Windows.Forms.Application.StartupPath + "\\computer.ico");
            // 是否显示图标
            notifyIcon.Visible = true;

            //设置菜单项
            // System.Windows.Forms.MenuItem SetUp = new System.Windows.Forms.MenuItem("设置");
            // 鼠标点击设置 显示主窗体
            // SetUp.Click += Show_MainWindows;

            // 显示电脑IP菜单项
            System.Windows.Forms.MenuItem IP = new System.Windows.Forms.MenuItem(Ip);

            //退出菜单项
            System.Windows.Forms.MenuItem exit = new System.Windows.Forms.MenuItem("退出");
            // 退出按钮--退出输入密码
            exit.Click += Exit_Click;

            //关联托盘控件
            System.Windows.Forms.MenuItem[] childen = new System.Windows.Forms.MenuItem[] { IP, exit };
            notifyIcon.ContextMenu = new System.Windows.Forms.ContextMenu(childen);

            // 链接服务器
            connect_socket();
            // 第一次链接发送信息
            sent_msg(Ip, Mac);
            Thread.Sleep(1000 * 2);
            // 发送进程
            sent_process(process);
        }

        /**
         * 功能函数实现，不通过事件点击
         * */
        // 链接服务器
        private void connect_socket()
        {
            tcInt.SocketConn();
            string Log = Config.OutputLog("链接服务器成功！\n");
            Console.WriteLine(Log);
        }
        // 向服务器端发送消息以及心跳包
        private void sent_msg(string Ip,string Mac)
        {
            Hashtable ht = new Hashtable();
            ht.Add("ip", Ip);
            ht.Add("mac", Mac);
            ht.Add("command", "Heartbeat");

            JavaScriptSerializer js = new JavaScriptSerializer();
            string sendJs = js.Serialize(ht);
            string sendStr = sendJs.ToString();
            // Send Message
            tcInt.SentMsg(sendStr);
            Thread.Sleep(1000 * 2);
            // 开始心跳线程
            tcInt.Start();
        }
        // 发送进程
        private void sent_process(Dictionary<int, string> processInfo)
        {
            Hashtable ht = new Hashtable();
            foreach (KeyValuePair<int, string> itemInfo in processInfo)
            {
                ht.Add(itemInfo.Key.ToString(), itemInfo.Value.ToString());
                // 显示日志内容
                string showIndex = "PID:" + itemInfo.Key.ToString() + "----PNAME:" + itemInfo.Value.ToString() + "\n";
                string Log = Config.OutputLog(showIndex);
                Console.WriteLine(Log);
            }
            JavaScriptSerializer js = new JavaScriptSerializer();
            string sendJs = js.Serialize(ht);
            string sendStr = sendJs.ToString();
            tcInt.SentMsg(sendStr);
        }
        
        // 点击设置
        private void Show_MainWindows(object sender, EventArgs e)
        {
            this.Visibility = Visibility.Visible;
            this.WindowState = WindowState.Normal;
        }
        // 点击退出，显示密码输入
        private void Exit_Click(object sender, EventArgs e)
        {
            PwdExit pwdExit = new PwdExit();
            pwdExit.ShowDialog();
        }

        // 点击右上角关闭按钮隐藏到托盘
        protected override void OnClosing(System.ComponentModel.CancelEventArgs e)
        {
            //不显示在系统任务栏 
            this.ShowInTaskbar = false; 
            //托盘图标可见 
            notifyIcon.Visible = true;
            e.Cancel = true;
            // 将窗体变为最小化
            this.WindowState = WindowState.Minimized;
        }

        /**
         * 获得电脑的基本信息
         * */
        private void Btn_Get_Info(object sender, RoutedEventArgs e)
        {
            GetInfo getInfo = new GetInfo();
            string Ip = getInfo.GetIpInfo();
            string Mac = getInfo.GetMacInfo();
            Dictionary<int, string> process = getInfo.GetProcessInfo();

            this.IpBox.Text = Ip;
            this.MacBox.Text = Mac;
        }

        /**
         * 链接服务器
         * */
        private void SocketCon_Click(object sender, RoutedEventArgs e)
        {
            tcInt.SocketConn();
            string Log = Config.OutputLog("链接服务器成功！\n");
            this.Log.AppendText(Log);
        }
        /**
         * 关闭链接
         * */
        private void SocketClose_Click(object sender, RoutedEventArgs e)
        {
            tcInt.ClostConnect();
            string Log = Config.OutputLog("已断开服务器链接！\n");
            this.Log.AppendText(Log);
        }

        /**
         * 关闭进程
         * */
        private void KillProcess_Click(object sender, RoutedEventArgs e)
        {
            ExecuteCommand executeCommand = new ExecuteCommand();
            string processName = this.ProcessItemName.SelectedValue.ToString();
            executeCommand.KillProcess(processName);
            //executeCommand.ShutDownCommand();
        }

        /**
         * 向服务器发送进程信息
         * */
        private void SendProcess_Click(object sender, RoutedEventArgs e)
        {
            GetInfo getInfo = new GetInfo();
            Dictionary<int, string> processInfo = getInfo.GetProcessInfo();
            Hashtable ht = new Hashtable();
            foreach (KeyValuePair<int, string> itemInfo in processInfo)
            {
                ht.Add(itemInfo.Key.ToString(), itemInfo.Value.ToString());
                // 显示日志内容
                string showIndex ="PID:"+ itemInfo.Key.ToString() + "----PNAME:" + itemInfo.Value.ToString() +"\n";
                string Log = Config.OutputLog(showIndex);
                this.Log.AppendText(Log);
                // 下拉框显示内容
                this.ProcessItemName.Items.Add(itemInfo.Value.ToString());
            }
            JavaScriptSerializer js = new JavaScriptSerializer();
            string sendJs = js.Serialize(ht);
            string sendStr = sendJs.ToString();
            tcInt.SentMsg(sendStr);
        }
        /**
         * 功能描述：向服务器端发送消息以及心跳包
         * */
        private void SendMsg_Click(object sender, RoutedEventArgs e)
        {
            // 获得信息进行发送
            GetInfo getInfo = new GetInfo();
            string Ip = getInfo.GetIpInfo();
            string Mac = getInfo.GetMacInfo();

            Hashtable ht = new Hashtable();
            ht.Add("ip", Ip);
            ht.Add("mac", Mac);
            ht.Add("command", "Heartbeat");

            JavaScriptSerializer js = new JavaScriptSerializer();
            string sendJs = js.Serialize(ht);
            string sendStr = sendJs.ToString();
            // Send Message
            tcInt.SentMsg(sendStr);
            Thread.Sleep(1000 * 2);
            // 开始心跳线程
            tcInt.Start();
        }
    }
}
