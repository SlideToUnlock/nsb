using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Web.Script.Serialization;
using System.Windows;

namespace RoomManage
{
    class ScoketCommunication
    {
        /**
         * Socket通讯
         * 接收服务端发送指令
         * 向服务端发送数据
         * */
        private static Socket clientSocket;

        private static IPEndPoint ipe;
        // 客户端是否在线
        public Boolean Offline;
        static byte[] buffer = new byte[1024];
        public ExecuteCommand execute;
        /**
         * 链接服务器
         * */
         public void SocketConn()
        {
            try
            {
                Offline = false;
                IPAddress ip = IPAddress.Parse(Config.HOST);
                ipe = new IPEndPoint(ip, Config.PORT);
                clientSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                clientSocket.Connect(ipe);
                Console.WriteLine("client:connect to server success!", ConsoleColor.Green);
            }
            catch (Exception e)
            {

                MessageBox.Show(e.Message);
            }
        }

        /**
         * 关闭链接
         *      关闭socket通讯
         *      关闭心跳包线程
         * */
        public void ClostConnect()
        {
            // 关闭心跳包
            Offline = true;
            // 关闭Socket链接
            try
            {
                clientSocket.Close();
                Console.WriteLine("client:colse from server success!", ConsoleColor.Red);
            }
            catch (Exception e)
            {
                MessageBox.Show(e.Message);
            }
        }

        /**
         * 功能描述：判断socket是否连接
         * 过程描述：向服务器发送数据包进行验证
         * 返回描述：true：已连接；false：断开连接
         * */
        private bool IsSocketConnected(Socket client)
        {
            bool blockingState = client.Blocking;
            try
            {
                byte[] tmp = new byte[1];
                client.Blocking = false;
                client.Send(tmp, 0, 0);
                return true;
            }
            catch (SocketException e)
            {
                // 产生 10035 == WSAEWOULDBLOCK 错误，说明被阻止了，但是还是连接的  
                if (e.NativeErrorCode.Equals(10035))
                    return false;
                else
                    return true;
            }
            finally
            {
                // 恢复状态  
                client.Blocking = blockingState;    
            }
        }
        /**
         * 功能描述：开启心跳线程
         * */
        public void Start()
        {
            // 开启心跳线程  
            Thread t = new Thread(new ThreadStart(Heartbeat));
            t.IsBackground = true;
            t.Start();
            // 开启发送进程列表线程
            Thread p = new Thread(new ThreadStart(ProcessTh));
            p.IsBackground = true;
            p.Start();
        }
        /**
         * 功能描述：向服务端发送心跳包  
         * 过程描述：在开启心跳线程之前，先判断否链接到服务器，如果已经链接则使用
         *                  当前链接，如果没有链接则重新创建链接
         * */
        private void Heartbeat()
        {
            // 实现异步接受消息的方法 客户端不断监听消息  
            clientSocket.BeginReceive(buffer, 0, buffer.Length, SocketFlags.None, new AsyncCallback(ReceiveMsg), clientSocket);
            string sendStr = "Heartbeat";
            byte[] sendBytes = Encoding.UTF8.GetBytes(sendStr);
            while (!Offline)
            {
                // 发送心跳包
                clientSocket.Send(sendBytes);
                Thread.Sleep(1000 * 60 * 1);
            }
        }

        private void ProcessTh()
        {
            // 实现异步接受消息的方法 客户端不断监听消息  
            clientSocket.BeginReceive(buffer, 0, buffer.Length, SocketFlags.None, new AsyncCallback(ReceiveMsg), clientSocket);
            while (!Offline)
            {
                GetInfo getInfo = new GetInfo();
                // 发送进程心跳包
                Dictionary<int, string> processInfo = getInfo.GetProcessInfo();
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
                SentMsg(sendStr);
                Thread.Sleep(1000 * 60 * 2);
            }
        }

        /**
        * 功能描述：向服务端发送消息
        * 过程描述：向服务端发送消息，先判断是否链接到服务器，如果没有链接新建一个
        *                  链接进行发送消息。如果已经链接到服务器则直接发送消息。
        * */
        public void SentMsg(String msg)
        {
            bool SocketStatus = IsSocketConnected(clientSocket);
            if (SocketStatus)
            {
                // 发送消息
                byte[] sendBytes = Encoding.UTF8.GetBytes(msg);
                clientSocket.Send(sendBytes);
            }
            else
            {
                clientSocket.Connect(ipe);
                SentMsg(msg);
            }
        }

        /**
        * 接收服务端发送消息
        * */
        public void ReceiveMsg(IAsyncResult ar)
        {
            try
            {
                var socket = ar.AsyncState as Socket;
                var length = socket.EndReceive(ar);
                //读取出来消息内容  
                var message = Encoding.ASCII.GetString(buffer, 0, length);
                if (message.Length>0)
                {
                    // 如果消息不是null----执行相关操作
                    execute.doSomething(message);
                }
                //显示消息  
                Console.WriteLine("Recive:"+message, ConsoleColor.White);
                //接收下一个消息(因为这是一个递归的调用，所以这样就可以一直接收消息了）  
                socket.BeginReceive(buffer, 0, buffer.Length, SocketFlags.None, new AsyncCallback(ReceiveMsg), socket);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message, ConsoleColor.Red);
            }
        }
    }
}
