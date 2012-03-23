package com.github.mhendred.face4j.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mhendred.face4j.AsyncAdapter;
import com.github.mhendred.face4j.FaceApi;
import com.github.mhendred.face4j.AsyncFaceClient;
import com.github.mhendred.face4j.RequestListener;
import com.github.mhendred.face4j.exception.FaceServerException;
import com.github.mhendred.face4j.model.Photo;

/**
 * Example using the the Asychronus client
 * 
 * @author mhendred
 *
 */
public class AsyncExample extends ClientExample
{
	private static final Logger logger = LoggerFactory.getLogger(AsyncExample.class);
	
	public static void main(String[] args)
	{
		AsyncFaceClient faceClient = new AsyncFaceClient(API_KEY, API_SEC);
		
		RequestListener listener = new AsyncAdapter() {
			@Override
			public void onDetect(Photo photo) {
				logger.info("Success: ", photo);
			}
			
			@Override
			public void onFaceServerException(FaceServerException fse, FaceApi faceApi) {
				logger.error("Error: ", fse);
			}
		};
		
		faceClient.addListener(listener);
		faceClient.detect(URL_WITH_FACES);
	}
}
