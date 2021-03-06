import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

class BaseAlbumProvider extends SwingWorker<Vector<Album>, Object> {
    private final IMessageDisplay outputArea;
    private final ConfigurationService configurationService;
    private final Vector<Album> albumList;

    public int getExecuteCount() {
        return this.executeCount;
    }

    public void setExecuteCount(int executeCount) {
        this.executeCount = executeCount;
    }

    private int executeCount = 1;

    public BaseAlbumProvider(IMessageDisplay output, ConfigurationService configurationService) {
        this.outputArea = output;
        this.configurationService = configurationService;
        this.albumList = new Vector<Album>();
    }

    @Override
    protected Vector<Album> doInBackground() throws Exception {
        retrieveRealData();
        //retrieveFakeData();
        this.executeCount++;
        return this.albumList;
    }

    private void retrieveFakeData() {
        this.albumList.add(new Album("test1", "2", "1"));
        if (this.executeCount % 2 == 0) {
            this.albumList.add(new Album("test2", "2", "2"));
        }
    }

    private void retrieveRealData() {
        try {
            // get last version info from internet
            URL updateURL = new URL(this.configurationService.GetAlbumUrl());

            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new InputStreamReader(updateURL.openStream()));
            reader.beginArray();
            while (reader.hasNext()) {
                Album album = gson.fromJson(reader, Album.class);
                this.albumList.add(album);
            }
            reader.endArray();
            reader.close();

        } catch (FileNotFoundException e) {
            this.outputArea.showMessage("konnte album information nicht laden (FileNotFoundException)\n");
        } catch (UnknownHostException e) {
            this.outputArea.showMessage("konnte album information nicht laden (UnknownHostException)\n");
        } catch (ConnectException e) {
            this.outputArea.showMessage("konnte album information nicht laden (ConnectException)\n");
        } catch (MalformedURLException e) {
            this.outputArea.showMessage("konnte album information nicht laden (MalformedURLException)\n");
        } catch (IOException e) {
            this.outputArea.showMessage("konnte album information nicht laden (IOException)\n");
        }
    }
}
