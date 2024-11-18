import java.util.*;

public class DiskSchedulingAlgorithm {
    public static void main(String[] args) {
        int[] requestQueue = {98, 183, 37, 122, 14, 124, 65, 67};
        int headPosition = 53;
        int diskSize = 200;

        System.out.println("SSTF Total Head Movement: " + sstf(requestQueue, headPosition));
        System.out.println("SCAN Total Head Movement: " + scan(requestQueue, headPosition, diskSize));
        System.out.println("C-LOOK Total Head Movement: " + cLook(requestQueue, headPosition));
    }

    public static int sstf(int[] requestQueue, int headPosition) {
        List<Integer> requests = new ArrayList<>();
        for (int request : requestQueue) {
            requests.add(request);
        }

        List<Integer> serviceOrder = new ArrayList<>();
        int totalHeadMovement = 0;
        while (!requests.isEmpty()) {
            int closestRequest = findClosestRequest(requests, headPosition);
            totalHeadMovement += Math.abs(headPosition - closestRequest);
            headPosition = closestRequest;
            serviceOrder.add(closestRequest);
            requests.remove(Integer.valueOf(closestRequest));
        }

        System.out.println("SSTF Service Order: " + serviceOrder);
        return totalHeadMovement;
    }

    private static int findClosestRequest(List<Integer> requests, int headPosition) {
        int closestRequest = requests.get(0);
        int minDistance = Math.abs(headPosition - closestRequest);

        for (int request : requests) {
            int distance = Math.abs(headPosition - request);
            if (distance < minDistance) {
                minDistance = distance;
                closestRequest = request;
            }
        }

        return closestRequest;
    }

    public static int scan(int[] requestQueue, int headPosition, int diskSize) {
        List<Integer> requests = new ArrayList<>();
        for (int request : requestQueue) {
            requests.add(request);
        }
        requests.add(headPosition); // Add the initial head position
        requests.add(0); // Add the start of the disk
        requests.add(diskSize - 1); // Add the end of the disk
        Collections.sort(requests);

        List<Integer> serviceOrder = new ArrayList<>();
        int totalHeadMovement = 0;
        int currentIndex = requests.indexOf(headPosition);

        // Move towards the end of the disk
        for (int i = currentIndex; i < requests.size(); i++) {
            totalHeadMovement += Math.abs(headPosition - requests.get(i));
            headPosition = requests.get(i);
            serviceOrder.add(headPosition);
        }

        // Move towards the start of the disk
        for (int i = currentIndex - 1; i >= 0; i--) {
            totalHeadMovement += Math.abs(headPosition - requests.get(i));
            headPosition = requests.get(i);
            serviceOrder.add(headPosition);
        }

        System.out.println("SCAN Service Order: " + serviceOrder);
        return totalHeadMovement;
    }

    public static int cLook(int[] requestQueue, int headPosition) {
        List<Integer> requests = new ArrayList<>();
        for (int request : requestQueue) {
            requests.add(request);
        }
        requests.add(headPosition); // Add the initial head position
        Collections.sort(requests);

        List<Integer> serviceOrder = new ArrayList<>();
        int totalHeadMovement = 0;
        int currentIndex = requests.indexOf(headPosition);

        // Move towards the end of the disk
        for (int i = currentIndex; i < requests.size(); i++) {
            totalHeadMovement += Math.abs(headPosition - requests.get(i));
            headPosition = requests.get(i);
            serviceOrder.add(headPosition);
        }

        // Jump to the beginning of the disk
        totalHeadMovement += Math.abs(headPosition - requests.get(0));
        headPosition = requests.get(0);
        serviceOrder.add(headPosition);

        // Move towards the initial head position
        for (int i = 0; i < currentIndex; i++) {
            totalHeadMovement += Math.abs(headPosition - requests.get(i));
            headPosition = requests.get(i);
            serviceOrder.add(headPosition);
        }

        System.out.println("C-LOOK Service Order: " + serviceOrder);
        return totalHeadMovement;
    }
}
