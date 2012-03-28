package com.github.mhendred.face4j.examples;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.github.mhendred.face4j.DefaultFaceClient;
import com.github.mhendred.face4j.FaceClient;
import com.github.mhendred.face4j.exception.FaceClientException;
import com.github.mhendred.face4j.exception.FaceServerException;
import com.github.mhendred.face4j.model.Face;
import com.github.mhendred.face4j.model.Guess;
import com.github.mhendred.face4j.model.Photo;
import com.github.mhendred.face4j.model.UserStatus;
import com.github.mhendred.face4j.response.TrainResponse;

public class MyExample {

	protected static final String API_KEY = "56cc7ba56ff82ead6552f8190ebf5745";	
	protected static final String API_SEC = "ef8e7604d2581be04bc5e7787b9e0e51";
	protected static final String NAMESPACE = "radlab";
	protected static final String USER_ID = "qiu@" + NAMESPACE;
	
	FaceClient faceClient = new DefaultFaceClient(API_KEY, API_SEC);
	
	public boolean train(String filename, String id, String label)
			throws FaceClientException, FaceServerException {
		String uid = id + "@" + NAMESPACE;
		label = (label == null) ? "label" : label;		
		Photo photo = faceClient.detect(new File(filename));
		Face f = photo.getFace();
    	faceClient.saveTags(f.getTID(), uid, label);
    	TrainResponse response = faceClient.train(uid);
    	List<UserStatus> updated = response.getUpdated();
    	Iterator<UserStatus> iterator = updated.iterator();
    	while (iterator.hasNext()) {
    		UserStatus status = iterator.next();
    		if (status.getUID().equals(uid)) {
    			return true;
    		}
    	}
    	return false;
	}
	
	public HashSet<String> recognize(String filename)
			throws FaceClientException, FaceServerException {
		HashSet<String> list = new HashSet<String>();
		Photo photo = faceClient.recognize(new File(filename), "all@" + NAMESPACE);
		for (Face face : photo.getFaces()) {
			for (Guess guess : face.getGuesses()) {
				System.out.println(guess);
				if (list.contains(guess.first.substring(0, guess.first.indexOf('@')))) {
					continue;
				}
				if (guess.second > 80) {
					list.add(guess.first.substring(0, guess.first.indexOf('@')));
				}
			}
		}
		return list;
	}
}
