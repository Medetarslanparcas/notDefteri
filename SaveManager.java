package swing;

import java.util.ArrayList;
import java.util.List;

public class SaveManager {
    private final List<SaveObserver> observers = new ArrayList<>();

    public void addObserver(SaveObserver observer) {
        observers.add(observer);
    }
    public void notifyObservers() {
        for (SaveObserver observer : observers) {
            observer.onSave();
        }
    }
}
