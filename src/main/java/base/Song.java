package base;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.json.JSONObject;

public interface Song {

	public boolean hasMediaURL();

	public String getMediaURL();

	public void generateMediaURL();

	public String getIconURL();

	public HBox getBox();

	public ImageView getImageView();
	
	public String getTitle();

    public JSONObject toJSONObject();

}
