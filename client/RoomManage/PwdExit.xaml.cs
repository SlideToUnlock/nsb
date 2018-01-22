using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace RoomManage
{
    /// <summary>
    /// PwdExit.xaml 的交互逻辑
    /// </summary>
    public partial class PwdExit : Window
    {

        
        public PwdExit()
        {
            InitializeComponent();
            
        }

        private void Button_Clicked(object sender, RoutedEventArgs e)
        {
            /**
             * 判断按钮类型{Apply、Cancel}
             * Apply : 判断用户输入密码是否正确，如果正确程序退出,如果错误继续输入密码。
             * Cancel: 关闭当前的窗体。
             * */

            var Btn = sender as Button;
            string InputPwd = this.PwdEidt.Password;
            if (Btn.Name == "Apply")
            {
                if (InputPwd == Config.ExitPwd)
                {
                    Application.Current.Shutdown();
                }
                else
                {
                    MessageBox.Show("请重新输入密码","确定",MessageBoxButton.YesNo,MessageBoxImage.Warning,MessageBoxResult.No); 
                }
            }
            else
            {
                this.Close();
            }
        }
    }
}
