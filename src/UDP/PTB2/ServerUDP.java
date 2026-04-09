package UDP.PTB2;
import java.net.*;
import java.util.*;
import javax.script.*;
import java.util.Map;


public class ServerUDP extends  Thread {
    public static void main(String[] args) throws Exception {
       DatagramSocket serverSocket = new DatagramSocket(1234);
       byte[] receiverData = new byte[1024];
    }
    Map<String ,Double> tg = new HashMap<String,Double>();
    public void run() {
        Random random = new Random();
        while(true) {
            for (String key: tg.keySet()) {
                double tg_now = tg.get(key);
                double tg_new = tg_now*(1 + (random.nextDouble() - 0.5) * 0.01);
                tg.put(key,tg_new);
            }
            try {
                Thread.sleep(10);
            }catch ( InterruptedException e) {

            }
        }
    }
    public ServerUDP() {
        tg.put("USD",26166.00);
        tg.put("EUR",30207.65);
        tg.put("JPT",165.86);
        tg.put("VND",1.0);
        this.start();

        try {
            DatagramSocket datagramSocket = new DatagramSocket(6000);
            while(true) {
                DatagramPacket datagramPacket = new DatagramPacket(new byte[100],100);
                datagramSocket.receive(datagramPacket);

                String str = new String(datagramPacket.getData()).substring(0,datagramPacket.getLength());
                if (!"Chenh lech".equals(str.substring(0,12)))continue;
                String name1 = str.substring(12,15);
                if (! "to".equals(str.substring(15,17))) continue;
                String name2 = str.substring(17,20);
                String _tg = tg.get(name1)/tg.get(name2) +"";

                DatagramPacket datagramPacket1 = new DatagramPacket(_tg.getBytes(),_tg.length());
                datagramSocket.send(datagramPacket1);
            }
        }catch (Exception e) {

        }
    }

}

