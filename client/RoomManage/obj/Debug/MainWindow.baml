   M S B A M L   `   `   `  ����E  ARoomManage, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null'clr-namespace:RoomManage
RoomManage  5   	       RoomManage.MainWindow   5   	   \ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5   	   R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	   @d2http://schemas.microsoft.com/expression/blend/2008     5   	   Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5   	   *localclr-namespace:RoomManage     5   	     � Title$  
MainWindow��5   	   $��673.415��6   $��845.4��6-    � Icon$ computer.ico��6;   .��5	      � $
��Left=�6   $	��835��6&   .+�5
   
   �� -   5         ��Name$ IpLab��5
      $��Ip地址��6   $
��Left=�6.   $��	61,54,0,0q�6I   $	��Top=�6\   �� 5   
   -   5        $ MacLab��5      $��物理地址��6    $
��Left=�6/   $��	48,85,0,0q�6J   $	��Top=�6]   �� 5   
   -   5         ��Name$ IpBox��5      $��23��6!   $��
111,54,0,0q�6-    �� TextWrapping$
 Wrap=�6A   $	��Top=�6U   $
��Left=�6m   $	��175��6�   �� 5   
   -   5        $ MacBox��5      $��23��6"   $��
111,89,0,0q�6.   $
 Wrap=�6B   $	��Top=�6V   $
��Left=�6n   $	��175��6�   �� 5   
   -   5         ��Name$ GetIpBtn��5      $��获取��6#   $��111,132,0,0q�60   $	��Top=�6E   $
��Left=�6r   $��93��6�   �� 5   
   -   5        $ 	SocketCon��5      $��链接服务器��6$   $
��Left=�64   $��
365,60,0,0q�6O   $	��Top=�6c   $��75��6{   6�   �� 5   
   -   5        $ KillProcess��5      $��关闭进程��6&   $��229,212,549,0q�65   $	��Top=�6L   6d   �� 5   
   -   5        $ SocketClose��5      $��关闭链接��6&   $��365,98,395,0q�65   $	��Top=�6K   6c   �� 5   
   -	   5        $ SendProcess��5      $��发送进程��6&   $��365,179,395,0q�65   $	��Top=�6L    J� RenderTransformOrigin$ 1.666,0.995&�6~   �� 5   
   -
   5        $	 Log��5      $
��Left=�6   $��458,54,0,25q�6:   $
 Wrap=�6O   $	��367��6c    J� IsManipulationEnabled . " �� VerticalScrollBarVisibility$
 Auto=�6�   $	 �� HorizontalScrollBarVisibility$
	 Auto=�6�   �� 5   
   -   5        
 ��Name$
 ProcessItemName��5      $
��Left=�6,   $��111,210,0,0q�6G   $	��Top=�6\   $	��113��6t   �� 5   
   -   5        $ ProcessList��5      $��进程��6%   $
��Left=�62   $��
61,205,0,0q�6M   $	��Top=�6a   �� 5   
   -   5        $ SendMsg��5      $��发送消息��6"   $
��Left=�61   $��365,137,0,0q�6L   $	��Top=�6a   $��75��6y   6�   5      5      