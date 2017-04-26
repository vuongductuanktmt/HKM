//package com.example.nghia.hkm;
//
//import android.support.v4.app.Fragment;
//import android.util.Log;
//
//import com.example.nghia.hkm.Adapter.ViewPagerAdapter;
//import com.example.nghia.hkm.Class.ClientToServer;
//import com.example.nghia.hkm.Class.Products;
//import com.example.nghia.hkm.Class.ServerToClient;
//import com.example.nghia.hkm.Fragment.FragmentSPDYT;
//
//import org.bson.Document;
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.handshake.ServerHandshake;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static android.R.id.list;
//import static com.example.nghia.hkm.MainActivity.mDocuments;
//import static com.example.nghia.hkm.MainActivity.message;
//import static com.example.nghia.hkm.R.id.viewPager;
//
///**
// * Created by huu21 on 26/04/2017.
// */
//
//public class ConnectWebSocket {
//    WebSocketClient mWebSocketClient;
//    private List<Products> newList = new ArrayList<>();
//    public ViewPagerAdapter mViewPagerAdapter;
//
//    private void ConnectWebSocket(final int i) {
//        URI uri = null;
//        try {
//            uri = new URI("ws://130.211.149.139:6969/");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        mWebSocketClient = new WebSocketClient(uri) {
//            @Override
//            public void onOpen(ServerHandshake handshakedata) {
//                Log.d("requesting, ","wait");
//                ClientToServer Cts = new ClientToServer("LuuHung", "user", "get_pagination", "TableWebInfo", "Event", "*", 15, i+1);
//                mWebSocketClient.send(Cts.request());
//
//            }
//
//            @Override
//            public void onMessage(final String s) {
//                Log.d("Message",s);
//                run(new Runnable() {
//                    @Override
//                    public void run() {
//                        while (true) {
//                            if (s != null) {
//                                if (!s.equals("true") && !s.equals("false") && s != null) {
//                                    mDocuments = ServerToClient.getDataListProducts(s);
//                                    message = s;
//                                    Log.d("data", mDocuments.toString());
//                                    for (Document mDocument : mDocuments) {
//                                        //List<Document> products = (List<Document>) mDocument.get("Product");
//                                        String title = mDocument.getString("__Title__");
//                                        Log.d("in ra", title);
//                                        newList = ServerToClient.GetListArrayProducts(message);
//                                    }
//                                    Log.d("List", newList.toString());
//                                    Log.d("SIze", String.valueOf(newList.size()));
////                                  }
//                                    Fragment fragment = mViewPagerAdapter.getItem(viewPager.getCurrentItem());
//                                    ((FragmentSPDYT) fragment).PushData(newList);
////                                    Log.d("newList", newList.toString());
//                                    break;
//
//                    }
//                };
//
//            }
//
//        };
//    });}
//
//            @Override
//            public void onClose(int code, String reason, boolean remote) {
//
//            }
//
//            @Override
//            public void onError(Exception ex) {
//
//            }
//        };
//        mWebSocketClient.connect();}
//
//    public void getData(List<Products> list){
//
//    }
//}
