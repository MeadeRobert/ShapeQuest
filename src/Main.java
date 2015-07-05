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

	private static final long serialVersionUID = 300000;

	//graphics
	private Graphics _gg;
	private Image _ii;
	private Font _font = new Font("Serif", Font.BOLD, 32);
	
	private Thread _thread = new Thread(this);
	
	//shapes
	private Shape _possible_shapes[] = new Shape[32];
	private Shape _shapes[] = new Shape[7];
	private Shape _answer;
	
	//shape numbers
	private int _drawnShapes[] = new int[_shapes.length];
	private int _answernum;
	
	//score and time
	private int _score = 0;
	private int _rawTime = 240;
	private int _time;
	private int _level = 0;
	
	private boolean _gameover;
	
	private Random _random = new Random();
	
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
			else {
				h = 3;
			}
			for (byte k = 0; k < 2; k++)
			{
				for (byte l = 1; l < 9; l++)
				{
					_possible_shapes[i] = new Shape(h, l, k, 200, 200, this, _random);
					i++;
				}
			}
		}
	}

	@Override
	public void start()
	{
		nextScreen();
		_thread.start();
	}

	@Override
	public void run()
	{
		while (true)
		{
			_level = (int) _score / 10;

			_rawTime--;
			_time = (int) (_rawTime / (60));
			if (_time <= 0)
			{
				_time = 0;
				_gameover = true;
			}

			if (_gameover)
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
		if (_ii == null)
		{
			_ii = createImage(this.getWidth(), this.getHeight());
			_gg = _ii.getGraphics();
		}

		_gg.setColor(getBackground());
		_gg.fillRect(0, 0, this.getWidth(), this.getHeight());

		_gg.setColor(getForeground());
		paint(_gg);

		g.drawImage(_ii, 0, 0, this);
	}

	@Override
	public void paint(Graphics g)
	{
		// paint initial objects
		for (byte i = 0; i < _shapes.length; i++)
		{
			_shapes[i].paint(g);
		}

		// draw answer, time, and score box
		g.setColor(Color.white);
		g.drawRoundRect(1, 1, 200, 200, 10, 10);
		g.drawRoundRect(this.getWidth() / 2 - 100, 0, 200, 100, 10, 10);
		g.drawRoundRect(this.getWidth() - 201, 0, 200, 100, 10, 10);
		g.drawRoundRect(1, this.getHeight() - 52, 150, 52, 10, 10);

		// draw time string
		String s = Integer.toString(_time);
		g.setFont(_font);
		g.setColor(Color.lightGray);
		g.drawString(s, getWidth() / 2 - 10 + 2, 50 - 2);
		g.setColor(Color.white);
		g.drawString(s, getWidth() / 2 - 10, 50);

		// draw score string
		String s1 = "score: " + Integer.toString(_score);
		g.setFont(_font);
		g.setColor(Color.lightGray);
		g.drawString(s1, getWidth() - 180 + 2, 50 - 2);
		g.setColor(Color.white);
		g.drawString(s1, getWidth() - 180, 50);

		// draw level string
		String s2 = "level:" + Integer.toString(_level);
		g.setFont(_font);
		g.setColor(Color.lightGray);
		g.drawString(s2, 7, this.getHeight() - 12);
		g.setColor(Color.white);
		g.drawString(s2, 5, this.getHeight() - 10);

		// draw correct shape in answer box
		_answer.paint(g);

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		for (byte i = 0; i < _shapes.length; i++)
		{
			// check for click on possible answer circle
			if (i == _answernum)
			{
				if (_shapes[i].getSides() == 1)
				{
					if (e.getX() > _shapes[i].getShapeX()
							&& e.getX() < _shapes[i].getShapeX()
									+ _shapes[i].getSize())
					{
						if (e.getY() > _shapes[i].getShapeY()
								&& e.getY() < _shapes[i].getShapeY()
										+ _shapes[i].getSize())
						{
							_score++;
						}
						else
						{
							_gameover = true;
						}
					}
					else
					{
						_gameover = true;
					}
					// check for click on possible answer triangle
				}
				if (_shapes[i].getSides() == 3)
				{
					if (e.getX() > _shapes[i].getShapeX() - _shapes[i].getSize()
							&& e.getX() < _shapes[i].getShapeX()
									+ _shapes[i].getSize())
					{
						if (e.getY() < _shapes[i].getShapeY()
								&& e.getY() > _shapes[i].getShapeY()
										- _shapes[i].getShapeHeight())
						{
							_score++;
						}

					}
					else
					{
						_gameover = true;
					}
				}
			}
		}
	}

	

	public void restart()
	{
		_gameover = false;
		_answernum = _random.nextInt(_shapes.length);
		_score = 0;
	}

	public void nextScreen()
	{

		for (byte i = 0; i < _drawnShapes.length; i++)
		{
			_drawnShapes[i] = 0;
		}
		
		// reset timer
		_rawTime = 240;

		// setup answer array position
		_answernum = _random.nextInt(_shapes.length);

		for (byte j = 0; j < _drawnShapes.length; j++)
		{
			_drawnShapes[j] = _random.nextInt(32);
			CorrectDuplicates(j);
		}

		for (byte i = 0; i < _shapes.length; i++)
		{
			_shapes[i] = _possible_shapes[_drawnShapes[i]];
			_shapes[i].setShapeX(_random.nextInt((this.getWidth() - 300) - 100) + 250);
			_shapes[i]
					.setShapeY(_random.nextInt((this.getHeight() - 200) - 150) + 150);
			if (i == _answernum)
			{
				// for circle
				if (_shapes[i].getSides() == 1)
				{
					_answer = new Shape(_shapes[_answernum].getSides(),
							_shapes[_answernum].getColor(),
							_shapes[_answernum].getStyle(), 36, 36, this, _random);
				// for triangle
				}
				else
				{
					_answer = new Shape(_shapes[_answernum].getSides(),
							_shapes[_answernum].getColor(),
							_shapes[_answernum].getStyle(), 100, 150, this, _random);
				}
			}
		}

	}

	public void CorrectDuplicates(byte j)
	{
		for (byte k = 0; k < j; k++)
		{
			if (_drawnShapes[j] == _drawnShapes[k])
			{
				_drawnShapes[j] = _random.nextInt(32);
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