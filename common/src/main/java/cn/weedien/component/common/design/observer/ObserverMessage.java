package cn.weedien.component.common.design.observer;

/**
 * Message notifying observer.
 */
public interface ObserverMessage<T> {

    /**
     * Message method definition
     *
     * @return Different message formats
     */
    T message();
}
