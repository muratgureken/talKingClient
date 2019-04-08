package mg1.com;

public interface Observable {
    void notifyObserver();
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
}