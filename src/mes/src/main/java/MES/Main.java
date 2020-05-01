package MES;

import org.bouncycastle.asn1.eac.UnsignedInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.UaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Timer;


public class Main {

    public static List<orderTransform> orderListTransformation = Collections.synchronizedList(new ArrayList<>());
    public static List<orderUnload> orderListUnload = Collections.synchronizedList(new ArrayList<>());
    public static Timer timer = new Timer();
    public static PriorityQueue<order> ordersPriority = new PriorityQueue<>(new OrderComparator());

    public static int unitCount;
    public static SFS floor = new SFS();
    //public static String aux = "DESKTOP-LPATDUL";
    public static String Client = "opc.tcp://DESKTOP-RNTM3PU:4840";
    //public static String Client = "opc.tcp://LAPTOP-UGJ82VH1:4840";
    public static OPCUA_Connection MyConnection = new OPCUA_Connection(Client);

    static Thread threadReadSystem;
    static Thread threadUnload;
    static Thread threadLoad;
    static Thread threadTransformation;



    public static void main(String[] args) throws Exception {
        //Start Global Timer

        //Creates object for connection and makes the connection
        OpcUaClient connection = MyConnection.MakeConnection();
        connection.connect().get();


        int port = 54321;

        udpServer server = new udpServer(port);
        //sendXML client = new sendXML(port, "C:\\XML\\received_data.xml"); // linha de testes

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        //executorService.submit(client);
        executorService.submit(server);
        //executorService.submit(client); //linha de testes
        //executorService.submit(server);
        passPath passPath = new passPath();
        executorService.submit(passPath);


        TransformationsGraph transformTable = new TransformationsGraph();

        /* TESTE DE FUNÇÕES PARA OPC-UA
        // (funcionar) getValue, getValueBoolean, setValueBoolean, getValueString, setValueString, getValueInt, setValueInt
        // (não Funcionar)
        System.out.println("--------------Value Change--------------");
        OPCUA_Connection.getValueBoolean("MAIN_TASK","AT1.SENSOR");
        System.out.println("--------------Value Change--------------");
        int x = OPCUA_Connection.getValueInt("MAIN_TASK","UNIT_COUNT_MES");
        System.out.println("----" + x);
         */

        //Start thread that updates Floor
        threadReadSystem = new Thread(new readSystem());
        threadReadSystem.start();

        //Start thread that handles TransformationsOrders
        threadTransformation = new Thread(new TransformationThread());
        threadTransformation.start();

        //Start thread that handles Unload Orders
        threadUnload = new Thread(new UnloadThread());
        threadUnload.start();

        //Start thread that handles Load Orders
        threadLoad = new Thread(new LoadThread());
        threadLoad.start();


    }

}
