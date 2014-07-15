package org.mk300.marshal.bench;

import java.util.ArrayList;
import java.util.List;

import data.media.Image;
import data.media.Image.Size;
import data.media.Media;
import data.media.Media.Player;
import data.media.MediaContent;

public class Util {

	private static final MediaContent obj;
	
	static {

		List<String> persons = new ArrayList<>();
		persons.add("Bill Gates");
		persons.add("Steve Jobs");
		
		Media media = new Media("http://javaone.com/keynote.mpg",
				"Javaone Keynote",
				640,
				480,
				"video/mpg4",
				18000000,
				58982400,
				262144,
				true,
				persons,
				Player.JAVA,
				null);
		
		List<Image> images= new ArrayList<>();
		{
			Image image = new Image("http://javaone.com/keynote_large.jpg",
					"Javaone Keynote", 1024, 768, Size.LARGE);
			
			images.add(image);
		}
		{
			Image image = new Image("http://javaone.com/keynote_small.jpg",
					"Javaone Keynote", 320, 240, Size.SMALL);
			
			images.add(image);
		}
		MediaContent mediaContent = new MediaContent(media, images);
		
		obj = mediaContent;
	}
	
	
	public static final MediaContent getObject() {
		return obj;
	}
}
