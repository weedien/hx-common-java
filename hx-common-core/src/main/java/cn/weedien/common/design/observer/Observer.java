package cn.weedien.common.design.observer;

/**
 * Observer.
 */
public interface Observer<T> {

    /**
     * Receive notification.
     *
     * @param observerMessage
     */
    void accept(ObserverMessage<T> observerMessage);
}
