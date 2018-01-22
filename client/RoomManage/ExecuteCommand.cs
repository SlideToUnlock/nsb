using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace RoomManage
{
    class ExecuteCommand
    {
        /**
         * 执行命令（关机、关闭进程）
         * */
        public void ShutDownCommand(int Second=0)
        {
            /**
             * 执行关机命令，
             * 定时关机和立即关机
             * */
            if (Second != 0)
            {
                Process.Start("shutdown.exe", "-s -t " + Second.ToString());
            }
            else
            {
                Process.Start("shutdown.exe", "-s");
            }
        }

        public void KillProcess(string processName)
        {
            /**
             * 杀死进程
             * */
            try
            {
                string proName = processName;
                Process[] p = Process.GetProcessesByName(proName);
                p[0].Kill();
                MessageBox.Show("进程关闭成功！");
            }
            catch
            {

                MessageBox.Show("无法关闭此进程！"); ;
            }
        }
        /**
         * 将要执行的命令
         * */
        public void doSomething(string value)
        {
            Console.WriteLine(value);
        }
    }
}
