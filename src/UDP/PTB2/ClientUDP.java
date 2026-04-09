//package UDP.PTB2;
//
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.Graphics;
//import java.awt.GridLayout;
//import java.io.DataInputStream;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.util.Vector;
//
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.xml.crypto.Data;
//
//public class ClientUDP extends JFrame {
//    public static  void main(String[] args) {
//        new ClientUDP();
//    }
//    public ClientUDP() {
//        this.setTile("Phuong trinh tinh toan");
//        this.setSize(800,600);
//        this.setDefaultCloseOperation(3);
//        this.setLayout(new GridLayout(2,2));
//
//        ClientUDP e1 = new ClientUDP("ExchangeRateUSDTo JPY");
//
//    }
//
//}
//class ExchangeRate extends JPanel implements Runnable {
//    String cmd = "ExchangeToUSD";
//    public ExchangeRate(String cmd) {
//        this.cmd = cmd;
//    }
//    public void paint(Graphics graphics) {
//        graphics.setColor(Color.white);
//        graphics.fillRect(0,0,this.getWidth(), this.getHeight());
//        graphics.setColor(Color.BLUE);
//        graphics.drawRect(1,1,this.getWidth(),this.getHeight());
//        int last = 0;
//        for(int i = 1; i <_tg.size();i++) {
//            int x1 = (i - 1) * 5;
//            double y1 = _tg.get(i - 1);
//            int x2 = i * 5;
//            double y2 =_tg.get(i);
//            graphics.drawLine(x1,this.getHeight() - (int)y1,x2,this.getHeight() -(int)y2);
//            last = (int)y2;
//
//        }
//        graphics.setFont(new Font("arial",Font.BOLD,20));
//        graphics.drawString(cmd+":" + last,50,20);
//    }
//    Vector<Double>_tg = new Vector<Double>();
//
//    @Override
//    public void run() {
//        try {
//            DatagramSocket datagramSocket = new DatagramSocket();
//            while(true) {
//                DatagramPacket datagramPacket = new DatagramPacket(cmd.getBytes(),cmd.length()),
//                InetAddress.getLocalHost(),6000);
//                datagramSocket.send(datagramPacket);
//
//                DatagramPacket dtPacket = new DatagramPacket(new byte[100],100);
//                datagramSocket.receive(datagramPacket);
//                String tg =new String(datagramPacket.getData()).substring(0,datagramPacket.getLength());
//                _tg.add(Double.parseDouble(tg));
//                while(_tg.size()* 5 >=this.getWith)) _tg.remove(0);
//                this.repaint(100);
//
//            }
//        }catch (Exception e) {
//
//        }
//    }
//}
