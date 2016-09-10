
import javax.imageio.ImageIO;
import javax.sound.midi.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by EricMVasey on 9/10/2016.
 */
public class Main
{
	public static void main(String[] args)
	{
		File f = new File("tri-m.jpg");
		Color[][] rgb_img;
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(f);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		rgb_img = new Color[img.getHeight()][img.getWidth()];

		for(int i = 0; i < img.getHeight(); i++)
		{
			for(int j = 0; j < img.getWidth(); j++)
			{
				rgb_img[i][j] = new Color(img.getRGB(j,i));
			}
		}

		int[] row_sums = new int[img.getHeight()];
		int index = 0, max = -1;

		ArrayList<Integer> notes = new ArrayList<>();

		for(Color[] row: rgb_img)
		{
			int sum = 0;
			for(Color col: row)
			{
				if(col.getRed() < 150)
				{
					sum++;
				}
			}

			row_sums[index++] = sum;
		}

		for(int sum: row_sums)
		{
			if(sum > 50)
				notes.add(sum);
		}

		System.out.println(max);

		try
		{
			ShortMessage msg = new ShortMessage();
			msg.setMessage(ShortMessage.NOTE_ON, 0, 60, 93);

			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();

			MidiChannel[] channels = synth.getChannels();


			for(int i = 0; i < notes.size(); i++)
			{
				channels[0].noteOn(convertRange(notes.get(i)),93);

				if(i < notes.size()-1)
					Thread.sleep(300);
				else
					Thread.sleep(2000);

				channels[0].noteOff(convertRange(notes.get(i)));
			}

			synth.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static int convertRange(int in)
	{
		return (int) ((in * 75.0f) / 72.0f);
	}
}
