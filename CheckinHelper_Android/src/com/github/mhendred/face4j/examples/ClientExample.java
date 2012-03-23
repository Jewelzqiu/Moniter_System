package com.github.mhendred.face4j.examples;

import java.io.File;

import com.github.mhendred.face4j.DefaultFaceClient;
import com.github.mhendred.face4j.FaceClient;
import com.github.mhendred.face4j.exception.FaceClientException;
import com.github.mhendred.face4j.exception.FaceServerException;
import com.github.mhendred.face4j.model.Face;
import com.github.mhendred.face4j.model.Photo;

/**
 * This is an example of using the client to train your index for one new user. For facebook
 * you can skip the training step and  just call recognize with facebook credentials set
 * 
 * @author mhendred
 *
 */
public class ClientExample 
{
	/**
	 * Your face.com API key
	 */
	protected static final String API_KEY = "56cc7ba56ff82ead6552f8190ebf5745";
	
	/**
	 * Your face.com API secret
	 */
	protected static final String API_SEC = "ef8e7604d2581be04bc5e7787b9e0e51";

	/**
	 * A url of a photo with faces in it
	 */
	protected static final String URL_WITH_FACES = "http://seedmagazine.com/images/uploads/attractive_article.jpg";

	/**
	 * Your face.com API namespace
	 */
	protected static final String NAMESPACE = "radlab";

	/**
	 * user id to recognize
	 */
	protected static final String USER_ID = "qiu@" + NAMESPACE;
	
    public static void main(String[] args) throws FaceClientException, FaceServerException
    {
    	FaceClient faceClient = new DefaultFaceClient(API_KEY, API_SEC);
    	
    	/**
    	 * First we detect some faces in a url. This URL has a single face, So we get back one
    	 * Photo object with one Face object in it.
    	 * 
    	 * You can pass more than one URL (comma delimited String) or you can pass an image file    
    	 * 
    	 * @see http://developers.face.com/docs/api/faces-detect/
    	 * @see http://developers.face.com/docs/api/faces-recognize/
    	 */
    	//Photo photo = faceClient.detect(URL_WITH_FACES).get(0);
    	Photo photo = faceClient.detect(new File("D:/images/20120317150520.jpg")); 
    	/*
    	 * Now we pull out the temporary tag and call save with the desired username and label.
    	 */
    	Face f = photo.getFace();
    	faceClient.saveTags(f.getTID(), USER_ID, "a label");
    	
    	/*
    	 * Let get the training status for this user now. We should see training in progress TRUE
    	 * because we havent called train yet.
    	 */
    	faceClient.status(USER_ID);
    	
    
    	/*
    	 * IMPORTANT: Now we call train on our untrained user. This will commit our saved tag for this user to
    	 * the database so we can recognize them later with 'recognize' calls   
    	 */
    	faceClient.train(USER_ID);
    	
    	/**
    	 * Now we can call recognize. Look for any user in our index (we only have one now)
    	 * We should see a guess now
    	 */
    	//photo = faceClient.recognize(URL_WITH_FACES, "all@" + NAMESPACE).get(0);
    	photo = faceClient.recognize(new File("D:/images/20120322125249.jpg"), "all@" + NAMESPACE);
    	
    	for (Face face : photo.getFaces())
    	{
    		System.out.println(face.getGuesses());
    	}
    }
}