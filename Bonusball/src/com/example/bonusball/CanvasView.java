package com.example.bonusball;  
  
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import com.example.bonusball.ball.BaseBall;
import com.example.bonusball.ball.BallForCH;
import com.example.bonusball.hzk.ScreenPoint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
  
public class CanvasView extends SurfaceView implements SurfaceHolder.Callback  
{  

	/**Ħ����*/
	private final static float FRICTION=0.96f;		// 
	
	private final static String TAG = "CanvasView";
	
	private ArrayList<ScreenPoint> screenlist=null;
	//�ⲿ���� ������ɵĽӿ�
	private OnCompleteListener myCompleteListener;
	
    private SurfaceHolder myHolder;  
    private Paint ballPaint; // Paint used to draw the cannonball  
    private int screenWidth; // width of the screen  
    private int screenHeight; // height of the screen  
    private int maxBallRadius=10;  
    private CanvasThread myThread;  
    private List<BallForCH> ballList;//����С��ļ���
    private Paint backgroundPaint;  
    private Random mRandom;  
    
    //����ѭ��  
    boolean isLoop;  
    
    int	mouseX, mouseY;// ��ǰ�������
    int	mouseVX, mouseVY;// ����ٶ�
    int	prevMouseX,	prevMouseY;// �ϴ��������
    boolean	isMouseDown=false;// �������Ƿ���
  
    public CanvasView(Context context,int width,int height) {  
        super(context);   
        // TODO Auto-generated constructor stub  
        this.screenWidth=width;
        this.screenHeight=height;
        
        myHolder=this.getHolder();  
        myHolder.addCallback(this);  
        ballPaint=new Paint();  
  
        backgroundPaint = new Paint();  
        backgroundPaint.setColor(Color.BLACK);  
        isLoop = true;  
        ballList=new CopyOnWriteArrayList<BallForCH>();  
        mRandom=new Random();  
        
    	// ��ʼ��������
    	mouseX = prevMouseX = width / 2;
    	mouseY = prevMouseY = height / 2;
        
       
    }  
  
    public void fireBall()
    {
    	//���������ɫ
        int ranColor = 0xff000000 | mRandom.nextInt(0x00ffffff);
        
        //��������뾶
        float randomRadius=mRandom.nextInt(maxBallRadius);  
        float tmpRadius=maxBallRadius/5.0>randomRadius?maxBallRadius:randomRadius;  
       
//        tmpRadius=maxBallRadius;
        
        float pX=screenWidth * 0.5f; 
        float pY=screenHeight * 0.5f;
        int i=ballList.size();
        float speedX=(float) Math.cos(i)*mRandom.nextInt(34);
        float speedY=(float) Math.sin(i)*mRandom.nextInt(34);
        
        ballList.add(new BallForCH(ranColor,tmpRadius,pX,pY,speedX,speedY));  
    }
    
    public void fireBall(float startX,float startY,float velocityX,float velocityY)  
    {  
    	//���������ɫ
        int ranColor = 0xff000000 | mRandom.nextInt(0x00ffffff);
        
        //��������뾶
        float randomRadius=mRandom.nextInt(maxBallRadius);  
        float tmpRadius=maxBallRadius/5.0>randomRadius?maxBallRadius:randomRadius;  
        
        ballList.add(new BallForCH(ranColor,tmpRadius,startX,startY,velocityX,velocityY));  
//        System.out.println("Fire");  
    }  
  
