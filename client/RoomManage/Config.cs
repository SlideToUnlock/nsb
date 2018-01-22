using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RoomManage
{
    class Config
    {
        /**
         * 配置系统的配置信息
         * */
        // 系统退出密码
        public static string ExitPwd { get; set; } = "111111";

        /**
         * HOST = ''
         * PORT = 
         * */

        // 服务器地址
        //public static string HOST { get; set; } = "10.64.16.201";
        public static string HOST { get; set; } = "127.0.0.1";
        //public static string HOST { get; set; } = "10.70.26.76";

        // 服务器端口
        public static int PORT { get; set; } = 9000;

        /**
         * 功能描述：格式化输出
         * */
        public static string OutputLog(string str)
        {
            string Log =  DateTime.Now.ToString("MM-dd HH:mm:ss")+":"+str;
            return Log;
        }
    }
}
