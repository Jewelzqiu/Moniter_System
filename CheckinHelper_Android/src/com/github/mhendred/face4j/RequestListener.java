package com.github.mhendred.face4j;

import java.util.List;

import com.github.mhendred.face4j.exception.FaceClientException;
import com.github.mhendred.face4j.exception.FaceServerException;
import com.github.mhendred.face4j.model.Namespace;
import com.github.mhendred.face4j.model.Photo;
import com.github.mhendred.face4j.model.RemovedTag;
import com.github.mhendred.face4j.model.SavedTag;
import com.github.mhendred.face4j.model.UserStatus;
import com.github.mhendred.face4j.response.GroupResponse;
import com.github.mhendred.face4j.response.LimitsResponse;
import com.github.mhendred.face4j.response.TrainResponse;
import com.github.mhendred.face4j.response.UsersResponse;

public interface RequestListener
{
	/**
	 * Callback to be executed when the request has finished
	 * 
	 * @param response The parsed response object from the request
	 * @param faceApi The API request that has finished
	 */
	public void onDetect(Photo photo);
	
	public void onDetect(List<Photo> photos);
	
	public void onRecognize(Photo photo);
	
	public void onRecognize(List<Photo> photos);
			
	public void onAddTag();
	
	public void onGetTags(List<Photo> photos);
	
	public void onSaveTags(List<SavedTag> savedTags);
	
	public void onStatus(List<UserStatus> userStatus);
	
	public void onFacebookGet(List<Photo> photos);
	
	public void onGroup(GroupResponse response);
	
	public void onGetNamespaces(List<Namespace> namespaces);
	
	public void onGetNamespace(Namespace namespace);

	public void onUsers(UsersResponse response);
	
	public void onGetLimits(LimitsResponse response);
	
	public void onFaceClientException(FaceClientException fce, FaceApi faceApi);
	
	public void onFaceServerException(FaceServerException fse, FaceApi faceApi);

	public void onRemoveTags (List<RemovedTag> removedTags);

	public void onTrain (TrainResponse response);
}