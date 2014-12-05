package com.example.bonusball;  
  
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bonusball.CanvasView.OnCompleteListener;
import com.example.bonusball.hzk.HZKUtils;
import com.example.bonusball.hzk.ScreenCal;
import com.example.bonusball.hzk.ScreenPoint;
public class BallActivity extends Activity {  
  
	private GestureDetector gd;  //���Ƽ���
	
    private CanvasView myCanvas;  
    //����Ļ�ϻ�һ��16*16�ĵ��� ��¼���е�����
    private ArrayList<ScreenPoint> screenlist;
  
    EditText inputEt=null;
    
    private String str="֣��";//��ʾ����
    private int ballNum=100;//�������
    private int index=0;
    
    private Handler handler;
    
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);    
        
        // ��ȡ��Ļ���
     	Display display = getWindowManager().getDefaultDisplay();
     	
     	screenlist=ScreenCal.screenCal(display.getWidth(), display.getHeight());
     	
        myCanvas=new CanvasView(this,display.getWidth(),display.getHeight());
        
        setContentView(myCanvas); 
        
        
        handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
//				if(msg.what==1)
//				{
//					myCanvas.random_update_ball_speed();
					

				/**
				 * ��������ƶ�2��֮����д��һ����
				 */
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				startGame(str, msg.what);
//				}
			}
        	
        };
        
        
        //��ʼ�������
        inputEt=new EditText(this);
        inputEt.setHint("������");
        
        gd=new GestureDetector(this,new OnDoubleClick());  
        
        /*
         * ��ʼ������
         */
        for(int i=0;i<ballNum;i++)
        	myCanvas.fireBall(); 
        
        
        //���ڻ�����ʱ�����
        myCanvas.setOnCompleteListener(new OnCompleteListener() {
			
			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				/**
				 * ��������
				 * ��ʼ����һ��
				 * index���˵ڼ���
				 */
				if(str.length()>1&&index!=(str.length()-1))//��ֹһ����
				{
					index++;
					
					
					handler.sendEmptyMessage(index);
				}
				else
				{
					index=0;//��¼g��0
				}
//				System.out.println("������ ��"+index+"�� �ܹ� "+str.length());
				
				
			}
		});
    }  
    
    

    /**
     * ��ʼ��ʾ��
     * @param string ��Ҫ��ʾ����
     */
	protected void startGame(String str,int position) {
		// TODO Auto-generated method stub
//		System.out.println("���ڻ���"+index+"�� �ܹ� "+str.length());
		//���ݺ��ֵõ���Ӧ�ĵ���
    	int[][] data=HZKUtils.readChinese(this,str.charAt(position));
    	
    	//���data�޸� screenlist�еĵ��flag
    	for(int i=0,length=data.length;i<length;i++)
        {
        	for (int j = 0; j < length; j++) {
        		//����Ĭ��ֵ
        		screenlist.get(i*length+j).setFalg(false);
        		//�õ�Ϊ1 ���״̬��Ϊtrue
        		//��ʾ��Ҫ�����ƶ�����Ӧ�ĵط�
        		if(data[i][j]==1)
        		{
        			screenlist.get(i*length+j).setFalg(true);
//        			System.out.print("��");
        		}
        		else
    			{
        			screenlist.get(i*length+j).setFalg(false);
//        			System.out.print(" ");
    			}
			}
//        	System.out.print("\n");
        }
    	myCanvas.formChinese(screenlist);
	    	
	    
		
		
	}
    
    @Override  
    public boolean onTouchEvent(MotionEvent event)  
    {  
    	
    	switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			myCanvas.isMouseDown=true;
			Log.i("BallActivity", "MotionEvent.ACTION_DOWN");
			break;
		case MotionEvent.ACTION_MOVE:
			
			myCanvas.isMouseDown=false;
			
			float x=event.getX();
	    	float y=event.getY();
	    	
			myCanvas.mouseX=(int)x;
	    	myCanvas.mouseY=(int)y;
	    	break;
		case MotionEvent.ACTION_UP:
			myCanvas.isMouseDown=false;
			Log.i("BallActivity", "MotionEvent.ACTION_UP");
			break;
		default:
			break;
		}
    	
        return gd.onTouchEvent(event);   //����˫�� ����
    }  
    
  
  
    class OnDoubleClick extends GestureDetector.SimpleOnGestureListener{  
    	
    
		@Override
		public boolean onSingleTapUp(MotionEvent event) {
			// TODO Auto-generated method stub
			
	    	
			//myCanvas.random_update_ball_speed();
//			myCanvas.isMouseDown=true;
//			Log.i("BallActivity", "onSingleTapUp");
			return false;
		}

		@Override  
        public boolean onDoubleTap(MotionEvent e) {  //˫��
            //TODO  
			myCanvas.random_update_ball_speed();
			Log.i("BallActivity", "onDoubleTap");
            return false;  
        }

		@Override
		public void onLongPress(MotionEvent event) {//����
			// TODO Auto-generated method stub
			super.onLongPress(event);
//			startGame(str);
			
	    	if(str.length()>0)
			{
				startGame(str,0);//��ʼд��
				index=0;
//				handler.sendEmptyMessage(0);
			}
//			Toast.makeText(BallActivity.this, "����", Toast.LENGTH_SHORT).show();
		}  
		
		
//        @Override  
//        public boolean onDoubleTapEvent(MotionEvent e) {  
//            return super.onDoubleTapEvent(e);  
//        }  
    }  
    



	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		myCanvas.clear();
		Log.i("BallActivity", "onDestroy");
	}  
    
    
    
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {  
        getMenuInflater().inflate(R.menu.main, menu);  
        
        return true;  
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		showInputDialog();//����������ʾ
		return super.onOptionsItemSelected(item);
	}  
    
	public void showInputDialog() {
		new AlertDialog.Builder(this)
				.setTitle("��ʾ")
				.setMessage("��������Ҫ��ʾ���֣�������Ҫ̫��")
				.setView(inputEt)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface di, int which) {
						String tem=inputEt.getText().toString();
						
						//û�����뺺��ʱ  ��ʾһ��
						if(tem.length()<1)
						{
							Toast.makeText(BallActivity.this, "û�������κκ��֣�", Toast.LENGTH_SHORT).show();
						}
						else
						{
							str=tem;//�޸ı��ش洢��ֵ
							startGame(str,0);
							index=0;
//							handler.sendEmptyMessage(0);
						}
						
					}

				}).show();
	}
	
}  