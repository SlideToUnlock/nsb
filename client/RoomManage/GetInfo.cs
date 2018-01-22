using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Management;
using System.Net;
using System.Net.NetworkInformation;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace RoomManage
{
    /**
     * 获得本机信息（IP、MAC、进程信息）
     * 执行 
     * */
    class GetInfo
    {
        public string IP { get; set; }
        public string GetIpInfo()
        {
            NetworkInterface[] nics = NetworkInterface.GetAllNetworkInterfaces();
            foreach (NetworkInterface adapter in nics)
            {
                // 判断是否为以太网卡
                // Wireless80211         无线网卡    
                // Ppp                   宽带连接
                // Ethernet              以太网卡   
                if (adapter.NetworkInterfaceType == NetworkInterfaceType.Ethernet)
                {
                    //获取以太网卡网络接口信息
                    IPInterfaceProperties ip = adapter.GetIPProperties();
                    //获取单播地址集
                    UnicastIPAddressInformationCollection ipCollection = ip.UnicastAddresses;
                    foreach (UnicastIPAddressInformation ipadd in ipCollection)
                    {
                        // InterNetwork          IPV4地址      
                        // InterNetworkV6        IPV6地址
                        // Max                   MAX 位址
                        if (ipadd.Address.AddressFamily == AddressFamily.InterNetwork)
                        {
                            //判断是否为ipv4
                            this.IP = ipadd.Address.ToString();//获取ip
                            break;
                        }
                    }
                    break;
                }
            }
            return this.IP;
        }

        public string GetMacInfo()
        {
            try
            {
                //获取网卡硬件地址 
                string mac = "";
                ManagementClass mc = new ManagementClass("Win32_NetworkAdapterConfiguration");
                ManagementObjectCollection moc = mc.GetInstances();
                foreach (ManagementObject mo in moc)
                {
                    if ((bool)mo["IPEnabled"] == true)
                    {
                        mac = mo["MacAddress"].ToString();
                        break;
                    }
                }
                return mac;
            }
            catch
            {
                return "unknow";
            }
        }

        public Dictionary<int, string> GetProcessInfo()
        {
            // 定义字典类型 把每个进程ID和进程名封装一个字典保存
            Dictionary<int, string> ProcessDic = new Dictionary<int, string>();
            Process[] localAll = Process.GetProcesses();
            foreach (var item in localAll)
            {
                string name = item.ProcessName;
                int pid = item.Id;
                ProcessDic.Add(pid, name);
            }
            return ProcessDic;
        }
    }
}
