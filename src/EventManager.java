import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;
/**
 * A lightweight event manager by Archonic. Credit appreciated, but not required. Regardless, please don't claim it as your own or remove this message. Thanks, and have fun!
 */
@SuppressWarnings("unused")
public class EventManager {
    private final Set<VerifiedListener> listeners = new HashSet<>();

    private static EventManager instance;
    /**
     * Retrieves the event manager's instance. This instance must be retrieved in order to perform any operation regarding the event manager.
     *
     * @returns The EventManager's instance, or a newly initialized EventManager instance if one has not been set yet
     */
    public static EventManager getEventManager(){
        return instance == null ? init() : instance;
    }

    /**
     * Private instance initializer method.
     * @returns The initialized EventManager instance
     */
    private static EventManager init(){
        return instance = new EventManager();
    }

    /**
     * Registers a listener object for eligible event methods to be called
     * @param listener The {@link VerifiedListener} object to register
     */
    public void registerEvents(VerifiedListener listener){
        listeners.add(listener);
    }

    /**
     * Unregisters all curretly existing and registered listeners.
     */
    public void clearEventListeners(){
        listeners.clear();
    }

    /**
     * Unregisters a {@link VerifiedListener} object.
     * @param listener The listener object to be unregistered
     * @return True if the listener to be unregistered was registered in the first place
     */
    public boolean deregisterEvents(VerifiedListener listener){
        return listeners.remove(listener);
    }

    /**
     * Calls all eligible methods with this {@link Event} object.
     * An event method is eligible to be called if:
     * <br>
     *     <li> The candidate method resides within a registered {@link VerifiedListener} object</li>
     *     <li> The candidate method only has one parameter</li>
     *     <li> The {@link Event} object passed into the {@code call()} method is an instance of the candidate method's single parameter type</li>
     *     <li> The candidate method's name does not end with "Uncalled"</li>
     * <br>
     * After all eligible calls have been completed (successfully or unsuccessfully) the event object's {@code afterTask()} method will be run.
     * @param event The event object with which to call eligible methods.
     */
    public void call(Event event){
        for(VerifiedListener listener : listeners){
            for(Method method : listener.getClass().getDeclaredMethods()){
                if(!method.getName().endsWith("Uncalled") && !Modifier.isStatic(method.getModifiers())){
                    Parameter[] params = method.getParameters();
                    if(params.length == 1 && params[0].getType().isInstance(event)){
                        try{
                            method.invoke(listener, event);
                        }catch(IllegalAccessException | InvocationTargetException e){
                            System.out.println("Error calling event on method " + method.getName() + ":");
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        event.afterTask();
    }

    /**
     * Private constructor (suppresses default constructor).
     * <br>In order to use the EventManager class, you must retreive an instance of it using {@code EventManager.getEventManager()}.
     */
    private EventManager(){}
}