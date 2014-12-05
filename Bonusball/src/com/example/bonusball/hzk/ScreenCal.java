package com.example.bonusball.hzk;
import java.util.ArrayList;


public class ScreenCal {
	
	
	
	/**
	 * ������Ļ��С
	 * ����Ļ�м��һ��16*16�ĵ���
	 * ��������64��������
	 * @param width
	 * @param height
	 * @return ����һ���б�
	 */
	public static ArrayList<ScreenPoint> screenCal(int width,int height)
	{
		ArrayList<ScreenPoint> list=new ArrayList<ScreenPoint>();
		
		int l=width/17;//����ȡ�� ÿ����֮��Ŀ��
		
		int temp_x=(width-15*l)/2;//��һ��������ߵľ���
		
		int temp_y=(height-15*l)/2;//��һ�����붥���ľ���
		
		//����һȺ�㰴˳��ŵ�list��
		for(int i=0;i<256;i++)
		{
			int x=temp_x+l*(i%16);
			int y=temp_y+l*(i/16);
					
			ScreenPoint point=new ScreenPoint(x,y,false);
			list.add(point);
		}
		
		return list;
	}
	
	
}
