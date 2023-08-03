import java.util.ArrayList;
import java.util.Observable;

public class MainChannel extends Observable {

    private final ArrayList<String> mainChannelPosts;

    public MainChannel() {
        mainChannelPosts = new ArrayList<>();
    }

    private void updateContentViewArea() {
        // Notify all observers (User1) about the new post
        setChanged();
        notifyObservers(mainChannelPosts);
    }

    public void addPost(String postText) {
        if (!postText.trim().isEmpty()) {
            mainChannelPosts.add(0, postText);
            updateContentViewArea();
        }
    }

    public ArrayList<String> getMainChannelPosts() {
        return mainChannelPosts;
    }

}
