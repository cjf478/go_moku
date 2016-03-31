package go_moku;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.math.*;

public class game{
	
	public static DrawCanvas dc;
	
	public static void main(String args[])
	{
		JFrame frame=new JFrame("NightCat");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		dc = new DrawCanvas();
		dc.addMouseListener(new MouseClickHandler());
		frame.add(dc);
		
		Menu menu1=new Menu("游戏");
		MenuItem item1=new MenuItem("新游戏");
		MenuItem item2=new MenuItem("悔棋");
		MenuItem item3=new MenuItem("退出");
		MenuItem item4=new MenuItem("查看帮助");
		MenuItem item5=new MenuItem("关于");			
		MenuItem item6=new MenuItem("玩家先手");
		MenuItem item7=new MenuItem("电脑先手");
		
		item1.addActionListener(new ActionListener(){	//新游戏

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.add(dc);
				Chess.init();
			}
		});
		item2.addActionListener(new ActionListener(){	//悔棋

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Graphics g=dc.getGraphics();
				g.setColor(Color.green.darker());
				g.fillRect(30*(Chess.x[Chess.n-1])-10, 30*(Chess.y[Chess.n-1])-10, 20, 20);
				g.setColor(Color.black);
				g.drawLine(30*(Chess.x[Chess.n-1]),30*(Chess.y[Chess.n-1])-10,30*(Chess.x[Chess.n-1]),30*(Chess.y[Chess.n-1])+10);
				g.drawLine(30*(Chess.x[Chess.n-1])-10,30*(Chess.y[Chess.n-1]),30*(Chess.x[Chess.n-1])+10,30*(Chess.y[Chess.n-1]));
				
				g.setColor(Color.green.darker());
				g.fillRect(30*(Chess.x[Chess.n-2])-10, 30*(Chess.y[Chess.n-2])-10, 20, 20);
				g.setColor(Color.black);
				g.drawLine(30*(Chess.x[Chess.n-2]),30*(Chess.y[Chess.n-2])-10,30*(Chess.x[Chess.n-2]),30*(Chess.y[Chess.n-2])+10);
				g.drawLine(30*(Chess.x[Chess.n-2])-10,30*(Chess.y[Chess.n-2]),30*(Chess.x[Chess.n-2])+10,30*(Chess.y[Chess.n-2]));
				
				Chess.c[Chess.x[Chess.n-1]-1][Chess.y[Chess.n-1]-1]=0;
				Chess.c[Chess.x[Chess.n-2]-1][Chess.y[Chess.n-2]-1]=0;
				Chess.n-=2;	
			}
			
		});	
		item3.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				
			}
			
		});
		item4.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (Chess.huo4()){
					System.out.println("yes");
				}
			}
			
		});   //查看帮助
		item5.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		item6.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				item6.disable();
				item7.enable();
			}
			
		});
		item7.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				item7.disable();
				item6.enable();
			}
			
		});
		
		menu1.add(item1);
		menu1.add(item2);
		menu1.add(item3);
		
		Menu menu2=new Menu("设置");
		menu2.add(item6);
		menu2.add(item7);
		item6.disable();
		
		Menu menu3=new Menu("帮助");
		menu3.add(item4);
		menu3.add(item5);
		
		MenuBar menubar=new MenuBar();
		menubar.add(menu1);
		menubar.add(menu2);
		menubar.add(menu3);
		frame.setMenuBar(menubar);
		frame.setSize(480,520);
		frame.setLocation(400, 100);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}

class Node{
	public int x;
	public int y;
	public int v;
	public Node next;
	public Node(int x0,int y0,int v0)
	{
		x=x0;
		y=y0;
		v=v0;
	}
}

class heap{
	private Node[] Heap;
	private int n;
	private int hash[];
	
	private void swap(int i,int j){
		Node tmp=Heap[i];
		Heap[i]=Heap[j];
		Heap[j]=tmp;
	}
	
	private void shiftdown(int pos){
		while (!isLeaf(pos)){
			int j=leftchild(pos);
			int rc=rightchild(pos);
			if ((rc<n) && Heap[rc].v>Heap[j].v)
				j=rc;
			if (Heap[pos].v>Heap[j].v)
				return;
			swap(pos,j);
			pos=j;
		}
	}
	
	public heap(){
		Heap=new Node [225];
		n=0;
		hash=new int [225];
		for (int i=0;i<225;++i)
		{
			hash[i]=0;
		}
	}
	
	public void clear()
	{
		for (int i=0;i<225;++i)
		{
			hash[i]=0;
		}
		n=0;
	}
	
	public int size(){
		return n;
	}
	
	public boolean isLeaf(int pos){
		return (pos>=n/2) && (pos<n); 
	}
	
	public int leftchild(int pos){
		return 2*pos+1;
	}
	
	public int rightchild(int pos){
		return 2*pos+2;
	}
	
	public int parent(int pos){
		return (pos-1)/2;
	}
	
	public void buildHeap()
	{
		for (int i=n/2-1;i>=0;i--)
			shiftdown(i);
	}
	
	public void insert(Node it){
		if (hash[it.x*15+it.y]==0)
		{
			int curr=n++;
			Heap[curr]=it;
			while ((curr!=0) && Heap[curr].v>Heap[parent(curr)].v){
				swap(curr,parent(curr));
				curr=parent(curr);
			}
			hash[it.x*15+it.y]=1;
		}
	}
	
	public Node removefirst(){
		swap(0,--n);
		if (n!=0)
			shiftdown(0);
		hash[Heap[n].x*15+Heap[n].y]=0;
		return Heap[n];
	}
	
	public Node remove(int pos){
		if (pos==(n-1))
			n--;
		else
		{
			swap(pos,--n);
			while ((pos!=0) && Heap[pos].v>Heap[parent(pos)].v){
				swap(pos,parent(pos));
				pos=parent(pos);
			}
			if (n!=0)
				shiftdown(pos);
		}
		hash[Heap[n].x*15+Heap[n].y]=0;
		return Heap[n];
	}
}

class Chess{
	static int c[][]=new int [15][15]; //0 空 1黑 玩家 2白 电脑
	static int side=0; //0 玩家 1电脑
	static int x[]=new int [225];
	static int y[]=new int [225];
	static int n=0;
	final static int INFINITY=1000000;
	
