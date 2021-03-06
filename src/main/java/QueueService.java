import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        ConcurrentLinkedQueue<String> concurrentQueue = new ConcurrentLinkedQueue<>();
        Resp resp = new Resp("", "204");
        if ("POST".equals(req.getHttpRequestType())) {
            concurrentQueue.add(req.getParam());
            queue.putIfAbsent(req.getSourceName(), concurrentQueue);
        } else if ("GET".equals(req.getHttpRequestType())) {
           String status;
           String text = queue.get(req.getSourceName()).poll();
           if (text == null) {
               status = "404";
               text = "";
           } else {
               status = "200";
           }
           resp = new Resp(text, status);
        }
        return resp;
    }
}
