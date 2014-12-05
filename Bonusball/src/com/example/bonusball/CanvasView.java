package com.example.bonusball;  
  
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

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
	/**
	 * �ж� С��ֹͣ�ǵ�ƫ����
	 */
	private final static int OFFSET=5;
	
	/**ʣ����ô������� �ж����Ѿ�����*/
	private final static int NAUGHTY_BALL_NUMBER=10;
	
	/**Ħ����*/
	private final static float FRICTION=0.96f;		// 
	
	private ArrayList<ScreenPoint> screenlist=null;
	public int number=0;
	public int totalNum=0;
	//�ⲿ���� ������ɵĽӿ�
	private OnCompleteListener myCompleteListener;
	
    private SurfaceHolder myHolder;  
    private Paint ballPaint; // Paint used to draw the cannonball  
    private int screenWidth; // width of the screen  
    private int screenHeight; // height of the screen  
    private int maxBallRadius=10;  
    private int maxBallSpeed=30;
    private CanvasThread myThread;  
    private List<Ball> ballList;  //����С��ļ���
    private Paint backgroundPaint;  
    private Random mRandom;  
    //����ѭ��  
    boolean isLoop;  
    
    int		mouseX,		mouseY;			// ��ǰ�������
    int		mouseVX,	mouseVY;		// ����ٶ�
    int		prevMouseX,	prevMouseY;		// �ϴ��������
    boolean	isMouseDown=false;				// �������Ƿ���
  
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
        ballList=new CopyOnWriteArrayList<Ball>();  
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
        
        ballList.add(new Ball(ranColor,tmpRadius,pX,pY,speedX,speedY));  
    }
    
    public void fireBall(float startX,float startY,float velocityX,float velocityY)  
    {  
    	//���������ɫ
        int ranColor = 0xff000000 | mRandom.nextInt(0x00ffffff);
        
        //��������뾶
        float randomRadius=mRandom.nextInt(maxBallRadius);  
        float tmpRadius=maxBallRadius/5.0>randomRadius?maxBallRadius:randomRadius;  
        
        ballList.add(new Ball(ranColor,tmpRadius,startX,startY,velocityX,velocityY));  
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
        for(Ball b:ballList)  
        {  
            ballPaint.setColor(b.getColor());  
            canvas.drawCircle(b.getX(),b.getY(),b.getRadius(),ballPaint);  
        }  
    }  
    
    
    
    /**
     * �ı��������λ��
     * ����ÿ����ǰ���ٶ�
     * ��������߽� ����
     * @param elapsedTimeMS
     */
    private void updatePositions(double elapsedTimeMS) {  
        // TODO Auto-generated method stub  
//        float interval = (float) (elapsedTimeMS / 1000.0);   
        float interval = (float) 0.1;  
        //�����д����ֹͣ�߳�
        //�����������򡣡����ٳ��ܲ���λ
        if(totalNum!=0&&number>totalNum-NAUGHTY_BALL_NUMBER)//ֹͣ�߳�
        {
//        	myThread.stop();
//        	isLoop=false;
        	myCompleteListener.onComplete();
        	number=0;
//        	screenlist.clear();
//        	screenlist=null;
        	random_update_ball_speed();//�������
        	
        }
        
        

		mouseVX    = mouseX - prevMouseX;
		mouseVY    = mouseY - prevMouseY;
		prevMouseX = mouseX;
		prevMouseY = mouseY;
		
		float toDist   = screenWidth * 0.86f;
		float stirDist = screenWidth * 0.125f;
		float blowDist = screenWidth * 0.5f;
        
        
        for(Ball b:ballList)  
        {  
        	

			float x  = b.getX();
			float y  = b.getY();
			float vX = b.getVX();
			float vY = b.getVY();
			
			float dX = x - mouseX;
			float dY = y - mouseY; 
			float d  = (float)Math.sqrt(dX * dX + dY * dY);
			dX = d>0 ? dX / d : 0;
			dY = d>0 ? dY / d : 0;
			
        	/*
        	 * ����screenlist
        	 * ���������Щ�㡣��ͣ����
        	 */
        	if(screenlist!=null)
        	{
        		for(ScreenPoint point:screenlist)
            	{
        			//�������ֵ�����ڶ�
        			if(!b.isStop()&&point.isFalg())
        			{//&&	b.getY()==point.getY()
        				
        				
        				if(Math.sqrt((b.getX()-point.getX()))<OFFSET&&
        						Math.sqrt((b.getY()-point.getY()))<OFFSET)
	            		{
        					point.setFalg(new Random().nextBoolean());//����������Ѿ����ˡ�����
	            			b.setStop(true);
	            			number++;
	            			break;
//	            			System.out.println(number+"  "+totalNum);
	            		}
        				
        				float d2=(float) Math.sqrt(vX * vX + vY * vY);
        				
        				if(d2>2*OFFSET)
        				{
        					//�ƶ�֮��������Ŀ�����ж�
//        					Log.i("CanvasView", "������");
        					if(Math.sqrt((x+vX/2-point.getX()))<OFFSET&&
            						Math.sqrt((y+vY/2-point.getY()))<OFFSET)
    	            		{
            					point.setFalg(new Random().nextBoolean());//����������Ѿ����ˡ�����
    	            			b.setStop(true);
    	            			number++;
    	            			break;
//    	            			System.out.println(number+"  "+totalNum);
    	            		}
        				}
        					
        					
        			}
            		
            	}
        	}
        	
        	
        
        	if(!b.isStop())//��ǰ����ֹͣ״̬
        	{
//        		b.setPosX(b.getX()+b.getVX()*interval);  
//                b.setPosY(b.getY()+b.getVY()*interval);  
//                
////                Log.i("Ball", b.getX()+","+b.getY());
//                
//                if (b.getX() + b.getRadius()> screenWidth )  
//                {  
//                    b.setVX(-1*b.getVX());  
//                    //�߽��޸�  
//                    b.setPosX(screenWidth-b.getRadius());  
//                }  
//                if(b.getX() - b.getRadius() < 0)  
//                {  
//                    b.setVX(-1*b.getVX());  
//                    b.setPosX(b.getRadius());  
//                }  
//                if (b.getY() + b.getRadius()> screenHeight)  
//                {  
//                    b.setVY(-1*b.getVY());  
//                    b.setPosY(screenHeight-b.getRadius());  
//                }  
//                if(b.getY() - b.getRadius() < 0)  
//                {  
//                    b.setVY(-1*b.getVY());  
//                    b.setPosY(b.getRadius());  
//                }
        		
//        		darken();	// ȫ���䰵

        		
        			
//        			//��갴�¼��� �����Ļ������
        			if (isMouseDown && d < blowDist)
        			{
        				float blowAcc = (1 - (d / blowDist)) * 14;
        				vX += dX * blowAcc + 0.5f - mRandom.nextFloat()/Integer.MAX_VALUE;
        				vY += dY * blowAcc + 0.5f - mRandom.nextFloat()/Integer.MAX_VALUE;
        			}
        			
        			if (d < toDist)
        			{
        				float toAcc = (1 - (d / toDist)) * screenWidth * 0.0014f;
        				vX -= dX * toAcc;
        				vY -= dY * toAcc;			
        			}
        			
        			if (d < stirDist)
        			{
        				float mAcc = (1 - (d / stirDist)) * screenWidth * 0.00026f;
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
        			
        			// ��С��
//        			setcolor(b.getColor());
//        			setfillstyle(b.getColor());
//        			fillcircle(int(nextX + 0.5), int(nextY + 0.5), int(sc + 0.5));
        		
        	}
        	
              
        }  
  
    }  
    
    public void formChinese(ArrayList<ScreenPoint> list)
    {
    	screenlist=new ArrayList<ScreenPoint>();
    	screenlist=list;
    	number=0;//��¼�ж��ٸ���ͣ������
    	
    	
    	
    	totalNum=ballList.size();//�������
    }
    
    /**
     * �ı�С��ǰ���ٶȷ���
     * ����Ŀ��ص�
     * �������ǰ���ٶȷ���
     * @param x
     * @param y
     */
    public void update_ball_speed(float x2,float y2)
    {

        for(Ball b:ballList)  
        {  
    		float x1=b.getX();
        	float y1=b.getY();

    		b.setVX((x2-x1)/2);
    		b.setVY((y2-y1)/2);
        }
    }
    
    /**
     * ����ı�
     * @param x
     * @param y
     */
    public void random_update_ball_speed()
    {
    	screenlist=null;//�����
    	
        for(Ball b:ballList)  
        {  
        	
        	float speedX=mRandom.nextFloat()>0.5? maxBallSpeed:-maxBallSpeed;
            float speedY=mRandom.nextFloat()>0.5? maxBallSpeed:-maxBallSpeed;
            
            b.setStop(false);//ֹͣ����Ҳ��ʼ�ƶ�
        	
        	b.setVX(speedX);
        	b.setVY(speedY);
//        	
    
        }
    }
    
    /**
     * ����ڴ�
     */
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
            long previousFrameTime = System.currentTimeMillis();   
            while(isLoop)  
            { 
                  
                try{  
                    canvas = myHolder.lockCanvas(null);//��ȡ����  
                    synchronized( myHolder )  
                    {  
//                        canvas.drawColor(Color.BLACK);  
//                        long currentTime = System.currentTimeMillis();  
//                        double elapsedTimeMS = currentTime - previousFrameTime;  
                        updatePositions(0); // update game state  
                        if(canvas!=null)
                        	drawGameElements(canvas);  
//                        previousFrameTime = currentTime; // update previous time  
                        //System.out.println("run");  
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
//                
                
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