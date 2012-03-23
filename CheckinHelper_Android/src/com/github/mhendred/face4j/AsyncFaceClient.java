package com.github.mhendred.face4j;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
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

public class AsyncFaceClient implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 9137522149263319634L;

	private final List<RequestListener> listeners;

    private final FaceClient faceClient;

    public AsyncFaceClient(FaceClient faceClient) {
	this.faceClient = faceClient;
	this.listeners = new LinkedList<RequestListener>();
    }

    public AsyncFaceClient(String apiKey, String apiSecret) {
	this.faceClient = new DefaultFaceClient(apiKey, apiSecret);
	this.listeners = new LinkedList<RequestListener>();
    }

    public void addTag(final String url, final float x, final float y, final int width,
	    final int height, final String uid, final String label, final String taggerId) {
	new Thread() {
	    public void run() {
		try {
		    faceClient.addTag(url, x, y, width, height, uid, label, taggerId);

		    for (RequestListener listener : listeners) {
			listener.onAddTag();
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.ADD_TAG);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.ADD_TAG);
		}
	    }
	}.start();
    }

    public void detect(final File imageFile) {
	new Thread() {
	    public void run() {
		try {
		    Photo photo = faceClient.detect(imageFile);

		    for (RequestListener listener : listeners) {
			listener.onDetect(photo);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.DETECT);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.DETECT);
		}
	    }
	}.start();
    }

    public void detect(final String urls) {
	new Thread() {
	    public void run() {
		try {
		    List<Photo> photos = faceClient.detect(urls);

		    for (RequestListener listener : listeners) {
			listener.onDetect(photos);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.DETECT);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.DETECT);
		}
	    }
	}.start();
    }

    public void facebookGet(final String uids) {
	new Thread() {
	    public void run() {
		try {
		    List<Photo> photos = faceClient.facebookGet(uids);

		    for (RequestListener listener : listeners) {
			listener.onFacebookGet(photos);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.FACEBOOK);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.FACEBOOK);
		}
	    }
	}.start();
    }

    public void getLimits() {
	new Thread() {
	    public void run() {
		try {
		    LimitsResponse response = faceClient.getLimits();

		    for (RequestListener listener : listeners) {
			listener.onGetLimits(response);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.LIMITS);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.LIMITS);
		}
	    }
	}.start();
    }

    public void getNamespace(final String namespace) {
	new Thread() {
	    public void run() {
		try {
		    Namespace response = faceClient.getNamespace(namespace);

		    for (RequestListener listener : listeners) {
			listener.onGetNamespace(response);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.NAMESPACES);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.NAMESPACES);
		}
	    }
	}.start();
    }

    public void getTags(final String pids, final String urls, final String uids,
	    final String order, final String filter, final boolean together, final int limit) {
	new Thread() {
	    public void run() {
		try {
		    List<Photo> photos = faceClient.getTags(pids, urls, uids, order, filter,
			    together, limit);

		    for (RequestListener listener : listeners) {
			listener.onGetTags(photos);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.GET_TAGS);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.GET_TAGS);
		}
	    }
	}.start();
    }

    public void getTags(final String urls, final String uids, final String order,
	    final String filter, final boolean together, final int limit) {
	new Thread() {
	    public void run() {
		try {
		    List<Photo> photos = faceClient.getTags(urls, uids, order, filter, together,
			    limit);

		    for (RequestListener listener : listeners) {
			listener.onGetTags(photos);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.GET_TAGS);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.GET_TAGS);
		}
	    }
	}.start();
    }

    public void group(final File imageFile, final String uids) throws FaceClientException,
	    FaceServerException {
	new Thread() {
	    public void run() {
		try {
		    GroupResponse response = faceClient.group(imageFile, uids);

		    for (RequestListener listener : listeners) {
			listener.onGroup(response);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.GROUP);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.GROUP);
		}
	    }
	}.start();
    }

    public void group(final String urls, final String uids) throws FaceClientException,
	    FaceServerException {
	new Thread() {
	    public void run() {
		try {
		    GroupResponse response = faceClient.group(urls, uids);

		    for (RequestListener listener : listeners) {
			listener.onGroup(response);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.GROUP);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.GROUP);
		}
	    }
	}.start();
    }

    public void namespaces()  {
	new Thread() {
	    public void run() {
		try {
		    List<Namespace> namespaces = faceClient.namespaces();

		    for (RequestListener listener : listeners) {
			listener.onGetNamespaces(namespaces);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.NAMESPACES);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.NAMESPACES);
		}
	    }
	}.start();
    }

    public void recognize(final File imageFile, final String uids) {
	new Thread() {
	    public void run() {
		try {
		    Photo photo = faceClient.recognize(imageFile, uids);

		    for (RequestListener listener : listeners) {
			listener.onRecognize(photo);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.RECOGNIZE);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.RECOGNIZE);
		}
	    }
	}.start();
    }

    public void recognize(final String urls, final String uids) {
	new Thread() {
	    public void run() {
		try {
		    List<Photo> photos = faceClient.recognize(urls, uids);

		    for (RequestListener listener : listeners) {
			listener.onRecognize(photos);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.RECOGNIZE);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.RECOGNIZE);
		}
	    }
	}.start();
    }

    public void removeTags(final String tids)  {
	new Thread() {
	    public void run() {
		try {
		    List<RemovedTag> removedTags = faceClient.removeTags(tids);

		    for (RequestListener listener : listeners) {
			listener.onRemoveTags(removedTags);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.RECOGNIZE);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.RECOGNIZE);
		}
	    }
	}.start();
    }

    public void saveTags(final String tids, final String uid, final String label)
	     {
	new Thread() {
	    public void run() {
		try {
		    List<SavedTag> savedTags = faceClient.saveTags(tids, uid, label);

		    for (RequestListener listener : listeners) {
			listener.onSaveTags(savedTags);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.SAVE_TAGS);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.SAVE_TAGS);
		}
	    }
	}.start();
    }

    public void status(final String uids)  {
	new Thread() {
	    public void run() {
		try {
		    List<UserStatus> userStatuses = faceClient.status(uids);
		    for (RequestListener listener : listeners) {
			listener.onStatus(userStatuses);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.STATUS);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.STATUS);
		}
	    }
	}.start();
    }

    public void train(final String uid)  {
	new Thread() {
	    public void run() {
		try {
		    TrainResponse response = faceClient.train(uid);
		    for (RequestListener listener : listeners) {
			listener.onTrain(response);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.TRAIN);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.TRAIN);
		}
	    }
	}.start();
    }

    public void users(final String namespaces) {
	new Thread() {
	    public void run() {
		try {
		    UsersResponse response = faceClient.users(namespaces);

		    for (RequestListener listener : listeners) {
			listener.onUsers(response);
		    }
		}

		catch (FaceServerException fse) {
		    notifyListeners(fse, FaceApi.USERS);
		} catch (FaceClientException fce) {
		    notifyListeners(fce, FaceApi.USERS);
		}
	    }
	}.start();
    }

    public void addListener(RequestListener listener) {
	listeners.add(listener);
    }

    public void clearFacebookCreds() {
	faceClient.clearFacebookCreds();
    }

    public void clearTwitterCreds() {
	faceClient.clearTwitterCreds();
    }

    public void setAggressive(boolean isAggressive) {
	faceClient.setAggressive(isAggressive);
    }

    public void setFacebookOauth2(String fbUserId, String oauth2Token) {
	faceClient.setFacebookOauth2(fbUserId, oauth2Token);
    }

    public void setTwitterOauth(String oauthUser, String oauthSecret, String oauthToken) {
	faceClient.setTwitterOauth(oauthUser, oauthSecret, oauthToken);
    }

    public boolean isAggressive() {
	return faceClient.isAggressive();
    }

    public FaceClient getFaceClient() {
	return faceClient;
    }

    private void notifyListeners(FaceServerException fse, FaceApi faceApi) {
	for (RequestListener listener : listeners) {
	    listener.onFaceServerException(fse, faceApi);
	}
    }

    private void notifyListeners(FaceClientException fce, FaceApi faceApi) {
	for (RequestListener listener : listeners) {
	    listener.onFaceClientException(fce, faceApi);
	}
    }
}
