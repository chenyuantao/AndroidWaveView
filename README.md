# AndroidWaveView
Android仿拉勾网app的波浪View
###效果图
![效果图](http://img.blog.csdn.net/20160717175731335)
###使用方法
在xml中，
	<cn.chenyuantao.view.WaveView
	        android:layout_width="match_parent"
	        android:layout_height="200dp" />
在Activity中，
	//改变颜色
	waveView.setColor(Color.RED);
	//开始(无需手动调用)
	waveView.start();
	//停止
	waveView.stop();