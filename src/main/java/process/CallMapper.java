package process;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Dec 03, 2018 10:27
 */
public class CallMapper {
    private Map<String, String> call;
    private static CallMapper _instance = new CallMapper();
    private CallMapper() {
        synchronized (CallMapper.class) {
            call = new HashMap<String, String>();
        }
    }

    public static CallMapper getInstance() {
        return _instance;
    }

    public void addCall(UUID uuid, String className) {
        synchronized (CallMapper.class) {
            call.put(uuid.toString(), className);
        }
    }

    public String getCall(UUID uuid) {
        synchronized (CallMapper.class) {
            String className = call.get(uuid.toString());
            call.remove(uuid.toString());
            return className;
        }
    }
}