    @Override  
    public void surfaceChanged(SurfaceHolder holder, int format, int width,  
            int height) {  
        // TODO Auto-generated method stub  
  
  
    }  
    @Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh)  
    {  
        super.onSizeChanged(w, h, oldw, oldh);  
        screenWidth = w; // store the width  
        screenHeight = h; // store the height  
        maxBallRadius=w/10;  
    }  
  
    @Override  
    public void surfaceCreated(SurfaceHolder holder) {  
        // TODO Auto-generated method stub  
        myThread = new CanvasThread();  
        System.out.println("SurfaceCreated!");  
        myThread.start();   
  
    }  
  
    @Override  
    public void surfaceDestroyed(SurfaceHolder holder) {  
        // TODO Auto-generated method stub  
        // ֹͣѭ��  
        isLoop = false;  
    }  
    
    /**
     * �ڻ�����
     * �ı�����С���״̬
     * @param canvas
     */
    public void drawGameElements(Canvas canvas)  
    {  
  
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);  
        for(BaseBall b:ballList)  
        {  
            ballPaint.setColor(b.getColor());  
            canvas.drawCircle(b.getX(),b.getY(),b.getRadius(),ballPaint);  
        }  
    }  
    
    
    
    /**
     * �ı��������λ��
     * ����ÿ����ǰ���ٶ�
     * ��������߽� ����
     */
    private void updatePositions() {  
        // TODO Auto-generated method stub  
     
        //�����д����ֹͣ�߳�
        //�����������򡣡����ٳ��ܲ���λ        

		mouseVX    = mouseX - prevMouseX;
		mouseVY    = mouseY - prevMouseY;
		prevMouseX = mouseX;
		prevMouseY = mouseY;
		
		float toDist   = screenHeight * 0.86f;
		float stirDist = screenHeight * 0.125f;
		float blowDist = screenHeight * 0.5f;
        
        for(BallForCH b:ballList)  
        {  
			float x  = b.getX();
			float y  = b.getY();
			float vX = b.getVX();
			float vY = b.getVY();
			
			float dX = x - mouseX;
			float dY = y - mouseY; 
			if(b.isReadyToForm())//��ǰ���ڹ�������״̬
        	{
				dX = x - b.getTargetX();
				dY = y - b.getTargetY();
				
        	} 
			
			float d  = (float)Math.sqrt(dX * dX + dY * dY);
			dX = d>0 ? dX / d : 0;
			dY = d>0 ? dY / d : 0;
			
        	
   			//��갴�¼��� �����Ļ������
			if (isMouseDown && d < blowDist)
			{
				float blowAcc = (1 - (d / blowDist)) * 14;
				vX += dX * blowAcc + 0.5f - mRandom.nextFloat()/Integer.MAX_VALUE;
				vY += dY * blowAcc + 0.5f - mRandom.nextFloat()/Integer.MAX_VALUE;
			}
			//�޸��ٶ�
			//�봥����ԽԶ�ٶ�ԽС
			if (d < toDist)
			{
				float toAcc = (1 - (d / toDist)) * screenHeight * 0.0014f;
				vX -= dX * toAcc;
				vY -= dY * toAcc;			
			}
			
			if (d < stirDist)
			{
				float mAcc = (1 - (d / stirDist)) * screenHeight * 0.00026f;
				vX += mouseVX * mAcc;
				vY += mouseVY * mAcc;			
			}
			
			vX *= FRICTION;
			vY *= FRICTION;
			
			float avgVX = (float)Math.abs(vX);
			float avgVY = (float)Math.abs(vY);
			float avgV  = (avgVX + avgVY) * 0.5f;
			
			if (avgVX < 0.1) vX *= mRandom.nextFloat()/Integer.MAX_VALUE * 3;//float(mRandom.nextInt()) / Integer.MAX_VALUE * 3;
			if (avgVY < 0.1) vY *= mRandom.nextFloat()/Integer.MAX_VALUE * 3;
			
			float sc = avgV * 0.45f;
			sc = Math.max(Math.min(sc, 3.5f), 0.4f);
			
			float nextX = x + vX;
			float nextY = y + vY;
			
			if		(nextX > screenWidth)	{ nextX = screenWidth;	vX *= -1; }
			else if (nextX < 0)		{ nextX = 0;		vX *= -1; }
			if		(nextY > screenHeight){ nextY = screenHeight;	vY *= -1; }
			else if (nextY < 0)		{ nextY = 0;		vY *= -1; }
			
			b.setVX(vX);
			b.setVY(vY);
			b.setPosX(nextX);
			b.setPosY(nextY);
        	
        }
    }  

    public void formChinese(ArrayList<ScreenPoint> list)
    {
    	screenlist=new ArrayList<ScreenPoint>();
    	//ȡ����Ҫ���������
    	for(ScreenPoint point : list) {
    		if(point.isFalg()) {
    			screenlist.add(point);
    		}
    	}
    	
    	int index = 0;
    	int size = screenlist.size();
    
    	//�޸�ÿ��С���target����
    	//����ֵȡ�Ժ�������ֵ�б�
		for(BallForCH b : ballList) {
			ScreenPoint point = screenlist.get(index);
    		
    		b.setTargetX(point.getX());
    		b.setTargetY(point.getY());
    		b.setReadyToForm(true);
//    		Log.i(TAG, " ready to stop " + b.getTargetX() + "," + b.getTargetY());
    		
    		index ++;
    		index = index % size;
		}
		
		
		//����һ����ʱ���� ����� �������� 
		stopFormChineseTimerTask(5000);
		
    }
    
    private void stopFormChineseTimerTask(long when) {
    	new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				formChineseComplete();
			}
		}, when);
    }
    
    private void formChineseComplete(){
    	if(myCompleteListener != null) {
			myCompleteListener.onComplete();
		}
    	for(BallForCH b:ballList)  
        {  
            b.setReadyToForm(false);//
        }
    }
//    
//    /**
//     * ����ı� �������
//     * �𵽴����������
//     * ��������Ϊfree״̬
//     */
//    public void random_update_ball_speed()
//    {
//    	screenlist=null;//�����
//    	
//        for(BallForCH b:ballList)  
//        {  
//        	float speedX=mRandom.nextFloat()>0.5? maxBallSpeed:-maxBallSpeed;
//            float speedY=mRandom.nextFloat()>0.5? maxBallSpeed:-maxBallSpeed;
//            
//            b.setReadyToForm(false);//
//        	
//        	b.setVX(speedX);
//        	b.setVY(speedY);
//        }
//    }
    

    public synchronized void clear()
    {
		isLoop=false;
    	ballList.clear();
    }
  
    private class CanvasThread extends Thread  
    {  
        @Override  
        public void run()  
        {  
            Canvas canvas=null;  
            while(isLoop)  
            { 
                try{  
                    canvas = myHolder.lockCanvas(null);//��ȡ����  
                    synchronized( myHolder )  
                    {  
                        updatePositions(); 
                        if(canvas!=null)
                        	drawGameElements(canvas);  
                        try {
            				Thread.sleep(5);
            			} catch (Exception e) {
            				// TODO: handle exception
            			}
                    }  
                }  
                finally  
                {  
                    if (canvas != null)   
                        myHolder.unlockCanvasAndPost(canvas);//�����������ύ���õ�ͼ��  
                } // end finally  
            }  
        }  
    }  
    
    /**
	 * �켣������¼�
	 */
	public interface OnCompleteListener {
		/**
		 * ������
		 */
		public void onComplete();
	}
	
	/**
	 * �ⲿ����ʹ��
	 * @param myCompleteListener
	 */
	public void setOnCompleteListener(OnCompleteListener myCompleteListener) {
		this.myCompleteListener = myCompleteListener;
	}
    
    
}  