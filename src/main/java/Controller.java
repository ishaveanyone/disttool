import com.alibaba.fastjson.JSONObject;
import com.dist.TestClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;


public class Controller {

    @FXML
    private  TextField ip;

    @FXML
    private  TextField port;

    @FXML
    private Button connectBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private Label result;//结果文本


    @FXML
    private ListView listResult;

    @FXML
    private TextField content;//搜索文本内容


    @FXML
    private ChoiceBox optionValue;//下拉工具条

    @FXML
    private Button disConnectBtn; //断开连接

    @FXML
    private Button searchConfigProcessBtn;// 搜索配置好的线程搜索


    @FXML
    private TextField contentConfig; //文件内容


    @FXML
    private ListView listResultConfig;//搜索的进程名称 以及文件路


    @FXML
    private TextField pid;


    @FXML
    private Label exeLable;






    //    文件内的全局引用
    private TestClient testClient;

//    点击连接远程服务器---每一次点击新建一个客户端
    public void connectNIOServer(ActionEvent actionEvent){
//    获取ip还有端口好

        String ipValue=ip.getText();

        int portValue=Integer.valueOf(port.getText());

        testClient=new TestClient(ipValue,Integer.valueOf(portValue));

//        启动客户端
        testClient.start();
        try {

           testClient.sendMsg("open:"+testClient.getSocket().getInetAddress());

           Thread.sleep(5000);

           String msg=testClient.readMsg();

           if(msg!=null){

               result.setText(msg);

           }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

//      测试发送消息
    public void search(ActionEvent actionEvent){
        String msg=content.getText();

        try {
            String value =optionValue.getValue().toString();
            if("name".equals(value)){

                ObservableList items = FXCollections.observableArrayList();

                testClient.sendMsg("findByName:"+msg);

                Thread.sleep(5000);
                String resultJson=testClient.readMsg();

                JSONObject jsonObject = JSONObject.parseObject(resultJson);
                //json对象转Map
                Map<String,Object> map = (Map<String,Object>)jsonObject;
                for(Object str: map.values()){
                    items.add((String)str);
                }
                listResult.setItems(items);

                result.setText("通过名称查找数据成功！！");
            }else if("port".equals(value)){
                testClient.sendMsg("findByPort:"+msg);
                ObservableList items = FXCollections.observableArrayList();
                Thread.sleep(5000);
                String resultJson=testClient.readMsg();

                JSONObject jsonObject = JSONObject.parseObject(resultJson);
                //json对象转Map
                Map<String,Object> map = (Map<String,Object>)jsonObject;
                for(Object str: map.values()){
                    items.add((String)str);
                }
                listResult.setItems(items);
                result.setText("通过端口查找数据成功！！");
            }else{
                testClient.sendMsg("findByNameAndPort:"+msg);
                ObservableList items = FXCollections.observableArrayList();

                testClient.sendMsg("findByName:"+msg);

                Thread.sleep(5000);
                String resultJson=testClient.readMsg();

                JSONObject jsonObject = JSONObject.parseObject(resultJson);
                //json对象转Map
                Map<String,Object> map = (Map<String,Object>)jsonObject;
                for(Object str: map.values()){
                    items.add((String)str);
                }
                listResult.setItems(items);
                testClient.sendMsg("findByPort:"+msg);
                result.setText("通过端口和名称查找数据成功！！");
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public void mouseType()
    {

        try {
            String test = (String) this.listResult.getFocusModel().getFocusedItem();
            ObservableList items = FXCollections.observableArrayList();
            String tmp1 = test.split(",")[1].substring(1, test.split(",")[1].length() - 1);

            this.pid.setText(tmp1);

            //        传输进程名称并且进行数据查询到路径
            String exeName = test.split(",")[0].substring(1, test.split(",")[0].length() - 1);
            testClient.sendMsg("findPathByExeName:" + exeName);

            Thread.sleep(5000);
            String resultJson = testClient.readMsg();

            JSONObject jsonObject = JSONObject.parseObject(resultJson);
            //json对象转Map
            Map<String,Object> map = (Map<String,Object>)jsonObject;
            for(Object str: map.values()){
                items.add((String)str);
            }
            listResultConfig.setItems(items);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void kill()
    {
        String msg = this.pid.getText();
        testClient.sendMsg("kill:"+msg);
        result.setText("杀死进程成功！！");
    }


    public void disConnect(){
        testClient.sendMsg("close:"+testClient.getSocket().getInetAddress());
        try {
            result.setText("关闭成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    //搜索配置好的线程
    public void searchConfigProcess(){
        String keyword=contentConfig.getText();

        testClient.sendMsg("findProcess:"+keyword);
//        等待5秒钟时间
        try {
            ObservableList items = FXCollections.observableArrayList();
            Thread.sleep(5000);
            String resultJson=testClient.readMsg();
            JSONObject jsonObject = JSONObject.parseObject(resultJson);
            //json对象转Map
            Map<String,Object> map = (Map<String,Object>)jsonObject;

            Set<String> keys= map.keySet();
//          通过key值获获取数据
            for(String key:keys){
                if(null==keyword||"".equals(keyword)){
                    items.add(key+"-》"+(String) map.get(key));
                    continue;
                }
                else if(key.equals(keyword)){
                    items.add(key+"-》"+(String) map.get(key));
                    continue;
                }
            }
            listResultConfig.setItems(items);
            result.setText("查找所有配置运行的进程成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void mouseConfigListType()
    {
        String test = (String)this.listResultConfig.getFocusModel().getFocusedItem();
        String tmp1=test;
        if(test.contains("-》")){
            tmp1= test.substring(test.indexOf("-》")+2);
        }

//       展示选中进程的具体路径
        this.exeLable.setText(tmp1);
    }


    //    c重启
    public  void reStart(ActionEvent actionEvent){

//        获取选择的目标

        testClient.sendMsg("restartExe:"+exeLable.getText());

        result.setText("重启中。。。。。");

        try {

            Thread.sleep(5000);

        } catch (InterruptedException e) {

            e.printStackTrace();

        }
//        String resultJson=testClient.readMsg();

        result.setText("重启成功");

    }

    public Controller() {




    }





}
