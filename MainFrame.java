import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by xilingyuli on 2016/10/17.
 * 主窗口
 */
public class MainFrame extends JFrame {

    JTextArea result;
    JTextField path;
    JButton button,button2;

    //更改ui样式
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
    }

    MainFrame()
    {
        super("Fasta算法");

        path = new JTextField();
        path.setText("src/sequence.txt");

        path.setPreferredSize(new Dimension(150,25));
        button = new JButton("选择文件");
        button.setPreferredSize(new Dimension(120,25));
        //选择源码文件
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(path.getText());
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.showDialog(path,"确定");
                File f = chooser.getSelectedFile();
                if(f!=null) {
                    path.setText(f.getPath());
                }
            }
        });



        button2 = new JButton("确定");
        button2.setPreferredSize(new Dimension(200,25));
        button2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = getFile();
                ArrayList<String> sequence = new ArrayList<String> ();
                String res = "";
                try {
                    String encoding="GBK";
                    File file=new File(fileName);
                    if(file.isFile() && file.exists()){ //判断文件是否存
                    InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                        BufferedReader bufferedReader = new BufferedReader(read);
                        String lineTxt = null;
                        while((lineTxt = bufferedReader.readLine()) != null){
                            sequence.add(lineTxt);
                            res += lineTxt+"\n";
                        }
                        read.close();
                    }else{
                        System.out.println("找不到指定的文件");
                    }
                } catch (Exception e2) {
                    System.out.println("读取文件内容出错");
                    e2.printStackTrace();
                }

                Map<Character,ArrayList<Integer>> hashMap = new HashMap<Character,ArrayList<Integer>>();

                String T1 = sequence.get(0);

                for (int i=0;i<T1.length();i++){
                    if (hashMap.containsKey(T1.charAt(i))){
                        hashMap.get(T1.charAt(i)).add(i);
                    }else{
                        ArrayList<Integer> init = new ArrayList<Integer>();
                        init.add(i);
                        hashMap.put(T1.charAt(i),init);
                    }
                }

                for (Character base : hashMap.keySet()){
                    System.out.print(base+" ");
                    for (Integer site:hashMap.get(base)){
                        System.out.print(site+" ");
                    }
                    System.out.println();
                }




                for (int i=1;i<sequence.size();i++){
                    Map<Integer,ArrayList<Integer>> compareMap = new HashMap<Integer,ArrayList<Integer>>();
                    for(int j =0;j<sequence.get(i).length();j++){
                        char base = sequence.get(i).charAt(j);
                        ArrayList<Integer> sites = new ArrayList<Integer>();
                        for(int k : hashMap.get(base)){
                            sites.add(j-k);
                        }
                        compareMap.put(j,sites);
                    }

                    System.out.println("第"+i+"组偏移量比较");
                    Map<Integer,Integer> offsetMap = new HashMap<Integer,Integer>();
                    for (int site : compareMap.keySet()){
                        System.out.print(site+": ");
                        for (Integer offset:compareMap.get(site)){
                            if (!offsetMap.containsKey(offset)){
                                offsetMap.put(offset,0);
                            }
                            offsetMap.put(offset,offsetMap.get(offset)+1);
                            System.out.print(offset+" ");
                        }
                        System.out.println();
                    }
                    ArrayList<Integer> maxOffset = new ArrayList<Integer>();
                    int max = -1;

                    for (int offset : offsetMap.keySet()){
                        System.out.println(offsetMap.get(offset)+" hahahahaha");
                        if (offsetMap.get(offset)>max) {
                            maxOffset.clear();
                            maxOffset.add(offset);
                            max = offsetMap.get(offset);
                        }else if(offsetMap.get(offset) == max){
                            maxOffset.add(offset);
                        }
                    }
                    res += "第"+i+"组最大位移量: ";
                    for (int offset : maxOffset){
                        res += offset+" ";
                    }
                    res += "\n";
                }
                setResult(res);
            }
        });

        result = new JTextArea();
        result.setPreferredSize(new Dimension(250,200));

        setPreferredSize(new Dimension(300,350));
        setLayout(new FlowLayout());
        add(path);
        add(button);

        add(button2);
        add(result);
        pack();

        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public String getFile()
    {
        return path.getText();
    }

    public void setResult(String results)
    {
        result.setText(results);
    }


}