	static{
		for (int i=0;i<15;++i)
		{
			for (int j=0;j<15;++j)
			{
				c[i][j]=0;
			}
		}
	}
	public static void init()
	{
		side=0;
		for (int i=0;i<15;++i)
		{
			for (int j=0;j<15;++j)
			{
				c[i][j]=0;
			}
		}
		n=0;
	}
	
	public static void generateMove(heap h)
	{
		h.clear();
		//各种情况罗列
		
		for (int i=0;i<15;++i)
		{
			for (int j=0;j<15;++j)
			{
				if (c[i][j]==0)
				{
					c[i][j]=side+1;
					
					if (cheng5())
					{
						h.insert(new Node(i,j,2000));
						c[i][j]=0;
						continue;
					}
					
					side=1-side;
					c[i][j]=1+side;
					if (cheng5())
					{
						h.insert(new Node(i,j,1900));
						c[i][j]=0;
						continue;
					}
					side=1-side;
					c[i][j]=1+side;
					
					if (huo4() || shuangsi4() || si4huo3())
					{
						h.insert(new Node(i,j,1800));
						
						c[i][j]=0;
						continue;
					}
					
					side=1-side;
					c[i][j]=1+side;
					if (huo4() || shuangsi4() || si4huo3())
					{
						h.insert(new Node(i,j,1700));
						c[i][j]=0;
						continue;
					}
					side=1-side;
					c[i][j]=1+side;
					
					if (shuanghuo3())
					{
						h.insert(new Node(i,j,1600));
						c[i][j]=0;
						continue;
					}
					
					side=1-side;
					c[i][j]=1+side;
					if (shuanghuo3())
					{
						h.insert(new Node(i,j,1500));
						c[i][j]=0;
						continue;
					}
					side=1-side;
					c[i][j]=1+side;
					
					if (si3huo3())
					{
						h.insert(new Node(i,j,1400));
						c[i][j]=0;
						continue;
					}
					
					side=1-side;
					c[i][j]=1+side;
					if (si3huo3())
					{
						h.insert(new Node(i,j,1300));
						c[i][j]=0;
						continue;
					}
					side=1-side;
					c[i][j]=1+side;
					
					if (si4())
					{
						h.insert(new Node(i,j,1200));
						c[i][j]=0;
						continue;
					}
					
					side=1-side;
					c[i][j]=1+side;
					if (si4())
					{
						h.insert(new Node(i,j,1100));
						c[i][j]=0;
						continue;
					}
					side=1-side;
					c[i][j]=1+side;
					
					if (huo3())
					{
						h.insert(new Node(i,j,1000));
						c[i][j]=0;
						continue;
					}
					
					side=1-side;
					c[i][j]=1+side;
					if (huo3())
					{
						h.insert(new Node(i,j,900));
						c[i][j]=0;
						continue;
					}
					side=1-side;
					c[i][j]=1+side;	
					
					if (shuanghuo2())
					{
						h.insert(new Node(i,j,800));
						c[i][j]=0;
						continue;
					}
					
					side=1-side;
					c[i][j]=1+side;
					if (shuanghuo2())
					{
						h.insert(new Node(i,j,700));
						c[i][j]=0;
						continue;
					}
					side=1-side;
					c[i][j]=1+side;
					
					if (si3())
					{
						h.insert(new Node(i,j,600));
						c[i][j]=0;
						continue;
					}
					
					side=1-side;
					c[i][j]=1+side;
					if (si3())
					{
						h.insert(new Node(i,j,500));
						c[i][j]=0;
						continue;
					}
					side=1-side;
					c[i][j]=1+side;
					
					if (huo2())
					{
						h.insert(new Node(i,j,400));
						c[i][j]=0;
						continue;
					}
					
					side=1-side;
					c[i][j]=1+side;
					if (huo2())
					{
						h.insert(new Node(i,j,300));
						c[i][j]=0;
						continue;
					}
					side=1-side;
					c[i][j]=1+side;
					
					if (si2())
					{
						h.insert(new Node(i,j,200));
						c[i][j]=0;
						continue;
					}
					
					side=1-side;
					c[i][j]=1+side;
					if (si2())
					{
						h.insert(new Node(i,j,100));
						c[i][j]=0;
						continue;
					}
					side=1-side;
					
					c[i][j]=0;
					if (n>=2)	
						h.insert(new Node(i,j,0-Math.max(Math.abs(i-x[n-2]),Math.abs(j-y[n-2]))));
					else
						h.insert(new Node(i,j,0));
				}
			}
		}
	}
	public static boolean cheng5()
	{
		for (int i=0;i<15;++i) //横
		{
			int count=0;
			for (int j=0;j<15;++j)
			{
				if (c[i][j]==side+1)
				{
					count++;
					if (count==5)
						return true;
				}
				else
					count=0;
			}
		}
		for (int j=0;j<15;++j)	//竖
		{
			int count=0;
			for (int i=0;i<15;++i)
			{
				if (c[i][j]==side+1)
				{
					count++;
					if (count==5)
						return true;
				}
				else
					count=0;
			}
		}
		for (int i=4;i<15;++i)
		{
			int count=0;
			for (int j=0;j<i+1;++j)
			{
				if (c[i-j][j]==side+1)
				{
					count++;
					if (count==5)
						return true;
				}
				else
					count=0;
			}
		}
		for (int i=10;i>0;--i)
		{
			int count=0;
			for (int j=0;j<15-i;++j)
			{
				if (c[14-j][i+j]==side+1){
					count++;
					if (count==5)
						return true;
				}
				else
					count=0;
			}
		}
		for (int i=0;i<11;++i)
		{
			int count=0;
			for (int j=0;j<15-i;++j)
			{
				if (c[j][i+j]==side+1){
					count++;
					if (count==5)
						return true;
				}
				else
					count=0;
			}
		}
		for (int i=1;i<11;++i)
		{
			int count=0;
			for (int j=0;j<15-i;++j)
			{
				if (c[i+j][j]==side+1){
					count++;
					if (count==5)
						return true;
				}
				else
					count=0;
			}
		}	
		return false;
	}
	
