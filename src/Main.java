import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class Main extends Applet implements Runnable, MouseListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 300000;

	// initialize variables for basic graphics
	private Graphics gg;
	private Image ii;
	private Random r = new Random();

	// process threads
	Thread thread = new Thread(this);

	// other objects and variables
	Shape possible_shapes[] = new Shape[32];
	Shape shapes[] = new Shape[7];
	int drawn_shapes[] = new int[shapes.length];
	Shape answer;
	int answernum;
	int score = 0;
	int second = 240;
	int time;
	int level = 0;
	boolean gameover;
	Font font = new Font("Serif", Font.BOLD, 32);

	@Override
	public void init()
	{
		// listen for UI
		addMouseListener(this);

		// setup default window parameters
		setBackground(Color.black);
		setSize(1000, 800);

		// create array for all possible shapes
		int h;
		int i = 0;
		
		for (byte j = 0; j < 2; j++)
		{
			if (j == 0)
			{
				h = 1;
			}
			else
			{
				h = 3;
			}
			for (byte k = 0; k < 2; k++)
			{
				for (byte l = 1; l < 9; l++)
				{
					possible_shapes[i] = new Shape(h, l, k, 200, 200, this);
					i++;
				}
			}
		}
	}

	@Override
	public void start()
	{

		nextScreen();
		thread.start();
	}

	@Override
	public void run()
	{

		while (true)
		{
			level = (int) score / 10;

			second--;
			time = (int) (second / (60));
			if (time <= 0)
			{
				time = 0;
				gameover = true;
			}

			if (gameover)
			{
				restart();
			}

			repaint();

			// set fps to 60
			try
			{
				Thread.sleep(16);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stop()
	{
		super.stop();
	}

	@Override
	public void destroy()
	{
		super.destroy();
	}

	@Override
	public void update(Graphics g)
	{
		// double buffering code
		if (ii == null)
		{
			ii = createImage(this.getWidth(), this.getHeight());
			gg = ii.getGraphics();
		}

		gg.setColor(getBackground());
		gg.fillRect(0, 0, this.getWidth(), this.getHeight());

		gg.setColor(getForeground());
		paint(gg);

		g.drawImage(ii, 0, 0, this);
	}

	@Override
	public void paint(Graphics g)
	{

		// paint initial objects
		for (byte i = 0; i < shapes.length; i++)
		{
			shapes[i].paint(g);
		}

		// draw answer, time, and score box
		g.setColor(Color.white);
		g.drawRoundRect(1, 1, 200, 200, 10, 10);
		g.drawRoundRect(this.getWidth() / 2 - 100, 0, 200, 100, 10, 10);
		g.drawRoundRect(this.getWidth() - 201, 0, 200, 100, 10, 10);
		g.drawRoundRect(1, this.getHeight() - 52, 150, 52, 10, 10);

		// draw time string
		String s = Integer.toString(time);
		g.setFont(font);
		g.setColor(Color.lightGray);
		g.drawString(s, getWidth() / 2 - 10 + 2, 50 - 2);
		g.setColor(Color.white);
		g.drawString(s, getWidth() / 2 - 10, 50);

		// draw score string
		String s1 = "score: " + Integer.toString(score);
		g.setFont(font);
		g.setColor(Color.lightGray);
		g.drawString(s1, getWidth() - 180 + 2, 50 - 2);
		g.setColor(Color.white);
		g.drawString(s1, getWidth() - 180, 50);

		// draw level string
		String s2 = "level:" + Integer.toString(level);
		g.setFont(font);
		g.setColor(Color.lightGray);
		g.drawString(s2, 7, this.getHeight() - 12);
		g.setColor(Color.white);
		g.drawString(s2, 5, this.getHeight() - 10);

		// draw correct shape in answer box
		answer.paint(g);

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		for (byte i = 0; i < shapes.length; i++)
		{
			// check for click on possible answer circle
			if (i == answernum)
			{
				if (shapes[i].getSides() == 1)
				{
					if (e.getX() > shapes[i].getShapeX()
							&& e.getX() < shapes[i].getShapeX()
									+ shapes[i].getSize())
					{
						if (e.getY() > shapes[i].getShapeY()
								&& e.getY() < shapes[i].getShapeY()
										+ shapes[i].getSize())
						{
							score++;
						}
						else
						{
							gameover = true;
						}
					}
					else
					{
						gameover = true;
					}
					// check for click on possible answer triangle
				}
				else if (shapes[i].getSides() == 3)
				{
					if (e.getX() > shapes[i].getShapeX() - shapes[i].getSize()
							&& e.getX() < shapes[i].getShapeX()
									+ shapes[i].getSize())
					{
						if (e.getY() < shapes[i].getShapeY()
								&& e.getY() > shapes[i].getShapeY()
										- shapes[i].getShapeHeight())
						{
							score++;
						}

					}
					else
					{
						gameover = true;
					}
				}
				else
				{
					System.out.println("How did you make this print?!?");
				}
			}
		}
	}

	

	public void restart()
	{
		gameover = false;
		answernum = r.nextInt(shapes.length);
		score = 0;
	}

	public void nextScreen()
	{

		for (byte i = 0; i < drawn_shapes.length; i++)
		{
			drawn_shapes[i] = 0;
		}
		
		// reset timer
		second = 240;

		// setup answer array position
		answernum = r.nextInt(shapes.length);

		for (byte j = 0; j < drawn_shapes.length; j++)
		{
			drawn_shapes[j] = r.nextInt(32);
			CorrectDuplicates(j);
		}

		for (byte i = 0; i < shapes.length; i++)
		{
			shapes[i] = possible_shapes[drawn_shapes[i]];
			shapes[i].setShapeX(r.nextInt((this.getWidth() - 300) - 100) + 250);
			shapes[i]
					.setShapeY(r.nextInt((this.getHeight() - 200) - 150) + 150);
			if (i == answernum)
			{
				// for circle
				if (shapes[i].getSides() == 1)
				{
					answer = new Shape(shapes[answernum].getSides(),
							shapes[answernum].getColor(),
							shapes[answernum].getStyle(), 36, 36, this);
				// for triangle
				}
				else
				{
					answer = new Shape(shapes[answernum].getSides(),
							shapes[answernum].getColor(),
							shapes[answernum].getStyle(), 100, 150, this);
				}
			}
		}

	}

	public void CorrectDuplicates(byte j)
	{
		for (byte k = 0; k < j; k++)
		{
			if (drawn_shapes[j] == drawn_shapes[k])
			{
				drawn_shapes[j] = r.nextInt(32);
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		nextScreen();
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}