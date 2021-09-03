package me.archonic.liteevents.main;

/**
 * Default event class. To write your own event classes specific to your project, make sure to extend this one.
 */
public class Event {
    /**
     Override this method with what you would like to run after the event is called, if you please. It will be called immediately and directly after this event has been fired.
     */
    public void afterTask(){}
}