	public static boolean huo4()
	{
		for (int i=0;i<15;++i)
		{
			for (int j=0;j<15;++j){
				if (c[i][j]==side+1){
					if (i-1>=0 && i+4<15)
					{
						if (c[i-1][j]==0 &&
								c[i+1][j]==side+1 &&
								c[i+2][j]==side+1 &&
								c[i+3][j]==side+1 &&
								c[i+4][j]==0)
						{
							return true;
						}
					}
					if (j-1>=0 && j+4<15){
						if (c[i][j-1]==0 &&
								c[i][j+1]==side+1 &&
								c[i][j+2]==side+1 &&
								c[i][j+3]==side+1 &&
								c[i][j+4]==0)
						{
							return true;
						}
					}
					if (i-1>=0 && j-1>=0 && i+4<15 && j+4<15){
						if (c[i-1][j-1]==0 &&
								c[i+1][j+1]==side+1 &&
								c[i+2][j+2]==side+1 &&
								c[i+3][j+3]==side+1 &&
								c[i+4][j+4]==0)
						{
							return true;
						}
					}
					if (i-1>=0 && j+1<15 && i+4<15 && j-4>=0){
						if (c[i-1][j+1]==0 &&
								c[i+1][j-1]==side+1 &&
								c[i+2][j-2]==side+1 &&
								c[i+3][j-3]==side+1 &&
								c[i+4][j-4]==0)
						{
							return true;
						}
					}
					
					if (i+1<15 && i-4>=0)
					{
						if (c[i+1][j]==0 &&
								c[i-1][j]==side+1 &&
								c[i-2][j]==side+1 &&
								c[i-3][j]==side+1 &&
								c[i-4][j]==0)
						{
							return true;
						}
					}
					if (j+1<15 && j-4>=0){
						if (c[i][j+1]==0 &&
								c[i][j-1]==side+1 &&
								c[i][j-2]==side+1 &&
								c[i][j-3]==side+1 &&
								c[i][j-4]==0)
						{
							return true;
						}
					}
					if (i+1<15 && j+1<15 && i-4>=0 && j-4>=0){
						if (c[i+1][j+1]==0 &&
								c[i-1][j-1]==side+1 &&
								c[i-2][j-2]==side+1 &&
								c[i-3][j-3]==side+1 &&
								c[i-4][j-4]==0)
						{
							return true;
						}
					}
					if (i+1<15 && j-1>=0 && i-4>=0 && j+4<15){
						if (c[i+1][j-1]==0 &&
								c[i-1][j+1]==side+1 &&
								c[i-2][j+2]==side+1 &&
								c[i-3][j+3]==side+1 &&
								c[i-4][j+4]==0)
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	public static int count_si4()
	{
		int count=0;
		for (int i=0;i<15;++i)
		{
			for (int j=0;j<15;++j){
				if (c[i][j]==0){
					if (i+5<15){
						if (c[i+1][j]==side+1 &&
								c[i+2][j]==side+1 &&
								c[i+3][j]==side+1 &&
								c[i+4][j]==side+1 && 
								c[i+5][j]==2-side)
						{
							count++;
							continue;
						}
					}
					if (i-5>=0){
						if (c[i-1][j]==side+1 &&
								c[i-2][j]==side+1 &&
								c[i-3][j]==side+1 &&
								c[i-4][j]==side+1 &&
								c[i-5][j]==2-side)
						{
							count++;
							continue;
						}
					}
					if (j+5<15){
						if (c[i][j+1]==side+1 &&
								c[i][j+2]==side+1 &&
								c[i][j+3]==side+1 &&
								c[i][j+4]==side+1 &&
								c[i][j+5]==2-side)
						{
							count++;
							continue;
						}
					}
					if (j-5>=0){
						if (c[i][j-1]==side+1 &&
								c[i][j-2]==side+1 &&
								c[i][j-3]==side+1 &&
								c[i][j-4]==side+1 &&
								c[i][j-5]==2-side)
						{
							count++;
							continue;
						}
					}
					if (i+5<15 && j+5<15){
						if (c[i+1][j+1]==side+1 &&
								c[i+2][j+2]==side+1 &&
								c[i+3][j+3]==side+1 &&
								c[i+4][j+4]==side+1 &&
								c[i+5][j+5]==2-side
								)
						{
							count++;
							continue;
						}
					}
					if (i-5>=0 && j-5>=0){
						if (c[i-1][j-1]==side+1 &&
								c[i-2][j-2]==side+1 &&
								c[i-3][j-3]==side+1 &&
								c[i-4][j-4]==side+1 &&
								c[i-5][j-5]==2-side
								)
						{
							count++;
							continue;
						}
					}
					if (i-5>=0 && j+5<15){
						if (c[i-1][j+1]==side+1 &&
								c[i-2][j+2]==side+1 &&
								c[i-3][j+3]==side+1 &&
								c[i-4][j+4]==side+1 &&
								c[i-5][j+5]==2-side
								)
						{
							count++;
							continue;
						}
					}
					if (i+5<15 && j-5>=0){
						if (c[i+1][j-1]==side+1 &&
								c[i+2][j-2]==side+1 &&
								c[i+3][j-3]==side+1 &&
								c[i+4][j-4]==side+1 &&
								c[i+5][j-5]==2-side
								)
						{
							count++;
							continue;
						}
					}
					//1+3 3+1 2+2补充
					if (j-1>=0 && j+3<15)
					{
						if (c[i][j-1]==side+1 &&
								c[i][j+1]==side+1 &&
								c[i][j+2]==side+1 &&
								c[i][j+3]==side+1
								)
						{
							count++;
							continue;
						}
					}
					if (i-1>=0 && i+3<15)
					{
						if (c[i-1][j]==side+1 &&
								c[i+1][j]==side+1 &&
								c[i+2][j]==side+1 &&
								c[i+3][j]==side+1)
						{
							count++;
							continue;
						}
					}
					if (i-1>=0 && j-1>=0 && i+3<15 && j+3<15)
					{
						if (c[i-1][j-1]==side+1 &&
								c[i+1][j+1]==side+1 &&
								c[i+2][j+2]==side+1 &&
								c[i+3][j+3]==side+1
								)
						{
							count++;
							continue;
						}
					}
					if (i-1>=0 && i+3<15 && j+1<15 && j-3>=0)
					{
						if (c[i-1][j+1]==side+1 &&
								c[i+1][j-1]==side+1 &&
								c[i+2][j-2]==side+1 &&
								c[i+3][j-3]==side+1
								)
						{
							count++;
							continue;
						}
					}
					
					
					if (i-3>=0 && i+1<15)
					{
						if (c[i-3][j]==side+1 &&
								c[i-2][j]==side+1 &&
								c[i-1][j]==side+1 &&
								c[i+1][j]==side+1
								)
						{
							count++;
							continue;
						}
					}
					if (j-3>=0 && j+1<15)
					{
						if (c[i][j-3]==side+1 && 
								c[i][j-2]==side+1 &&
								c[i][j-1]==side+1 &&
								c[i][j+1]==side+1
										)
						{
							count++;
							continue;
						}
					}
					if (i-3>=0 && j-3>=0 && i+1<15 && j+1<15)
					{
						if (c[i-3][j-3]==side+1 &&
								c[i-2][j-2]==side+1 &&
								c[i-1][j-1]==side+1 &&
								c[i+1][j+1]==side+1
								)
						{
							count++;
							continue;
						}
					}
					if (i-3>=0 && i+1<15 && j+3<15 && j-1>=0)
					{
						if (c[i-3][j+3]==side+1 &&
								c[i-2][j+2]==side+1 &&
								c[i-1][j+1]==side+1 &&
								c[i+1][j-1]==side+1
								)
						{
							count++;
							continue;
						}
					}
					
					
					if (i-2>=0 && i+2<15)
					{
						if (c[i-2][j]==side+1 &&
								c[i-1][j]==side+1 &&
								c[i+1][j]==side+1 &&
								c[i+2][j]==side+1
								)
						{
							count++;
							continue;
						}
					}
					if (j-2>=0 && j+2<15)
					{
						if (c[i][j-2]==side+1 && 
								c[i][j-1]==side+1 &&
								c[i][j+1]==side+1 &&
								c[i][j+2]==side+1
										)
						{
							count++;
							continue;
						}
					}
					if (i-2>=0 && j-2>=0 && i+2<15 && j+2<15)
					{
						if (c[i-2][j-2]==side+1 &&
								c[i-1][j-1]==side+1 &&
								c[i+1][j+1]==side+1 &&
								c[i+2][j+2]==side+1
								)
						{
							count++;
							continue;
						}
					}
					if (i-2>=0 && i+2<15 && j+2<15 && j-2>=0)
					{
						if (c[i-2][j+2]==side+1 &&
								c[i-1][j+1]==side+1 &&
								c[i+1][j-1]==side+1 &&
								c[i+2][j-2]==side+1
								)
						{
							count++;
							continue;
						}
					}
				}
			}
		}
		return count;
	}
	public static int count_huo3(){
		int count=0;
		for (int i=0;i<15;++i)
		{
			for (int j=0;j<15;++j)
			{
				if (c[i][j]==side+1)
				{
					if (i-3>=0 && i+3<15)
					{
						if ((c[i-3][j]==0 || c[i+3][j]==0) &&
								c[i-2][j]==0 &&
								c[i-1][j]==side+1 &&
								c[i+1][j]==side+1 &&
								c[i+2][j]==0
								)
						{
							count++;
						}
					}
					if (j-3>=0 && j+3<15)
					{
						if ((c[i][j-3]==0 || c[i][j+3]==0) &&
								c[i][j-2]==0 &&
								c[i][j-1]==side+1 &&
								c[i][j+1]==side+1 &&
								c[i][j+2]==0
								)
						{
							count++;
						}
					}
					if (i-3>=0 && j-3>=0 && i+3<15 && j+3<15)
					{
						if ((c[i-3][j-3]==0 || c[i+3][j+3]==0) &&
								c[i-2][j-2]==0 &&
								c[i-1][j-1]==side+1 &&
								c[i+1][j+1]==side+1 &&
								c[i+2][j+2]==0
								)
						{
							count++;
						}
					}
					if (i-3>=0 && j+3<15 && i+3<15 && j-3>=0)
					{
						if ((c[i-3][j+3]==0 || c[i+3][j-3]==0) &&
								c[i-2][j+2]==0 &&
								c[i-1][j+1]==side+1 &&
								c[i+1][j-1]==side+1 &&
								c[i+2][j-2]==0
								)
						{
							count++;
						}
					}
					
					//1+2 2+1
					if (i-1>=0 && i+4<15)
					{
						if (c[i-1][j]==0 &&
								c[i+1][j]==0 &&
								c[i+2][j]==side+1 &&
								c[i+3][j]==side+1 &&
								c[i+4][j]==0
								)
						{
							count++;
						}
					}
					if (j-1>=0 && j+4<15)
					{
						if (c[i][j-1]==0 &&
								c[i][j+1]==0 &&
								c[i][j+2]==side+1 &&
								c[i][j+3]==side+1 &&
								c[i][j+4]==0
								)
						{
							count++;
						}
					}
					if (i-1>=0 && j-1>=0 && i+4<15 && j+4<15)
					{
						if (c[i-1][j-1]==0 &&
								c[i+1][j+1]==0 &&
								c[i+2][j+2]==side+1 &&
								c[i+3][j+3]==side+1 &&
								c[i+4][j+4]==0
								)
						{
							count++;
						}
					}
					if (i-1>=0 && i+4<15 && j+1<15 && j-4>=0)
					{
						if (c[i-1][j+1]==0 &&
								c[i+1][j-1]==0 &&
								c[i+2][j-2]==side+1 &&
								c[i+3][j-3]==side+1 &&
								c[i+4][j-4]==0
								)
						{
							count++;
						}
					}
					
					if (i+1<15 && i-4>=0)
					{
						if (c[i+1][j]==0 &&
								c[i-1][j]==0 &&
								c[i-2][j]==side+1 &&
								c[i-3][j]==side+1 &&
								c[i-4][j]==0
								)
						{
							count++;
						}
					}
					if (j+1<15 && j-4>=0)
					{
						if (c[i][j+1]==0 &&
								c[i][j-1]==0 &&
								c[i][j-2]==side+1 &&
								c[i][j-3]==side+1 &&
								c[i][j-4]==0
								)
						{
							count++;
						}
					}
					if (i+1<15 && j+1<15 && i-4>=0 && j-4>=0)
					{
						if (c[i+1][j+1]==0 &&
								c[i-1][j-1]==0 &&
								c[i-2][j-2]==side+1 &&
								c[i-3][j-3]==side+1 &&
								c[i-4][j-4]==0
								)
						{
							count++;
						}
					}
					if (i+1<15 && i-4>=0 && j-1>=0 && j+4<15)
					{
						if (c[i+1][j-1]==0 &&
								c[i-1][j+1]==0 &&
								c[i-2][j+2]==side+1 &&
								c[i-3][j+3]==side+1 &&
								c[i-4][j+4]==0
								)
						{
							count++;
						}
					}
				
				}
			}
		}
		return count;
	}
	
	
	public static boolean shuangsi4(){
		if (count_si4()>=2)
			return true;
		else
			return false;
	}
	
	
	public static boolean si4huo3()
	{
		if (count_si4()>=1 && count_huo3()>=1)
			return true;
		else
			return false;
	}
	
	public static boolean shuanghuo3()
	{
		if (count_huo3()>=2)
			return true;
		else
			return false;
	}
	
	public static boolean si3(){
		for (int i=0;i<15;++i)
		{
			for (int j=0;j<15;++j)
			{
				if (c[i][j]==0){
					if (i-1>=0 && i+4<15){
						if (	c[i-1][j]==0 &&
								c[i+1][j]==side+1 &&
								c[i+2][j]==side+1 &&
								c[i+3][j]==side+1 &&
								c[i+4][j]==2-side)
							return true;
					}
					if (j-1>=0 && j+4<15){
						if (
								c[i][j-1]==0 &&
								c[i][j+1]==side+1 &&
								c[i][j+2]==side+1 &&
								c[i][j+3]==side+1 &&
								c[i][j+4]==2-side
								)
							return true;
					}
					if (i-1>=0 && j-1>=0 && i+4<15 && j+4<15){
						if (
								c[i-1][j-1]==0 &&
								c[i+1][j+1]==side+1 &&
								c[i+2][j+2]==side+1 &&
								c[i+3][j+3]==side+1 &&
								c[i+4][j+4]==2-side
								)
							return true;
					}
					if (i-1>=0 && j+1<15 && i+4<15 && j-4>=0)
					{
						if (
								c[i-1][j+1]==0 &&
								c[i+1][j-1]==side+1 &&
								c[i+2][j-2]==side+1 &&
								c[i+3][j-3]==side+1 &&
								c[i+4][j-4]==2-side
								)
							return true;
					}
					
					if (i-4>=0 && i+1<15){
						if (	c[i+1][j]==0 &&
								c[i-1][j]==side+1 &&
								c[i-2][j]==side+1 &&
								c[i-3][j]==side+1 &&
								c[i-4][j]==2-side)
							return true;
					}
					if (j+1<15 && j-4>=0){
						if (
								c[i][j+1]==0 &&
								c[i][j-1]==side+1 &&
								c[i][j-2]==side+1 &&
								c[i][j-3]==side+1 &&
								c[i][j-4]==2-side
								)
							return true;
					}
					if (i+1<15 && j+1<15 && i-4>=0 && j-4>=0){
						if (
								c[i+1][j+1]==0 &&
								c[i-1][j-1]==side+1 &&
								c[i-2][j-2]==side+1 &&
								c[i-3][j-3]==side+1 &&
								c[i-4][j-4]==2-side
								)
							return true;
					}
					if (i+1<15 && j-1>=0 && i-4>=0 && j+4<15)
					{
						if (
								c[i+1][j-1]==0 &&
								c[i-1][j+1]==side+1 &&
								c[i-2][j+2]==side+1 &&
								c[i-3][j+3]==side+1 &&
								c[i-4][j+4]==2-side
								)
							return true;
					}
					
					if (i-2>=0 && i+3<15)
					{
						if (
								c[i-1][j]==side+1 &&
								c[i+1][j]==side+1 &&
								c[i+2][j]==side+1 &&
								((c[i-2][j]==0 && c[i+3][j]==2-side) ||
								(c[i-2][j]==2-side && c[i+3][j]==0))
								)
							return true;
					}
					if (j-2>=0 && j+3<15)
					{
						if (
								c[i][j-1]==side+1 &&
								c[i][j+1]==side+1 &&
								c[i][j+2]==side+1 &&
								((c[i][j-2]==0 && c[i][j+3]==2-side) ||
								(c[i][j-2]==2-side && c[i][j+3]==0)
								)
								)
								return true;
					}
					if (j-2>=0 && j+3<15 && i-2>=0 && i+3<15)
					{
						if (
								c[i-1][j-1]==side+1 &&
								c[i+1][j+1]==side+1 &&
								c[i+2][j+2]==side+1 &&
								((c[i-2][j-2]==0 && c[i+3][j+3]==2-side) ||
								(c[i-2][j-2]==2-side && c[i+3][j+3]==0)
								)
								)
								return true;
					}
					if (j-3>=0 && j+2<15 && i-2>=0 && i+3<15)
					{
						if (
								c[i-1][j+1]==side+1 &&
								c[i+1][j-1]==side+1 &&
								c[i+2][j-2]==side+1 &&
								((c[i-2][j+2]==0 && c[i+3][j-3]==2-side) ||
								(c[i-2][j+2]==2-side && c[i+3][j-3]==0)
								)
								)
								return true;
					}
					
					if (i-3>=0 && i+2<15)
					{
						if (
								c[i-2][j]==side+1 &&
								c[i-1][j]==side+1 &&
								c[i+1][j]==side+1 &&
								((c[i-3][j]==0 && c[i+2][j]==2-side) ||
								(c[i-3][j]==2-side && c[i+2][j]==0))
								)
							return true;
					}
					if (j-3>=0 && j+2<15)
					{
						if (
								c[i][j-2]==side+1 &&
								c[i][j-1]==side+1 &&
								c[i][j+1]==side+1 &&
								((c[i][j-3]==0 && c[i][j+2]==2-side) ||
								(c[i][j-3]==2-side && c[i][j+2]==0)
								)
								)
								return true;
					}
					if (j-3>=0 && j+2<15 && i-3>=0 && i+2<15)
					{
						if (
								c[i-2][j-2]==side+1 &&
								c[i-1][j-1]==side+1 &&
								c[i+1][j+1]==side+1 &&
								((c[i-3][j-3]==0 && c[i+2][j+2]==2-side) ||
								(c[i-3][j-3]==2-side && c[i+2][j+2]==0)
								)
								)
								return true;
					}
					if (j-2>=0 && j+3<15 && i-3>=0 && i+2<15)
					{
						if (
								c[i-2][j+2]==side+1 &&
								c[i-1][j+1]==side+1 &&
								c[i+1][j-1]==side+1 &&
								((c[i-3][j+3]==0 && c[i+2][j-2]==2-side) ||
								(c[i-3][j+3]==2-side && c[i+2][j-2]==0)
								)
								)
								return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean si3huo3()
	{
		if (count_huo3()>=1 && si3())
		{
			return true;
		}
		else
			return false;
	}
	
	public static boolean si4(){
		if (count_si4()>=1)
			return true;
		else
			return false;
	}
	
	public static boolean huo3()
	{
		if (count_huo3()>=1)
			return true;
		else
			return false;
	}
	
	public static boolean shuanghuo2()
	{
		for (int i=0;i<15;++i)
		{
			for (int j=0;j<15;++j)
			{
				if (c[i][j]==0)
				{
					c[i][j]=side+1;
					if (shuanghuo3())
					{
						c[i][j]=0;
						return true;
					}
					else
						c[i][j]=0;
				}	
			}
		}
		return false;
	}
	
	public static boolean huo2(){	//should improve
		for (int i=0;i<15;++i)
		{
			for (int j=0;j<15;++j)
			{
				if (c[i][j]==0)
				{
					if (i+3<15)
					{
						if (c[i+1][j]==side+1 && c[i+2][j]==side+1 && c[i+3][j]==0)
							return true;
					}
					if (j+3<15)
					{
						if (c[i][j+1]==side+1 && c[i][j+2]==side+1 && c[i][j+3]==0)
							return true;
					}
					if (i+3<15 && j+3<15)
					{
						if (c[i+1][j+1]==side+1 && c[i+2][j+2]==side+1 && c[i+3][j+3]==0)
							return true;
					}
					if (i+3<15 && j-3>=0)
					{
						if (c[i+1][j-1]==side+1 && c[i+2][j-2]==side+1 && c[i+3][j-3]==0)
							return true;
					}
					
					if (i-3>=0)
					{
						if (c[i-1][j]==side+1 && c[i-2][j]==side+1 && c[i-3][j]==0)
							return true;
					}
					if (j-3>=0)
					{
						if (c[i][j-1]==side+1 && c[i][j-2]==side+1 && c[i][j-3]==0)
							return true;
					}
					if (i-3>=0 && j-3>=0)
					{
						if (c[i-1][j-1]==side+1 && c[i-2][j-2]==side+1 && c[i-3][j-3]==0)
							return true;
					}
					if (i-3>=0 && j+3<15)
					{
						if (c[i-1][j+1]==side+1 && c[i-2][j+2]==side+1 && c[i-3][j+3]==0)
							return true;
					}
				}	
			}
		}
		return false;
	}
	
	public static boolean si2()	//should improve
	{
		for (int i=0;i<15;++i)
		{
			for (int j=0;j<15;++j)
			{
				if (c[i][j]==0)
				{
					if (i+3<15)
					{
						if (c[i+1][j]==side+1 && c[i+2][j]==side+1 && c[i+3][j]==2-side)
							return true;
					}
					if (j+3<15)
					{
						if (c[i][j+1]==side+1 && c[i][j+2]==side+1 && c[i][j+3]==2-side)
							return true;
					}
					if (i+3<15 && j+3<15)
					{
						if (c[i+1][j+1]==side+1 && c[i+2][j+2]==side+1 && c[i+3][j+3]==2-side)
							return true;
					}
					if (i+3<15 && j-3>=0)
					{
						if (c[i+1][j-1]==side+1 && c[i+2][j-2]==side+1 && c[i+3][j-3]==2-side)
							return true;
					}
					
					if (i-3>=0)
					{
						if (c[i-1][j]==side+1 && c[i-2][j]==side+1 && c[i-3][j]==2-side)
							return true;
					}
					if (j-3>=0)
					{
						if (c[i][j-1]==side+1 && c[i][j-2]==side+1 && c[i][j-3]==2-side)
							return true;
					}
					if (i-3>=0 && j-3>=0)
					{
						if (c[i-1][j-1]==side+1 && c[i-2][j-2]==side+1 && c[i-3][j-3]==2-side)
							return true;
					}
					if (i-3>=0 && j+3<15)
					{
						if (c[i-1][j+1]==side+1 && c[i-2][j+2]==side+1 && c[i-3][j+3]==2-side)
							return true;
					}
				}	
			}
		}
		return false;
	}
	
	public static int result()	//0 未决 1玩家赢 2电脑赢 3平局
	{
		for (int i=0;i<15;++i) //横
		{
			int black_count=0;
			int white_count=0;
			for (int j=0;j<15;++j)
			{
				if (c[i][j]==1)
				{
					black_count++;
					if (black_count==5)
						return 1;
				}
				else
					black_count=0;
				if (c[i][j]==2)
				{
					white_count++;
					if (white_count==5)
						return 2;
				}
				else
					white_count=0;
			}
		}
		for (int j=0;j<15;++j)	//竖
		{
			int black_count=0;
			int white_count=0;
			for (int i=0;i<15;++i)
			{
				if (c[i][j]==1)
				{
					black_count++;
					if (black_count==5)
						return 1;
				}
				else
					black_count=0;
				if (c[i][j]==2)
				{
					white_count++;
					if (white_count==5)
						return 2;
				}
				else
					white_count=0;
			}
		}
		for (int i=4;i<15;++i)
		{
			int black_count=0;
			int white_count=0;
			for (int j=0;j<i+1;++j)
			{
				if (c[i-j][j]==1)
				{
					black_count++;
					if (black_count==5)
						return 1;
				}
				else
					black_count=0;
				if (c[i-j][j]==2)
				{
					white_count++;
					if (white_count==5)
						return 2;
				}
				else
					white_count=0;
			}
		}
		for (int i=10;i>0;--i)
		{
			int black_count=0;
			int white_count=0;
			for (int j=0;j<15-i;++j)
			{
				if (c[14-j][i+j]==1){
					black_count++;
					if (black_count==5)
						return 1;
				}
				else
					black_count=0;
				if (c[14-j][i+j]==2){
					white_count++;
					if (white_count==5)
						return 2;
				}
				else
					white_count=0;
			}
		}
		for (int i=0;i<11;++i)
		{
			int black_count=0;
			int white_count=0;
			for (int j=0;j<15-i;++j)
			{
				if (c[j][i+j]==1){
					black_count++;
					if (black_count==5)
						return 1;
				}
				else
					black_count=0;
				if (c[j][i+j]==2){
					white_count++;
					if (white_count==5)
						return 2;
				}
					
				else
					white_count=0;
			}
		}
		for (int i=1;i<11;++i)
		{
			int black_count=0;
			int white_count=0;
			for (int j=0;j<15-i;++j)
			{
				if (c[i+j][j]==1){
					black_count++;
					if (black_count==5)
						return 1;
				}
				else
					black_count=0;
				if (c[i+j][j]==2){
					white_count++;
					if (white_count==5)
						return 2;
				}
				else
					white_count=0;
			}
		}
		for (int i=0;i<15;++i)
		{
			for (int j=0;j<15;++j)
			{
				if (c[i][j]==0)
					return 0;
			}
		}
		return 3;
	}
	
	public static int evaluate(){	
		
		int score=0;
		if (cheng5())
		{
			score+=10000*2;
		}
		side=1-side;
		if (cheng5())
		{
			score-=10000;
		}
		side=1-side;
		
		if (huo4()||shuangsi4()||si4huo3())
		{
			score+=8000*2;
		}
		side=1-side;
		if(huo4()||shuangsi4()||si4huo3())
		{
			score-=8000;
		}
		side=1-side;
		
		if (shuanghuo3())
		{
			score+=5000*2;
		}
		side=1-side;
		if (shuanghuo3())
		{
			score-=5000;
		}
		side=1-side;
		
		if (si3huo3())
		{
			score+=200*2;
		}
		side=1-side;
		if (si3huo3())
		{
			score-=200;
		}
		side=1-side;
		
		if (si4())
		{
			score+=150*2;
		}
		side=1-side;
		if (si4())
		{
			score-=150;
		}
		side=1-side;
		
		if (huo3())
		{
			score+=100*2;
		}
		side=1-side;
		if (huo3())
		{
			score-=100;
		}
		side=1-side;
		
		if (shuanghuo2())
		{
			score+=80*2;
		}
		side=1-side;
		if (shuanghuo2())
		{
			score-=80;
		}
		side=1-side;
		
		if (si3())
		{
			score+=50*2;
		}
		side=1-side;
		if (si3())
		{
			score-=50;
		}
		side=1-side;
		
		if (huo2())
		{
			score+=30*2;
		}
		side=1-side;
		if (huo2())
		{
			score-=30;
		}
		side=1-side;
		
		if (si2())
		{
			score+=10*2;
		}
		side=1-side;
		if (si2())
		{
			score-=10;
		}
		side=1-side;

		return score;
		
		/*
		if (cheng5())
		{
			return 10000;
		}
		side=1-side;
		if (cheng5())
		{
			return -10000;
		}
		side=1-side;
		
		if (huo4()||shuangsi4()||si4huo3())
		{
			return 8000;
		}
		side=1-side;
		if(huo4()||shuangsi4()||si4huo3())
		{
			return -8000;
		}
		side=1-side;
		
		if (shuanghuo3())
		{
			return 5000;
		}
		side=1-side;
		if (shuanghuo3())
		{
			return -5000;
		}
		side=1-side;
		
		if (si3huo3())
		{
			return 2000;
		}
		side=1-side;
		if (si3huo3())
		{
			return -2000;
		}
		side=1-side;
		
		if (si4())
		{
			return 1000;
		}
		side=1-side;
		if (si4())
		{
			return -1000;
		}
		side=1-side;
		
		if (huo3())
		{
			return 500;
		}
		side=1-side;
		if (huo3())
		{
			return -500;
		}
		side=1-side;
		
		if (shuanghuo2())
		{
			return 100;
		}
		side=1-side;
		if (shuanghuo2())
		{
			return -100;
		}
		side=1-side;
		
		if (si3())
		{
			return 50;
		}
		side=1-side;
		if (si3())
		{
			return -50;
		}
		side=1-side;
		
		if (huo2())
		{
			return 20;
		}
		side=1-side;
		if (huo2())
		{
			return -20;
		}
		side=1-side;
		
		if (si2())
		{
			return 10;
		}
		side=1-side;
		if (si2())
		{
			return -10;
		}
		side=1-side;
		
		return 0;
		*/
	}
	
	static int AlphaBeta(int depth,int alpha,int beta){
		Graphics g=game.dc.getGraphics();
		if (depth==0 || result()!=0 || huo4())
		{
			return -evaluate();
		}
		/*if (huo4()||cheng5()){
			return INFINITY;
		}	
		*/
		heap h=new heap();
		generateMove(h);
		for (int i=0;i<8;++i)
		{
			if (h.size()==0)
				break;
			Node move=h.removefirst();
			c[move.x][move.y]=side+1;
			side=1-side;
			g.setColor(Color.blue);
			g.fillOval(30*(move.x+1)-3, 30*(move.y+1)-3, 6, 6);
			
			int val=-AlphaBeta(depth-1,-beta,-alpha);
			c[move.x][move.y]=0;
			side=1-side;
			g.setColor(Color.green.darker());
			g.fillRect(30*(move.x+1)-3, 30*(move.y+1)-3, 6, 6);
			g.setColor(Color.black);
			g.drawLine(30*(move.x+1),30*(move.y+1)-3,30*(move.x+1),30*(move.y+1)+3);
			g.drawLine(30*(move.x+1)-3,30*(move.y+1),30*(move.x+1)+3,30*(move.y+1));
			g.drawLine(29, 29, 451, 29);
	        g.drawLine(29, 451, 451, 451);
	        g.drawLine(29, 29, 29, 451);
	        g.drawLine(451, 29, 451, 451);
	        g.setColor(Color.green.darker());
			if (val>=beta)
			{
				return beta;
			}
			if (val>alpha)
			{
				alpha=val;
			}
		}
		return alpha;
	}
}

class MouseClickHandler implements MouseListener{

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Point tmp=e.getPoint();
		boolean flag=false;
		Graphics g=e.getComponent().getGraphics();
		Font f=new Font("Arial",Font.BOLD,13);
		if (Chess.result()==0)
		{
			for (int i=1;i<=15;++i){
				for (int j=1;j<=15;++j){
					if (tmp.x>=30*i-10 && tmp.x<=30*i+10 
							&& tmp.y>=30*j-10 && tmp.y<=30*j+10)
					{
						if (Chess.c[i-1][j-1]==0)
						{
							if (Chess.side==0)
							{
								if (Chess.n!=0)
								{
									g.setColor(Color.BLACK);
									g.setFont(f);
									if (Chess.n<10)
										g.drawString(Integer.toString(Chess.n),30*Chess.x[Chess.n-1]-3 ,30*Chess.y[Chess.n-1]+4);
									else if (Chess.n<100)
										g.drawString(Integer.toString(Chess.n),30*Chess.x[Chess.n-1]-6 ,30*Chess.y[Chess.n-1]+4);
									else
										g.drawString(Integer.toString(Chess.n),30*Chess.x[Chess.n-1]-9 ,30*Chess.y[Chess.n-1]+4);
								}
								Chess.c[i-1][j-1]=1;
								Chess.x[Chess.n]=i;
								Chess.y[Chess.n]=j;
								Chess.n++;
								g.setColor(Color.black);
								g.fillOval(30*i-10, 30*j-10, 20, 20);
								g.setColor(Color.RED);
								g.setFont(f);
								if (Chess.n<10)
									g.drawString(Integer.toString(Chess.n),30*i-3 ,30*j+4);
								else if (Chess.n<100)
									g.drawString(Integer.toString(Chess.n),30*i-6 ,30*j+4);
								else
									g.drawString(Integer.toString(Chess.n),30*i-9 ,30*j+4);		
								Chess.side=1;
								
								if (Chess.result()==0)
								{
									//alpha-beta algorithm
									Node bestMove = null;
									heap h=new heap();
									Chess.generateMove(h);
									int maxV=-Chess.INFINITY*10;
									for (int k=0;k<8;++k)
									{
										if (h.size()==0)
											break;
										Node move=h.removefirst();
										g.setColor(Color.red);
										g.fillOval(30*(move.x+1)-2, 30*(move.y+1)-2, 4, 4);
										Chess.c[move.x][move.y]=Chess.side+1;
										Chess.side=1-Chess.side;
										int val=Chess.AlphaBeta(5,-Chess.INFINITY,Chess.INFINITY);
										System.out.printf("(%d,%d) %d\n",move.x+1,move.y+1,val);
										if (val>maxV)
										{
											maxV=val;
											bestMove=move;
										}
										g.setColor(Color.green.darker());
										g.fillRect(30*(move.x+1)-3, 30*(move.y+1)-3, 6, 6);
										g.setColor(Color.black);
										g.drawLine(30*(move.x+1),30*(move.y+1)-3,30*(move.x+1),30*(move.y+1)+3);
										g.drawLine(30*(move.x+1)-3,30*(move.y+1),30*(move.x+1)+3,30*(move.y+1));
										g.drawLine(29, 29, 451, 29);
								        g.drawLine(29, 451, 451, 451);
								        g.drawLine(29, 29, 29, 451);
								        g.drawLine(451, 29, 451, 451);
								        g.setColor(Color.green.darker());
										Chess.c[move.x][move.y]=0;
										Chess.side=1-Chess.side;
									}
									System.out.println("-------------------");
									i=bestMove.x+1;
									j=bestMove.y+1;
									
									if (Chess.n!=0)
									{
										g.setColor(Color.WHITE);
										g.setFont(f);
										if (Chess.n<10)
											g.drawString(Integer.toString(Chess.n),30*Chess.x[Chess.n-1]-3 ,30*Chess.y[Chess.n-1]+4);
										else if (Chess.n<100)
											g.drawString(Integer.toString(Chess.n),30*Chess.x[Chess.n-1]-6 ,30*Chess.y[Chess.n-1]+4);
										else
											g.drawString(Integer.toString(Chess.n),30*Chess.x[Chess.n-1]-9 ,30*Chess.y[Chess.n-1]+4);
									}
									Chess.c[i-1][j-1]=2;
									Chess.x[Chess.n]=i;
									Chess.y[Chess.n]=j;
									Chess.n++;
									g.setColor(Color.white);
									g.fillOval(30*i-10, 30*j-10, 20, 20);
									g.setColor(Color.RED);
									g.setFont(f);
									if (Chess.n<10)
										g.drawString(Integer.toString(Chess.n),30*i-3 ,30*j+4);
									else if (Chess.n<100)
										g.drawString(Integer.toString(Chess.n),30*i-6 ,30*j+4);
									else
										g.drawString(Integer.toString(Chess.n),30*i-9 ,30*j+4);
									Chess.side=0;
								}	
							}
						}
						flag=true;
						break;
					}
					if (flag)
						break;
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}

class item3_Handler implements ActionListener	//退出
{
	public void actionPerformed(ActionEvent e)
	{
		System.exit(0);
	}
}

class DrawCanvas extends Canvas
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void paint(Graphics g){ 
        g.setColor(Color.green.darker());
        g.fillRect(0,0,480,480);
        g.setColor(Color.black);
        g.drawLine(29, 29, 451, 29);
        g.drawLine(29, 451, 451, 451);
        g.drawLine(29, 29, 29, 451);
        g.drawLine(451, 29, 451, 451);
        for (int i=1;i<=15;++i)
        	g.drawLine(30, 30*i, 450, 30*i);
        for (int i=1;i<=15;++i)
        	g.drawLine(30*i, 30, 30*i, 450);
        g.fillOval(237, 237, 6, 6);
        g.fillOval(357, 357, 6, 6);
        g.fillOval(357, 117, 6, 6);
        g.fillOval(117, 357, 6, 6);
        g.fillOval(117, 117, 6, 6);
	}
}
